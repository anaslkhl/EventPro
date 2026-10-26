
package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.CreateTicketCategoriesPerEventRequest;
import com.eventProApi.eventpro.dto.TicketCategoriesPerEventResponse;
import com.eventProApi.eventpro.entity.Event;
import com.eventProApi.eventpro.entity.TicketCategory;
import com.eventProApi.eventpro.entity.TicketCategoriesPerEvent;
import com.eventProApi.eventpro.mapper.TicketCategoriesPerEventMapper;
import com.eventProApi.eventpro.repository.EventRepository;
import com.eventProApi.eventpro.repository.TicketCategoriesPerEventRepository;
import com.eventProApi.eventpro.repository.TicketCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketCategoriesPerEventService {

    private final TicketCategoriesPerEventRepository assignmentRepository;
    private final EventRepository eventRepository;
    private final TicketCategoryRepository categoryRepository;
    private final TicketCategoriesPerEventMapper assignmentMapper;

    public TicketCategoriesPerEventResponse create(
            UUID eventId,
            CreateTicketCategoriesPerEventRequest request
    ) {
        if (request.maxQuotaPerClient() > request.totalQuantity()) {
            throw new IllegalArgumentException(
                    "Maximum quota per client cannot exceed total quantity."
            );
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Event not found."));

        TicketCategory category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() -> new NoSuchElementException("Ticket category not found."));

        if (assignmentRepository.existsByEventIdAndCategoryId(
                eventId, request.categoryId())) {
            throw new IllegalArgumentException("This ticket category is already assigned to this event.");
        }

        TicketCategoriesPerEvent assignment = assignmentMapper.toEntity(request);

        assignment.setEvent(event);
        assignment.setCategory(category);

        TicketCategoriesPerEvent saved = assignmentRepository.save(assignment);

        return assignmentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TicketCategoriesPerEventResponse> getByEventId(
            UUID eventId
    ) {
        if (!eventRepository.existsById(eventId)) {
            throw new NoSuchElementException("Event not found.");
        }

        return assignmentRepository.findByEventId(eventId)
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();
    }
}
