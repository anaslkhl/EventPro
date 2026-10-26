
package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.CreateTicketCategoryRequest;
import com.eventProApi.eventpro.dto.TicketCategoryResponse;
import com.eventProApi.eventpro.entity.TicketCategory;
import com.eventProApi.eventpro.mapper.TicketCategoryMapper;
import com.eventProApi.eventpro.repository.TicketCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketCategoryService {

    private final TicketCategoryRepository ticketCategoryRepository;
    private final TicketCategoryMapper ticketCategoryMapper;


    public TicketCategoryResponse create(
            CreateTicketCategoryRequest request) {

        TicketCategory category =
                ticketCategoryMapper.toEntity(request);

        TicketCategory saved =
                ticketCategoryRepository.save(category);

        return ticketCategoryMapper.toResponse(saved);
    }
    
    @Transactional(readOnly = true)
    public List<TicketCategoryResponse> getAll() {

        return ticketCategoryMapper.toResponseList(
                ticketCategoryRepository.findAll()
        );
    }
}
