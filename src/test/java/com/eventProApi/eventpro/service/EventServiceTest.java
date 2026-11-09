package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.CreateEventRequest;
import com.eventProApi.eventpro.dto.EventResponse;
import com.eventProApi.eventpro.entity.Event;
import com.eventProApi.eventpro.entity.Organizer;
import com.eventProApi.eventpro.entity.enums.EventStatus;
import com.eventProApi.eventpro.mapper.EventMapper;
import com.eventProApi.eventpro.repository.EventRepository;
import com.eventProApi.eventpro.repository.OrganizerRepository;
import com.eventProApi.eventpro.repository.TicketCategoriesPerEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {


    @Mock
    private OrganizerRepository organizerRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private TicketCategoriesPerEventRepository ticketCategoriesPerEventRepository;


    @InjectMocks
    private EventService eventService;


    @Test
    void createEventTest(){


        UUID organizerId = UUID.randomUUID();


        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(2);


        CreateEventRequest eventRequest = CreateEventRequest.builder()
                .title("Java Introduction")
                .description("An introduction to Java programming language ")
                .location("Marrakech")
                .startDate(startDate)
                .endDate(endDate)
                .capacity(200)
                .price(new  BigDecimal("150.00"))
                .organizerId(organizerId)
                .build();

        Organizer organizer = Organizer.builder()
                .organizationName("The black milk organization")
                .build();


        Event event = new Event();

        Event savedEvent = new Event();

        EventResponse eventResponse = EventResponse.builder()
                .title("Java Introduction")
                .status(EventStatus.DRAFT)
                .build();

        when(organizerRepository.findById(organizerId)).thenReturn(Optional.of(organizer));

        when(eventMapper.toEntity(eventRequest)).thenReturn(event);

        when(eventRepository.save(event)).thenReturn(savedEvent);

        when(eventMapper.toResponse(savedEvent)).thenReturn(eventResponse);

        EventResponse response = eventService.createEvent(eventRequest);

        assertNotNull(response);
        assertEquals(EventStatus.DRAFT, event.getStatus());
        assertSame(organizer, event.getOrganizer());
        assertEquals("Java Introduction", response.getTitle());


        verify(organizerRepository).findById(organizerId);
        verify(eventRepository).save(event);
        verify(eventMapper).toResponse(savedEvent);
    }


    @Test
    void createEvent_shouldThrowExceptionWhenEndDateIsBeforeStartDate() {


        LocalDateTime startDate = LocalDateTime.now().plusDays(10);
        LocalDateTime endDate = startDate.minusDays(2);

        CreateEventRequest request = CreateEventRequest.builder()
                .title("Java Conference")
                .description("Java development conference")
                .location("Casablanca")
                .startDate(startDate)
                .endDate(endDate)
                .capacity(200)
                .price(new BigDecimal("150.00"))
                .organizerId(UUID.randomUUID())
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> eventService.createEvent(request)
        );

        verifyNoInteractions(organizerRepository, eventRepository);
    }
}
