package org.muratcan.petvaccine.reminder.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.entity.Pet;
import org.muratcan.petvaccine.reminder.service.PetService;
import org.muratcan.petvaccine.reminder.util.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public List<Pet> getPetsByUser(HttpServletRequest request) {
        String userId = jwtUtil.getUserId(request);
        return petService.getPetsByUser(userId);
    }

    @PostMapping
    public Pet addPet(HttpServletRequest request, @RequestBody Pet pet) {
        String userId = jwtUtil.getUserId(request);
        return petService.savePet(pet, userId);
    }
}
