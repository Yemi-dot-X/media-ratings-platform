package at.fhtw.mrp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
//fuer die Daten die der User ändern kann
@Getter
@Setter
public class UpdateProfileRequest {
    private String name;
    private LocalDate birthDate;
    private String email;
}
