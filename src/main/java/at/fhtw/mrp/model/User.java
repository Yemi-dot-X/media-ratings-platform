package at.fhtw.mrp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

//annotation
@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class User {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private String username;
    private String name;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private LocalDate dateOfBirth;
    //Konstruktor fuer die Registierung



}
