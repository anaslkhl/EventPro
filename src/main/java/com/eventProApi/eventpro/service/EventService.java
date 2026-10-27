package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.CreateEventRequest;
import com.eventProApi.eventpro.dto.EventResponse;
import com.eventProApi.eventpro.entity.Event;
import com.eventProApi.eventpro.entity.Organizer;
import com.eventProApi.eventpro.mapper.EventMapper;
import com.eventProApi.eventpro.repository.EventRepository;
import com.eventProApi.eventpro.repository.OrganizerRepository;
import com.eventProApi.eventpro.specification.EventSpecs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final OrganizerRepository organizerRepository;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;



    @Transactional
    public EventResponse createEvent(CreateEventRequest eventRequest){

        if(!eventRequest.getEndDate().isAfter(eventRequest.getStartDate())){
            throw new IllegalArgumentException("End date must be after start date !");
        }

        Organizer organizer = organizerRepository.findById(eventRequest.getOrganizerId())
                .orElseThrow(() -> new NoSuchElementException("The organazier not found !"));



        Event event = eventMapper.toEntity(eventRequest);

        event.setOrganizer(organizer);


        event.setPublished(true);

        Event savedEvent = eventRepository.save(event);

        return eventMapper.toResponse(savedEvent);


    }


    @Transactional
    public EventResponse getEventById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Event not found.")
                );

        return eventMapper.toResponse(event);
    }

    @Transactional
    public Page<EventResponse> getAllEvents(int page, int size){

        Pageable pageable = PageRequest.of(page, size);

        return eventRepository.findAll(pageable).map(eventMapper::toResponse);
    }


    @Transactional
    public List<EventResponse> getAllEventsSorted(String sortBy, String direction){


        Sort sort = Sort.by(Sort.Direction.fromString(direction),sortBy);

        return eventRepository.findAll(sort).stream().map(eventMapper::toResponse).toList();
    }


    @Transactional
    public List<EventResponse> searchByPrice(BigDecimal minPrice, BigDecimal maxPrice){


        return eventRepository.findByPriceBetween(minPrice, maxPrice)
                .stream().map(eventMapper::toResponse).toList();
    }


    @Transactional
    public List<EventResponse> searchEvents(String title, String categoryName){

        return eventRepository.findAll(EventSpecs.search(title, categoryName))
                .stream().map(eventMapper::toResponse).toList();
    }


    @Transactional
    public void deleteEvent(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Event not found.")
                );

        eventRepository.delete(event);
    }
}
