package sk.dekret.ereports.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.dekret.ereports.exceptions.EReportsException;
import sk.dekret.ereports.models.AuthRequest;
import sk.dekret.ereports.models.AuthResponse;
import sk.dekret.ereports.services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Log a user in based on username and password.
     *
     * @param authRequest {@link AuthRequest} containing username and password
     * @return {@link AuthResponse} containing role and jwt token
     * @throws EReportsException if username or password is not correct
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) throws EReportsException {
        return ResponseEntity.ok(authenticationService.login(authRequest));
    }
}
