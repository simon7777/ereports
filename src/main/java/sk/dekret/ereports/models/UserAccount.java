package sk.dekret.ereports.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import sk.dekret.ereports.db.entities.UserRole;

@Data
@Validated
public class UserAccount {
    private Long id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private UserRole role;

    private String password;
}
