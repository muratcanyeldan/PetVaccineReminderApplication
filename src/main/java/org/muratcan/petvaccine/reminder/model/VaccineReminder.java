package org.muratcan.petvaccine.reminder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaccineReminder {
    private String chatId;
    private String message;
    private String userId;
}
