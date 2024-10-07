package org.muratcan.petvaccine.reminder.repository;

import org.muratcan.petvaccine.reminder.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
}

