package sk.dekret.ereports.mocks;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.dekret.ereports.db.entities.UserRole;
import sk.dekret.ereports.exceptions.EReportsException;
import sk.dekret.ereports.models.UserAccount;
import sk.dekret.ereports.services.UserService;

@Service
@RequiredArgsConstructor
public class MockService {

    private final UserService userService;

    public void mockUserAccounts() throws EReportsException {
        UserAccount model = new UserAccount();

        model.setRole(UserRole.ROLE_MANAGER);
        model.setUsername("simon");
        model.setFirstName("Simon");
        model.setLastName("Dekret");
        model.setPassword("simon");

        this.userService.createUser(model);
    }
}
