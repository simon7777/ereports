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
        this.buildUserAccountAndSave("simon", "Simon", "Dekret", "simon", UserRole.ROLE_MANAGER);
        this.buildUserAccountAndSave("peter", "Peter", "Petrovsky", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("filip", "Filip", "Filipovsky", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("ondrej", "Ondrej", "Ondrejovsky", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("lubica", "Lubica", "Lubicova", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("jakub", "Jakub", "Jakubisko", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("tomas", "Tomas", "Tomasovy", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("martin", "Martin", "Martinovsky", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("juraj", "Juraj", "Jurovsky", "simon", UserRole.ROLE_USER);
        this.buildUserAccountAndSave("matej", "Matej", "Matejovsky", "simon", UserRole.ROLE_USER);
    }

    private void buildUserAccountAndSave(String username, String firstName, String lastName, String password, UserRole role) throws EReportsException {
        UserAccount userAccount = new UserAccount.Builder()
                .withUsername(username)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withPassword(password)
                .withRole(role)
                .build();

        saveUserAccount(userAccount);
    }

    private void saveUserAccount(UserAccount model) throws EReportsException {
        this.userService.createUser(model);
    }
}
