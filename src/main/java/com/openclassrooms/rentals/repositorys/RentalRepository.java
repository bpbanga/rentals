package com.openclassrooms.rentals.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.rentals.models.RentalEntity;

/**
 * Repository interface for managing {@link RentalEntity} persistence.  
 * <p>  
 * Extends {@link JpaRepository} to provide standard CRUD operations  
 * and database interactions for message entities.  
 * </p>  
 *  
 * @see JpaRepository  
 */
@Repository
public interface RentalRepository extends JpaRepository<RentalEntity, Long> {

    Optional<RentalEntity> findById(long id);

}
