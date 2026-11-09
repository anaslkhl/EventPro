
package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.*;
import com.eventProApi.eventpro.entity.*;
import com.eventProApi.eventpro.entity.enums.EventStatus;
import com.eventProApi.eventpro.entity.enums.StatutReservation;
import com.eventProApi.eventpro.entity.enums.UserRole;
import com.eventProApi.eventpro.exception.BusinessException;
import com.eventProApi.eventpro.exception.ResourceNotFoundException;
import com.eventProApi.eventpro.mapper.ReservationMapper;
import com.eventProApi.eventpro.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TicketCategoriesPerEventRepository ticketCategoryRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;


    private Client createClient() {
        return Client.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phoneNumber("0600000000")
                .role(UserRole.CLIENT)
                .build();
    }

    private Event createEvent(UUID eventId) {
        Event event = new Event();
        event.setId(eventId);
        event.setStatus(EventStatus.PUBLISHED);
        event.setStartDate(LocalDateTime.now().plusDays(10));
        return event;
    }

    private TicketCategoriesPerEvent createAssignment(
            UUID assignmentId, Event event) {

        TicketCategoriesPerEvent assignment =
                new TicketCategoriesPerEvent();

        assignment.setId(assignmentId);
        assignment.setEvent(event);
        assignment.setUnitPrice(new BigDecimal("300.00"));
        assignment.setTotalQuantity(100);
        assignment.setReservedQuantity(0);
        assignment.setSoldQuantity(0);
        assignment.setMaxQuotaPerClient(5);

        return assignment;
    }

    private CreateReservationRequest createRequest(
            UUID clientId, UUID eventId,
            UUID assignmentId, Integer quantity) {

        return new CreateReservationRequest(
                clientId,
                eventId,
                List.of(new ReservationLineRequest(assignmentId, quantity))
        );
    }

    private void mockValidReservation(
            UUID clientId,
            UUID eventId,
            UUID assignmentId,
            Client client,
            Event event,
            TicketCategoriesPerEvent assignment) {

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        when(ticketCategoryRepository.findByIdForUpdate(assignmentId))
                .thenReturn(Optional.of(assignment));
    }

    private void verifyReservationNotSaved() {
        verify(reservationRepository, never())
                .save(any(Reservation.class));

        verifyNoInteractions(reservationMapper);
    }


    @Test
    void createReservation_shouldCreateReservationSuccessfully() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        Client client = createClient();
        Event event = createEvent(eventId);

        TicketCategoriesPerEvent assignment =
                createAssignment(assignmentId, event);

        CreateReservationRequest request =
                createRequest(clientId, eventId, assignmentId, 2);

        ReservationResponse expectedResponse = new ReservationResponse(
                null, null, null, null,
                StatutReservation.PENDING,
                new BigDecimal("600.00"),
                clientId,
                List.of()
        );

        mockValidReservation(
                clientId, eventId, assignmentId,
                client, event, assignment
        );

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(reservationMapper.toResponse(any(Reservation.class)))
                .thenReturn(expectedResponse);

        ReservationResponse response =
                reservationService.createReservation(request);

        assertNotNull(response);
        assertEquals(StatutReservation.PENDING, response.statut());
        assertEquals(new BigDecimal("600.00"), response.montantTotal());

        assertEquals(2, assignment.getReservedQuantity());

        ArgumentCaptor<Reservation> captor =
                ArgumentCaptor.forClass(Reservation.class);

        verify(reservationRepository).save(captor.capture());

        Reservation saved = captor.getValue();

        assertEquals(StatutReservation.PENDING, saved.getStatut());
        assertEquals(new BigDecimal("600.00"), saved.getMontantTotal());
        assertEquals(1, saved.getReservationLines().size());

        ReservationLine line = saved.getReservationLines().get(0);

        assertEquals(2L, line.getQuantite());
        assertEquals(new BigDecimal("300.00"), line.getPrixUnitaire());
        assertEquals(new BigDecimal("600.00"), line.getSousTotal());

        assertEquals(
                Duration.ofMinutes(10),
                Duration.between(
                        saved.getDateCreation(),
                        saved.getDateExpiration()
                )
        );

        assertSame(client, saved.getClient());

        verify(clientRepository).findById(clientId);
        verify(eventRepository).findById(eventId);
        verify(ticketCategoryRepository).findByIdForUpdate(assignmentId);
        verify(reservationMapper).toResponse(saved);
    }

    // ==================== TEST 2: CLIENT NOT FOUND ====================

    @Test
    void createReservation_shouldThrowExceptionWhenClientNotFound() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());

        CreateReservationRequest request =
                createRequest(clientId, eventId, assignmentId, 2);

        assertThrows(ResourceNotFoundException.class,
                () -> reservationService.createReservation(request));

        verify(clientRepository).findById(clientId);
        verifyNoInteractions(eventRepository);
        verifyReservationNotSaved();
    }

    // ==================== TEST 3: INACTIVE CLIENT ====================

    @Test
    void createReservation_shouldRejectInactiveClient() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        Client client = createClient();
        client.setActive(false);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        CreateReservationRequest request =
                createRequest(clientId, eventId, assignmentId, 2);

        assertThrows(BusinessException.class,
                () -> reservationService.createReservation(request));

        verifyNoInteractions(eventRepository);
        verifyReservationNotSaved();
    }

    // ==================== TEST 4: EMPTY LINES ====================

    @Test
    void createReservation_shouldRejectEmptyReservationLines() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        Client client = createClient();

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        CreateReservationRequest request =
                new CreateReservationRequest(clientId, eventId, List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> reservationService.createReservation(request));

        verifyNoInteractions(eventRepository);
        verifyReservationNotSaved();
    }

    // ==================== TEST 5: INVALID QUANTITIES ====================

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {0, -1})
    void createReservation_shouldRejectInvalidQuantity(Integer quantity) {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        Client client = createClient();

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        CreateReservationRequest request =
                createRequest(clientId, eventId, assignmentId, quantity);

        assertThrows(BusinessException.class,
                () -> reservationService.createReservation(request));

        verifyNoInteractions(eventRepository);
        verifyReservationNotSaved();
    }

    // ==================== TEST 6: EVENT NOT FOUND ====================

    @Test
    void createReservation_shouldThrowExceptionWhenEventNotFound() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(createClient()));

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.empty());

        CreateReservationRequest request =
                createRequest(clientId, eventId, assignmentId, 2);

        assertThrows(ResourceNotFoundException.class,
                () -> reservationService.createReservation(request));

        verifyReservationNotSaved();
    }

    // ==================== TEST 7: UNPUBLISHED EVENT ====================

    @Test
    void createReservation_shouldRejectUnpublishedEvent() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        Client client = createClient();
        Event event = createEvent(eventId);
        event.setStatus(EventStatus.DRAFT);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        CreateReservationRequest request =
                createRequest(clientId, eventId, assignmentId, 2);

        assertThrows(BusinessException.class,
                () -> reservationService.createReservation(request));

        verifyNoInteractions(ticketCategoryRepository);
        verifyReservationNotSaved();
    }

    // ==================== TEST 8: EVENT ALREADY STARTED ====================

    @Test
    void createReservation_shouldRejectEventThatAlreadyStarted() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        Client client = createClient();
        Event event = createEvent(eventId);
        event.setStartDate(LocalDateTime.now().minusDays(1));

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        CreateReservationRequest request =
                createRequest(clientId, eventId, assignmentId, 2);

        assertThrows(BusinessException.class,
                () -> reservationService.createReservation(request));

        verifyNoInteractions(ticketCategoryRepository);
        verifyReservationNotSaved();
    }

    // ==================== TEST 9: CATEGORY NOT FOUND ====================

    @Test
    void createReservation_shouldThrowExceptionWhenAssignmentNotFound() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(createClient()));

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(createEvent(eventId)));

        when(ticketCategoryRepository.findByIdForUpdate(assignmentId))
                .thenReturn(Optional.empty());

        CreateReservationRequest request =
                createRequest(clientId, eventId, assignmentId, 2);

        assertThrows(ResourceNotFoundException.class,
                () -> reservationService.createReservation(request));

        verifyReservationNotSaved();
    }

    // ==================== TEST 10: WRONG EVENT ASSOCIATION ====================

    @Test
    void createReservation_shouldRejectAssignmentFromAnotherEvent() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID anotherEventId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        Client client = createClient();
        Event requestedEvent = createEvent(eventId);
        Event anotherEvent = createEvent(anotherEventId);

        TicketCategoriesPerEvent assignment =
                createAssignment(assignmentId, anotherEvent);

        mockValidReservation(
                clientId, eventId, assignmentId,
                client, requestedEvent, assignment
        );

        CreateReservationRequest request =
                createRequest(clientId, eventId, assignmentId, 2);

        assertThrows(BusinessException.class,
                () -> reservationService.createReservation(request));

        verifyReservationNotSaved();
    }

    // ==================== TEST 11: MAXIMUM QUOTA ====================

    @Test
    void createReservation_shouldRejectWhenMaximumQuotaExceeded() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        Client client = createClient();
        Event event = createEvent(eventId);

        TicketCategoriesPerEvent assignment =
                createAssignment(assignmentId, event);

        // Maximum quota is 5, but the client requests 6.
        CreateReservationRequest request =
                createRequest(clientId, eventId, assignmentId, 6);

        mockValidReservation(
                clientId, eventId, assignmentId,
                client, event, assignment
        );

        assertThrows(BusinessException.class,
                () -> reservationService.createReservation(request));

        assertEquals(0, assignment.getReservedQuantity());

        verifyReservationNotSaved();
    }

    // ==================== TEST 12: INSUFFICIENT AVAILABILITY ====================

    @Test
    void createReservation_shouldRejectWhenTicketsAreUnavailable() {

        UUID clientId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID assignmentId = UUID.randomUUID();

        Client client = createClient();
        Event event = createEvent(eventId);

        TicketCategoriesPerEvent assignment =
                createAssignment(assignmentId, event);

        assignment.setTotalQuantity(10);
        assignment.setReservedQuantity(8);
        assignment.setSoldQuantity(1);


        CreateReservationRequest request =
                createRequest(clientId, eventId, assignmentId, 2);

        mockValidReservation(
                clientId, eventId, assignmentId,
                client, event, assignment
        );

        assertThrows(BusinessException.class,
                () -> reservationService.createReservation(request));

        assertEquals(8, assignment.getReservedQuantity());

        verifyReservationNotSaved();
    }
}
