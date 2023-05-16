package sk.dekret.ereports.security;

import jakarta.annotation.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UserContext extends AbstractAuthenticationToken {

    private final String username;
    private final Long userAccountId;


    public UserContext(String username, Long userAccountId, @Nullable java.util.Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.username = username;
        this.userAccountId = userAccountId;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.username;
    }

    public Long getUserAccountId() {
        return this.userAccountId;
    }
}
