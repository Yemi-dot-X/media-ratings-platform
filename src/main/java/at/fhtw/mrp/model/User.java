package at.fhtw.mrp.model;

import lombok.*;

import java.time.LocalDate;

//annotation
@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor

public class User {
    private int id;
    private String username;
    private String name;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    //Konstruktor fuer die Registierung



}
