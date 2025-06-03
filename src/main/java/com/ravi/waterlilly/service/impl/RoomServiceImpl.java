package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.inventory.InventoryQuantityDTO;
import com.ravi.waterlilly.payload.room.RoomCardForReservationDTO;
import com.ravi.waterlilly.payload.room.RoomPayloadDTO;
import com.ravi.waterlilly.payload.room.RoomCardDTO;
import com.ravi.waterlilly.payload.room.RoomReferenceDTO;
import com.ravi.waterlilly.repository.*;
import com.ravi.waterlilly.service.*;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Implementation of room service
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final PrivilegeUtils privilegeUtils;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final RoomTypeService roomTypeService;
    private final RoomStatusService roomStatusService;
    private final InventoryService inventoryService;
    private final RoomFacilityService roomFacilityService;

    // Get all rooms
    @Override
    public List<RoomCardDTO> getAllRooms() {

        // check privileges of the user
        privilegeUtils.privilegeCheck("Room", AppConstants.SELECT);

        return roomRepository.findAll().stream()
                .map(room -> {
                    RoomCardDTO roomCardDTO = modelMapper.map(room, RoomCardDTO.class);
                    roomCardDTO.setPrice(basePriceCalculate(room));
                    return roomCardDTO;
                })
                .toList();
    }

    // get all with id and number only
    @Override
    public List<RoomReferenceDTO> getAll() {
        // check privileges of the user
        privilegeUtils.privilegeCheck("Room", AppConstants.SELECT);

        return roomRepository.findAll().stream()
                .map(room -> modelMapper.map(room, RoomReferenceDTO.class))
                .toList();
    }

    // filter rooms
    @Override
    public List<RoomCardDTO> filterRooms(Integer roomTypeId, Integer statusId, BigDecimal minPrice, BigDecimal maxPrice, Integer adults, Integer children, Integer infants, String searchQuery) {
        // Check privileges
        privilegeUtils.privilegeCheck("Room", AppConstants.SELECT);

        // Get all rooms first
        List<Room> allRooms = roomRepository.findRoomsWithFilters(
                roomTypeId, statusId, minPrice, maxPrice, adults, children, infants, searchQuery
        );

        // Apply filters
        return allRooms.stream()
                .map(room -> {
                    RoomCardDTO roomCardDTO = modelMapper.map(room, RoomCardDTO.class);
                    roomCardDTO.setPrice(basePriceCalculate(room));
                    return roomCardDTO;
                })
                .toList();
    }

    // get a single room
    @Override
    public RoomPayloadDTO getSingleRoom(Long roomId) {
        // check privileges of the user
        privilegeUtils.privilegeCheck("Room", AppConstants.SELECT);

        // find room by id and return
        Room room = getRoomById(roomId);

        RoomPayloadDTO mappedRoom = modelMapper.map(room, RoomPayloadDTO.class);
        Set<Integer> facilities = room.getRoomFacility().stream()
                .map(facility -> facility.getId())
                .collect(Collectors.toSet());

        // convert inventory with quantities
        List<InventoryQuantityDTO> quantityDTOS = room.getInventory().stream()
                .map(inventory -> {
                    InventoryQuantityDTO dto = new InventoryQuantityDTO();
                    dto.setInventoryId(inventory.getInventory().getId());
                    dto.setQuantity(inventory.getQuantity());
                    return dto;
                })
                .toList();

        mappedRoom.setRoomFacilityIds(facilities);
        mappedRoom.setPrice(basePriceCalculate(room));
        mappedRoom.setInventoryQuantities(quantityDTOS);
        return mappedRoom;
    }

    // get available rooms for a stay between two dates with adults, children and infants
    @Override
    public List<RoomCardForReservationDTO> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, Integer adults, Integer children, Integer infants) {

        // convert date into a reservation time period.
        LocalDateTime checkInDateTime = checkInDate.atTime(AppConstants.DEFAULT_CHECK_IN_TIME);
        LocalDateTime checkOutDateTime = checkOutDate.atTime(AppConstants.DEFAULT_CHECK_OUT_TIME);

        privilegeUtils.privilegeCheck("Room", AppConstants.SELECT);

        List<Room> availableRooms = roomRepository.findAvailableRooms(
                checkInDateTime,
                checkOutDateTime,
                adults,
                children,
                infants);

        if (!availableRooms.isEmpty()) {
            return availableRooms.stream()
                    .map(room -> {
                        RoomCardForReservationDTO roomCardDTO = modelMapper.map(room, RoomCardForReservationDTO.class);
                        roomCardDTO.setPrice(calculateReservationTotalPrice(room, checkInDateTime, checkOutDateTime));
                        roomCardDTO.setFacilities(
                                room.getRoomFacility()
                                        .stream()
                                        .map(RoomFacility::getName)
                                        .collect(Collectors.toSet())
                        );
                        return roomCardDTO;
                    })
                    .toList();
        }
        return List.of();
    }

    // add a new room
    @Override
    @Transactional
    public void addNewRoom(RoomPayloadDTO roomPayloadDTO) throws IOException {
        // check privileges of the user
        privilegeUtils.privilegeCheck("Room", AppConstants.INSERT);

        // check if the room number is in db
        validateRoomUniqueness(roomPayloadDTO, null);

        // map and save
        Room newRoom = new Room();
        updateRoomFields(newRoom, roomPayloadDTO);
        roomRepository.save(newRoom);
    }

    // update a room
    @Override
    @Transactional
    public void updateRoom(RoomPayloadDTO roomPayloadDTO, Long roomId) throws IOException {
        // check privileges of the user
        privilegeUtils.privilegeCheck("Room", AppConstants.UPDATE);

        // get existing room
        Room existingRoom = getRoomById(roomId);

        // validate status changes
        roomStatusService.validateStatusChangeForReservations(existingRoom, roomPayloadDTO.getStatusId());

        // check if the room number is in db
        validateRoomUniqueness(roomPayloadDTO, roomId);

        // existing image
        String existingImage = existingRoom.getPhoto();

        //map and save
        updateRoomFields(existingRoom, roomPayloadDTO);
        roomRepository.save(existingRoom);

        // delete image from server if image not matches
        if (existingImage != null && !existingImage.equals(existingRoom.getPhoto())) {
            imageService.deleteImage("rooms", existingImage);
        }
    }

    // delete room - soft delete
    @Override
    public void deleteRoom(Long roomId) {
        // check privileges of the user
        privilegeUtils.privilegeCheck("Room", AppConstants.DELETE);
        roomStatusService.setRoomToOutOfService(roomId);
    }

    // restore deleted room
    @Override
    public void restoreRoom(Long roomId) {
        // check privileges of the user
        privilegeUtils.privilegeCheck("Room", AppConstants.UPDATE);
        roomStatusService.setRoomToAvailable(roomId);
    }

    // Helper method to fetch room by id
    @Override
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "room id", roomId));
    }

    // Helper method to calculate total room price with pricing rules for the stay
    @Override
    public BigDecimal calculateReservationTotalPrice(Room room, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        BigDecimal roomPrice = roomTypeService.calculateRoomTypePriceForReservation(room.getRoomType(), checkInDate, checkOutDate);
        return calculatePriceWithFacilities(room, roomPrice, checkInDate, checkOutDate);
    }

    // validate room availability when reserving
    @Override
    public void validateRoomAvailability(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        // Convert LocalDate to LocalDateTime for consistent checking
        LocalDateTime checkInDateTime = checkIn.atTime(AppConstants.DEFAULT_CHECK_IN_TIME);
        LocalDateTime checkOutDateTime = checkOut.atTime(AppConstants.DEFAULT_CHECK_OUT_TIME);

        // Use the same room availability check as in RoomRepository
        List<Room> availableRooms = roomRepository.findAvailableRooms(
                checkInDateTime,
                checkOutDateTime,
                1,  // minimum adults
                0,  // minimum children
                0   // minimum infants
        );

        boolean isAvailable = availableRooms.stream()
                .anyMatch(room -> room.getId().equals(roomId));

        if (!isAvailable) {
            throw new APIException("Room is not available for the selected dates");
        }
    }

    // Modify the updateRoomFields method
    private void updateRoomFields(Room room, RoomPayloadDTO roomPayloadDTO) {
        room.setNumber(roomPayloadDTO.getNumber());
        room.setAdultNo(roomPayloadDTO.getAdultNo());
        room.setChildNo(roomPayloadDTO.getChildNo());
        room.setInfantNo(roomPayloadDTO.getInfantNo());
        room.setFloorNumber(roomPayloadDTO.getFloorNumber());
        room.setPhoto(roomPayloadDTO.getPhoto());
        room.setDescription(roomPayloadDTO.getDescription());
        room.setStatus(roomStatusService.getStatusById(roomPayloadDTO.getStatusId()));
        room.setRoomType(roomTypeService.fetchRoomTypeById(roomPayloadDTO.getRoomTypeId()));
        room.setRoomFacility(fetchRoomFacilities(roomPayloadDTO.getRoomFacilityIds()));

        LocalDateTime now = LocalDateTime.now();
        if (room.getId() == null) {
            room.setAddedDatetime(now);
        }
        room.setLastModifiedDatetime(now);

        updateRoomInventory(roomPayloadDTO, room);
    }

    // Helper method to update room inventory and main inventory
    private void updateRoomInventory(RoomPayloadDTO roomPayloadDTO, Room room) {
        if (roomPayloadDTO.getInventoryQuantities() == null) {
            return;
        }

        if (room.getInventory() != null && !room.getInventory().isEmpty()) {
            room.clearInventory();
        }

        // Validate inventory availability
        inventoryService.validateInventoryAvailability(roomPayloadDTO.getInventoryQuantities());

        // Reserve new inventory and update room relationships
        roomPayloadDTO.getInventoryQuantities().forEach(inventoryQuantityDTO -> {
            Inventory inventory = inventoryService.getInventoryById(inventoryQuantityDTO.getInventoryId());
            room.addInventory(inventory, inventoryQuantityDTO.getQuantity());
        });

        // Reserve inventory through service
        inventoryService.reserveInventory(roomPayloadDTO.getInventoryQuantities());
    }

    // Helper method to check if the room is in db
    private void validateRoomUniqueness(RoomPayloadDTO roomPayloadDTO, Long roomId) throws IOException {

        //Check for duplicated named room
        Room existingRoom = roomRepository.findByNumber(roomPayloadDTO.getNumber());
        if (existingRoom != null && !existingRoom.getId().equals(roomId)) {
            imageService.deleteImage("rooms", roomPayloadDTO.getPhoto());
            throw new APIException("Room with number '" + roomPayloadDTO.getNumber() + "' already exists!");
        }
    }

    // Helper method to fetch room facilities
    private Set<RoomFacility> fetchRoomFacilities(Set<Integer> facilities) {
        return facilities.stream()
                .map(facilityId -> roomFacilityService.getRoomFacilityById(facilityId))
                .collect(Collectors.toSet());
    }

    // Generic price calculation method
    private BigDecimal calculatePriceWithFacilities(Room room, BigDecimal roomPrice, LocalDateTime checkIn, LocalDateTime checkOut) {
        long numberOfNights = ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());
        numberOfNights = Math.max(numberOfNights, 1);

        BigDecimal facilityPrice = totalFacilityPriceCalculate(room).multiply(BigDecimal.valueOf(numberOfNights));
        return roomPrice.add(facilityPrice).setScale(2, RoundingMode.HALF_UP);
    }

    // Helper method to calculate the price
    private BigDecimal basePriceCalculate(Room room) {
        BigDecimal basePrice = room.getRoomType().getBasePrice();
        BigDecimal facilityPrice = totalFacilityPriceCalculate(room);
        return basePrice.add(facilityPrice);
    }

    // Helper method to calculate total facility price
    private BigDecimal totalFacilityPriceCalculate(Room room) {
        BigDecimal facilityPrice = BigDecimal.ZERO;

        if (!room.getRoomFacility().isEmpty()) {
            for (RoomFacility facility : room.getRoomFacility()) {
                facilityPrice = facilityPrice.add(facility.getPrice());
            }
        }
        return facilityPrice;
    }
}