package at.fhtw.mrp.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
}