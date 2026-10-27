
package com.eventProApi.eventpro.service;

import com.eventProApi.eventpro.dto.CreatePromotionRequest;
import com.eventProApi.eventpro.dto.PromotionResponse;
import com.eventProApi.eventpro.entity.Promotion;
import com.eventProApi.eventpro.entity.User;
import com.eventProApi.eventpro.mapper.PromotionMapper;
import com.eventProApi.eventpro.repository.OrganizerRepository;
import com.eventProApi.eventpro.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final OrganizerRepository organizerRepository;
    private final PromotionMapper promotionMapper;



    @Transactional
    public PromotionResponse create(CreatePromotionRequest request) {
        validateDates(request);

        if (promotionRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException("A promotion with this code already exists.");
        }

        User organizer = organizerRepository.findById(request.organizerId())
                .orElseThrow(() -> new NoSuchElementException("Organizer not found."));

        Promotion promotion = promotionMapper.toEntity(request);

        promotion.setLimiteUtilisation(request.limiteUtilisation());
        promotion.setOrganizer(organizer);
        promotion.setNombreUtilisations(0);

        return promotionMapper.toResponse(promotionRepository.save(promotion));
    }

    @Transactional(readOnly = true)
    public PromotionResponse getById(UUID id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Promotion not found."));

        return promotionMapper.toResponse(promotion);
    }

    @Transactional(readOnly = true)
    public List<PromotionResponse> getAll() {
        return promotionMapper.toResponseList(promotionRepository.findAll());
    }

    public PromotionResponse update(
            UUID id, CreatePromotionRequest request) {

        validateDates(request);

        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Promotion not found."));

        if (promotionRepository.existsByCodeAndIdNot(
                request.code(), id)) {
            throw new IllegalArgumentException("A promotion with this code already exists.");
        }

        User organizer = organizerRepository.findById(request.organizerId())
                .orElseThrow(() -> new NoSuchElementException("Organizer not found."));

        promotion.setCode(request.code());
        promotion.setTypeReduction(request.typeReduction());
        promotion.setValeurReduction(request.valeurReduction());
        promotion.setDateDebut(request.dateDebut());
        promotion.setDateFin(request.dateFin());
        promotion.setLimiteUtilisation(request.limiteUtilisation());
        promotion.setOrganizer(organizer);

        return promotionMapper.toResponse(promotionRepository.save(promotion));
    }

    public void delete(UUID id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Promotion not found."));

        promotionRepository.delete(promotion);
    }

    private void validateDates(CreatePromotionRequest request) {
        if (request.dateFin() == null || request.dateDebut() == null) {
            return;
        }

        if (request.dateFin().isBefore(request.dateDebut())) {
            throw new IllegalArgumentException("End date must be on or after start date.");
        }
    }
}
