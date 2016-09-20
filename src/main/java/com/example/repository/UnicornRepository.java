package com.example.repository;

import com.example.domain.Unicorn;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Unicorn entity.
 */
@SuppressWarnings("unused")
public interface UnicornRepository extends JpaRepository<Unicorn,Long> {

}
