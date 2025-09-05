package com.openclassrooms.rentals.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a rental entity in the database.
 * <p>
 * This class is mapped to the {@code rentals} table in the database, and
 * contains the rental's information including the price, the surface,
 * the creation and update dates, the name and a picture, and also the user 
 * who is the owner as foreign keys in the database.
 * </p>
 * 
 * Lombock is used to generate Getters/Setters and the empty constructor needed
 * by JPA.
 * 
 */
@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
public class RentalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "surface")
    private BigDecimal surface;

    @Column(name = "price", precision = 10, scale = 2, nullable = false, columnDefinition = "NUMERIC")
    private BigDecimal price;

    @Column(name = "picture", length = 255)
    private String picture;

    @Column(name = "description", length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;
}
