package at.fhtw.mrp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//annotation
@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor

public class User {
    private int id;
    private String username;
    private String email;
    private String password;


}
