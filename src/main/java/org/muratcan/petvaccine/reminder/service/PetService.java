package org.muratcan.petvaccine.reminder.service;

import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.entity.Pet;
import org.muratcan.petvaccine.reminder.entity.User;
import org.muratcan.petvaccine.reminder.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserService userService;

    public List<Pet> getPetsByUser(String userId) {
        return petRepository.findByUserId(userId);
    }

    public Pet savePet(Pet pet, String userId) {
        User user = userService.findById(userId);
        pet.setUser(user);
        return petRepository.save(pet);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAllWithVaccines();
    }

    public Optional<Pet> findById(Long petId) {
        return petRepository.findById(petId);
    }
}

