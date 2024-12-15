package com.ravi.waterlilly.features.guest;

import com.ravi.waterlilly.exception.APIException;
import com.ravi.waterlilly.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    public GuestServiceImpl(GuestRepository guestRepository, ModelMapper modelMapper) {
        this.guestRepository = guestRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public GuestResponse getAllGuests() {
        List<Guest> guests = guestRepository.findAll();
        List<GuestDTO> guestDTOS = guests.stream()
                .map((guest -> modelMapper.map(guest, GuestDTO.class)))
                .toList();

        GuestResponse guestResponse = new GuestResponse();
        guestResponse.setData(guestDTOS);
        return guestResponse;
    }

    @Override
    public void addGuest(GuestDTO guestDTO) {
        Guest exisistingGuest = guestRepository.findGuestByNic(guestDTO.getNic());

        // Add validation -> Check the NIC exists!
        if (exisistingGuest != null)
            throw new APIException("Guest with " + exisistingGuest.getNic() +" already exists!");

        Guest newGuest = modelMapper.map(guestDTO, Guest.class);
        guestRepository.save(newGuest);
    }

    @Override
    public GuestDTO updateGuest(Long id, GuestDTO guestDTO) {
        Guest exisistingGuest = guestRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Guest" , "guestId", id));

        Guest guestByNic = guestRepository.findGuestByNic(guestDTO.getNic());
        if (guestByNic != null && !guestByNic.getNic().equals(exisistingGuest.getNic()))
            throw new APIException("Guest with " + exisistingGuest.getNic() +" already exists!");

        Guest guest = modelMapper.map(guestDTO, Guest.class);
        guest.setGuestId(exisistingGuest.getGuestId());
        Guest savedGuest = guestRepository.save(guest);

        return modelMapper.map(savedGuest, GuestDTO.class);
    }

    @Override
    public void deleteGuest(Long id) {
        Guest exisistingGuest = guestRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Guest" , "guestId", id));
        exisistingGuest.setDeleted(true);
        guestRepository.save(exisistingGuest);
    }


}
