package com.eventProApi.eventpro.entity;

import com.eventProApi.eventpro.entity.enums.TypeReduction;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "promotions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_promotions_code", columnNames = "code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TypeReduction typeReduction;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valeurReduction;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Column(nullable = false)
    private Integer limiteUtilisation;

    @Column(nullable = false)
    private Integer nombreUtilisations = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;
}