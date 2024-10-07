package org.muratcan.petvaccine.reminder.service;

import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.entity.Pet;
import org.muratcan.petvaccine.reminder.entity.Vaccine;
import org.muratcan.petvaccine.reminder.repository.VaccineRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VaccineService {

    private final VaccineRepository vaccineRepository;

    public Optional<Vaccine> findById(Long id) {
        return vaccineRepository.findById(id);
    }

    public Vaccine addVaccine(Pet pet, Vaccine vaccine) {
        pet.getVaccines().add(vaccine);
        return vaccineRepository.save(vaccine);
    }

    public Vaccine updateVaccine(Vaccine existingVaccine, Vaccine vaccineDetails) {
        existingVaccine.setName(vaccineDetails.getName());
        existingVaccine.setDueDate(vaccineDetails.getDueDate());
        existingVaccine.setReminderFrequencyInMonths(vaccineDetails.getReminderFrequencyInMonths());
        return vaccineRepository.save(existingVaccine);
    }

    public void deleteVaccine(Vaccine vaccine) {
        vaccineRepository.delete(vaccine);
    }
}


