package sk.dekret.ereports.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import sk.dekret.ereports.security.UserContext;

import java.util.Optional;

@UtilityClass
public class SecurityUtils {

    public static Optional<Long> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UserContext userContext) {
            return Optional.of(userContext.getUserAccountId());
        }

        return Optional.empty();
    }
}
