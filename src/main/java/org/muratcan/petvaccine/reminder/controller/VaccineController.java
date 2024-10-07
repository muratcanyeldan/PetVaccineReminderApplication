package org.muratcan.petvaccine.reminder.controller;

import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.entity.Pet;
import org.muratcan.petvaccine.reminder.entity.Vaccine;
import org.muratcan.petvaccine.reminder.service.PetService;
import org.muratcan.petvaccine.reminder.service.VaccineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pets/{petId}/vaccines")
public class VaccineController {

    private final VaccineService vaccineService;
    private final PetService petService;

    @GetMapping
    public ResponseEntity<List<Vaccine>> getVaccines(@PathVariable Long petId) {
        Optional<Pet> pet = petService.findById(petId);
        return pet.map(value -> ResponseEntity.ok(value.getVaccines())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vaccine> addVaccine(@PathVariable Long petId, @RequestBody Vaccine vaccine) {
        Optional<Pet> pet = petService.findById(petId);
        if (pet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Vaccine savedVaccine = vaccineService.addVaccine(pet.get(), vaccine);
        return ResponseEntity.ok(savedVaccine);
    }

    @PutMapping("/{vaccineId}")
    public ResponseEntity<Vaccine> updateVaccine(@PathVariable Long petId, @PathVariable Long vaccineId, @RequestBody Vaccine vaccineDetails) {
        Optional<Vaccine> vaccine = vaccineService.findById(vaccineId);

        if (vaccine.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Pet> pet = petService.findById(petId);
        if (pet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!pet.get().getVaccines().contains(vaccine.get())) {
            return ResponseEntity.badRequest().build();
        }

        Vaccine updatedVaccine = vaccineService.updateVaccine(vaccine.get(), vaccineDetails);
        return ResponseEntity.ok(updatedVaccine);
    }

    @DeleteMapping("/{vaccineId}")
    public ResponseEntity<Void> deleteVaccine(@PathVariable Long petId, @PathVariable Long vaccineId) {
        Optional<Vaccine> vaccine = vaccineService.findById(vaccineId);
        if (vaccine.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Pet> pet = petService.findById(petId);
        if (pet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!pet.get().getVaccines().contains(vaccine.get())) {
            return ResponseEntity.badRequest().build();
        }

        vaccineService.deleteVaccine(vaccine.get());
        return ResponseEntity.noContent().build();
    }
}
