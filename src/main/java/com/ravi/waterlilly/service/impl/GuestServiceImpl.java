package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.config.AppConstants;
import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.*;
import com.ravi.waterlilly.payload.guest.GuestPayloadDTO;
import com.ravi.waterlilly.payload.guest.GuestReferenceDTO;
import com.ravi.waterlilly.payload.guest.GuestTableDataDTO;
import com.ravi.waterlilly.payload.guest.GuestPageResponse;
import com.ravi.waterlilly.repository.*;
import com.ravi.waterlilly.service.*;
import com.ravi.waterlilly.utils.PrivilegeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

// Implementation of Guest Service.
@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;
    private final PrivilegeUtils privilegeUtils;
    private final IDTypeService idTypeService;
    private final NationalityService nationalityService;
    private final CivilStatusService civilStatusService;
    private final GenderService genderService;
    private final TitleService titleService;
    private final StatusService statusService;


    // Get all guests with pagination, sorting, and search options
    @Override
    public GuestPageResponse getAllGuests(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String searchQuery) {

        // privilege check
        privilegeUtils.privilegeCheck("Guest", AppConstants.SELECT);

        // Determine the sorting order and sort by
        Sort sortAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Create a Pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortAndOrder);
        Page<Guest> guestPage = StringUtils.hasText(searchQuery)
                ? guestRepository.searchGuests(searchQuery.trim(), pageable)
                : guestRepository.findAll(pageable);

        // Extract the list of guests from the page
        List<Guest> guests = guestPage.getContent();

        // Map the list of Guests to a list of GuestTableDTO using ModelMapper
        List<GuestTableDataDTO> guestTableDataDTOS = guests.stream()
                .map(guest -> modelMapper.map(guest, GuestTableDataDTO.class))
                .toList();

        // Create a response object of guest data
        return new GuestPageResponse(
                guestTableDataDTOS,
                guestPage.getNumber(),
                guestPage.getSize(),
                guestPage.getTotalElements(),
                guestPage.getTotalPages(),
                guestPage.isLast()
        );
    }

    // get all guests without pagination
    @Override
    public List<GuestReferenceDTO> getAllGuestsNoPagination() {

        // check privileges
        privilegeUtils.privilegeCheck("Guest", AppConstants.SELECT);

        return guestRepository.findAll().stream()
                .map(guest -> modelMapper.map(guest, GuestReferenceDTO.class))
                .toList();
    }

    //Get one guest data
    @Override
    public GuestPayloadDTO getAnGuest(Long guestId) {
        privilegeUtils.privilegeCheck("Guest", AppConstants.SELECT);

        //Get single guest details from id
        Guest guest = getGuestById(guestId);

        return modelMapper.map(guest, GuestPayloadDTO.class);
    }

    //Add new guest
    @Override
    public void addGuest(GuestPayloadDTO guestDTO) {

        // privilege check
        privilegeUtils.privilegeCheck("Guest", AppConstants.INSERT);

        // check if the new Guest is already in database
        validateGuestUniqueness(guestDTO, null);

        Guest newGuest = new Guest();

        updateGuestFields(newGuest, guestDTO);
        newGuest.setStatus(statusService.getActiveStatus());
        guestRepository.save(newGuest);
    }

    //Update guest
    @Transactional
    @Override
    public void updateGuest(GuestPayloadDTO guestDTO, Long guestId) {

        //Privilege check
        privilegeUtils.privilegeCheck("Guest", AppConstants.UPDATE);

        // Check if guest exists by the id
        Guest existingGuest = getGuestById(guestId);

        // check if the new Guest is already in database
        validateGuestUniqueness(guestDTO, guestId);

        //Update and save guest
        updateGuestFields(existingGuest, guestDTO);
        guestRepository.save(existingGuest);
    }

    //Delete guest - soft delete change the status only.
    @Transactional
    @Override
    public void deleteGuest(Long guestId) {

        // privilege check
        privilegeUtils.privilegeCheck("Guest", AppConstants.DELETE);

        // Check if guest exists
        Guest existingGuest = getGuestById(guestId);

        // get the deleted status and set it to the guest
        Status deletedStatus = statusService.getDeletedStatus();
        existingGuest.setStatus(deletedStatus);
        existingGuest.setDeletedDateTime(LocalDateTime.now());
        guestRepository.save(existingGuest);
    }

    // restore a guest
    @Override
    public void restoreGuest(Long guestId) {
        // privilege check
        privilegeUtils.privilegeCheck("Guest", AppConstants.DELETE);

        // Check if guest exists
        Guest existingGuest = getGuestById(guestId);

        // get the deleted status and set it to the guest
        existingGuest.setStatus(statusService.getActiveStatus());
        existingGuest.setDeletedDateTime(null);
        guestRepository.save(existingGuest);
    }

    // get guest by id
    @Override
    public Guest getGuestById(Long guestId) {
        return guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "guestId", guestId));
    }

    // Helper method to update guest fields
    private void updateGuestFields(Guest guest, GuestPayloadDTO guestDTO) {
        guest.setTitle(titleService.getTitleById(guestDTO.getTitleId()));
        guest.setFullName(guestDTO.getFullName());
        guest.setCallingName(guestDTO.getCallingName());
        guest.setIdNumber(guestDTO.getIdNumber());
        guest.setDob(guestDTO.getDob());
        guest.setNote(guestDTO.getNote());
        guest.setAddress(guestDTO.getAddress());
        guest.setMobileNo(guestDTO.getMobileNo());
        guest.setEmail(guestDTO.getEmail());
        guest.setEmergencyNo(guestDTO.getEmergencyNo());
        guest.setIdType(idTypeService.getIdTypeById(guestDTO.getIdTypeId()));
        guest.setCivilStatus(civilStatusService.getCivilStatusById(guestDTO.getCivilStatusId()));
        guest.setGender(genderService.getGenderById(guestDTO.getGenderId()));
        guest.setNationality(nationalityService.getNationalityById(guestDTO.getNationalityId()));

        LocalDateTime now = LocalDateTime.now();
        if (guest.getId() == null) {
            guest.setAddedDateTime(now);
        }
        guest.setLastModifiedDateTime(now);
    }

    // Helper method to check if the guest is in db
    private void validateGuestUniqueness(GuestPayloadDTO guestDTO, Long guestId) {
        Nationality nationality = nationalityService.getNationalityById(guestDTO.getNationalityId());
        IDType idType = idTypeService.getIdTypeById(guestDTO.getIdTypeId());

        // check guest exist with same nationality, id type and id number
        Guest checkDuplicatedGuest = guestRepository.findGuestByNationalityIdTypeIdNumber(
                nationality.getId(), idType.getId(), guestDTO.getIdNumber()
        );

        if (checkDuplicatedGuest != null && !checkDuplicatedGuest.getId().equals(guestId)) {
            throw new APIException(
                    String.format("An guest with %s '%s' from %s already exists!",
                            idType.getName(),
                            guestDTO.getIdNumber(),
                            nationality.getName()
                    )
            );
        }

        // check existing guest with email. Also exclude same guest id
        Guest existingGuestEmail = guestRepository.findGuestByEmail(guestDTO.getEmail());
        if (existingGuestEmail != null && !existingGuestEmail.getId().equals(guestId)) {
            throw new APIException("An guest with email " + guestDTO.getEmail() + " already exists!");
        }

        // check existing guest with mobile number. Also exclude same guest id
        Guest existingGuestMobileNo = guestRepository.findGuestByMobileNo(guestDTO.getMobileNo());
        if (existingGuestMobileNo != null && !existingGuestMobileNo.getId().equals(guestId)) {
            throw new APIException("An guest with mobile number " + guestDTO.getMobileNo() + " already exists!");
        }
    }
}