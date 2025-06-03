package com.ravi.waterlilly.service.impl;

import com.ravi.waterlilly.exception.ResourceNotFoundException;
import com.ravi.waterlilly.model.Title;
import com.ravi.waterlilly.payload.ReferenceDataDTO;
import com.ravi.waterlilly.repository.TitleRepository;
import com.ravi.waterlilly.service.TitleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// implementation of title service
@Service
@RequiredArgsConstructor
public class TitleServiceImpl implements TitleService {
    private final TitleRepository titleRepository;
    private final ModelMapper modelMapper;

    // get all titles
    @Override
    public List<ReferenceDataDTO> getAllTitles() {
        return titleRepository.findAll().stream()
                .map(title -> modelMapper.map(title, ReferenceDataDTO.class))
                .toList();
    }

    // get title by id
    @Override
    public Title getTitleById(Integer id) {
        return titleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Title", "id", id.toString()));
    }
}
