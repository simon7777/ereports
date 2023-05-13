package sk.dekret.ereports.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import sk.dekret.ereports.db.entities.UserAccount;
import sk.dekret.ereports.exceptions.EReportsException;
import sk.dekret.ereports.models.AuthRequest;
import sk.dekret.ereports.models.AuthResponse;
import sk.dekret.ereports.repositories.UserAccountRepository;
import sk.dekret.ereports.security.JwtTokenUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserAccountRepository userAccountRepository;

    public AuthResponse login(AuthRequest request) throws EReportsException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));

            UserAccount user = loadUserAccount((String) authentication.getPrincipal());
            String accessToken = jwtTokenUtil.generateAccessToken(user);

            return new AuthResponse(accessToken, user.getRole());
        } catch (BadCredentialsException ex) {
            throw new EReportsException(EReportsException.EReportsErrors.USERNAME_OR_PASSWORD_NOT_CORRECT);
        }
    }

    public UserAccount loadUserAccount(String username) throws EReportsException {
        return this.userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new EReportsException(EReportsException.EReportsErrors.USERNAME_OR_PASSWORD_NOT_CORRECT));
    }
}
