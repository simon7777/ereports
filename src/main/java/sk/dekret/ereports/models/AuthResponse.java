package sk.dekret.ereports.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import sk.dekret.ereports.db.entities.UserRole;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserRole role;
}
