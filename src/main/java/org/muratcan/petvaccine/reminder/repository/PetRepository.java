package org.muratcan.petvaccine.reminder.repository;

import org.muratcan.petvaccine.reminder.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByUserId(String userId);

    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.vaccines")
    List<Pet> findAllWithVaccines();
}
