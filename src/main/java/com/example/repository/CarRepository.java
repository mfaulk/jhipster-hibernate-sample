package com.example.repository;

import com.example.domain.Car;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Car entity.
 */
@SuppressWarnings("unused")
public interface CarRepository extends JpaRepository<Car,Long> {

    // finds all Cars with car.owner.id = id
    List<Car> findByOwner_Id(Long id);

}
