package org.muratcan.petvaccine.reminder.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.entity.Pet;
import org.muratcan.petvaccine.reminder.entity.User;
import org.muratcan.petvaccine.reminder.entity.Vaccine;
import org.muratcan.petvaccine.reminder.model.VaccineReminder;
import org.muratcan.petvaccine.reminder.producer.VaccineReminderProducer;
import org.muratcan.petvaccine.reminder.service.PetService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VaccineReminderScheduler {

    private final PetService petService;
    private final VaccineReminderProducer vaccineReminderProducer;

    @Scheduled(cron = "0 0 10 * * *")
    public void scheduleVaccineReminders() throws JsonProcessingException {
        List<Pet> pets = petService.getAllPets();
        for (Pet pet : pets) {
            for (Vaccine vaccine : pet.getVaccines()) {
                if (isDue(vaccine)) {
                    VaccineReminder vaccineReminder = new VaccineReminder();
                    String message = generateReminderMessage(pet, vaccine);
                    User user = pet.getUser();
                    vaccineReminder.setMessage(message);
                    vaccineReminder.setChatId(user.getChatId());
                    vaccineReminderProducer.sendVaccineReminder(vaccineReminder);
                }
            }
        }
    }

    private boolean isDue(Vaccine vaccine) {
        LocalDate today = LocalDate.now();
        LocalDate dueDate = vaccine.getDueDate();

        if (vaccine.getReminderFrequencyInMonths() == 0) {
            return today.isEqual(dueDate);
        } else {
            LocalDate nextDueDate = dueDate;
            while (nextDueDate.isBefore(today)) {
                nextDueDate = nextDueDate.plusMonths(vaccine.getReminderFrequencyInMonths());
            }
            return today.isEqual(nextDueDate);
        }
    }

    private String generateReminderMessage(Pet pet, Vaccine vaccine) {
        return "Reminder: Your pet " + pet.getName() + " is due for the vaccine: " + vaccine.getName();
    }
}
