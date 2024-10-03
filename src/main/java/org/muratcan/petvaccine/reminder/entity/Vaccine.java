package org.muratcan.petvaccine.reminder.entity;

import jakarta.persistence.Column;
import org.muratcan.petvaccine.reminder.enums.AnimalType;

public class Vaccine extends BaseEntity {

    @Column(name = "vaccine_name")
    private String vaccineName;

    @Column(name = "vaccine_description")
    private String vaccineDescription;

    @Column(name = "vaccine_animal_type")
    private AnimalType vaccineAnimalType;
}
