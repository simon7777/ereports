package sk.dekret.ereports.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class AuthRequest {
    @NotEmpty
    private String username;
    
    @NotEmpty
    private String password;
}
