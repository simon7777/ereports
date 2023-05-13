package sk.dekret.ereports.models;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class AuthRequest {
    private String username;
    private String password;
}
