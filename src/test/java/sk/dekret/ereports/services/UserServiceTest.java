package sk.dekret.ereports.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sk.dekret.ereports.EReportsApplication;
import sk.dekret.ereports.db.entities.UserAccount;
import sk.dekret.ereports.db.entities.UserRole;
import sk.dekret.ereports.exceptions.EReportsException;
import sk.dekret.ereports.repositories.UserAccountRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(classes = {EReportsApplication.class})
@AutoConfigureMockMvc
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @AfterEach
    void tearDown() {
        userAccountRepository.deleteAll();
    }

    @Test
    void findAllUsers() {
        UserAccount userAccount = new UserAccount();
        userAccount.setFirstName("firstName");
        userAccount.setLastName("lastName");
        userAccount.setUsername("username");
        userAccount.setPassword("pass");
        userAccount.setRole(UserRole.ROLE_MANAGER);

        userAccountRepository.save(userAccount);

        List<sk.dekret.ereports.models.UserAccount> accounts = userService.findAll();

        assertThat(accounts).hasSize(1);
        sk.dekret.ereports.models.UserAccount account = accounts.get(0);

        assertThat(account.getRole()).isEqualTo(userAccount.getRole());
        assertThat(account.getFirstName()).isEqualTo(userAccount.getFirstName());
        assertThat(account.getLastName()).isEqualTo(userAccount.getLastName());
        assertThat(account.getUsername()).isEqualTo(userAccount.getUsername());
    }

    @Test
    void findUserById() {
        UserAccount userAccount = new UserAccount();
        userAccount.setFirstName("firstName");
        userAccount.setLastName("lastName");
        userAccount.setUsername("username");
        userAccount.setPassword("pass");
        userAccount.setRole(UserRole.ROLE_MANAGER);

        userAccount = userAccountRepository.save(userAccount);

        assertThat(userAccount).isNotNull();

        sk.dekret.ereports.models.UserAccount response = userService.findById(userAccount.getId());

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(userAccount.getId());
    }

    @Test
    void createUserAccount() throws EReportsException {
        sk.dekret.ereports.models.UserAccount userAccount = new sk.dekret.ereports.models.UserAccount();
        userAccount.setFirstName("firstName");
        userAccount.setLastName("lastName");
        userAccount.setUsername("username");
        userAccount.setPassword("pass");
        userAccount.setRole(UserRole.ROLE_MANAGER);

        sk.dekret.ereports.models.UserAccount response = userService.createUser(userAccount);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(userAccount.getUsername());
        assertThat(response.getLastName()).isEqualTo(userAccount.getLastName());
        assertThat(response.getFirstName()).isEqualTo(userAccount.getFirstName());
        assertThat(response.getRole()).isEqualTo(userAccount.getRole());
    }

    @Test
    void createUserWithExistingUsername() {
        UserAccount userAccount = new UserAccount();
        userAccount.setFirstName("firstName");
        userAccount.setLastName("lastName");
        userAccount.setUsername("username");
        userAccount.setPassword("pass");
        userAccount.setRole(UserRole.ROLE_MANAGER);

        this.userAccountRepository.save(userAccount);

        sk.dekret.ereports.models.UserAccount request = new sk.dekret.ereports.models.UserAccount();
        request.setUsername("username");

        assertThrows(EReportsException.class, () -> this.userService.createUser(request));
    }

    @Test
    void updateUser() throws EReportsException {
        sk.dekret.ereports.db.entities.UserAccount entity = new sk.dekret.ereports.db.entities.UserAccount();
        entity.setPassword("pass");
        entity.setRole(UserRole.ROLE_MANAGER);
        entity.setUsername("username");
        entity.setLastName("lastName");
        entity.setFirstName("firstName");

        entity = this.userAccountRepository.save(entity);

        sk.dekret.ereports.models.UserAccount model = new sk.dekret.ereports.models.UserAccount();
        model.setId(entity.getId());
        model.setUsername("username2");
        model.setFirstName("firstname2");
        model.setLastName("lastname2");
        model.setRole(UserRole.ROLE_USER);

        sk.dekret.ereports.models.UserAccount response = this.userService.updateUser(entity.getId(), model);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(entity.getId());
        assertThat(response.getUsername()).isEqualTo(model.getUsername());
        assertThat(response.getFirstName()).isEqualTo(model.getFirstName());
        assertThat(response.getLastName()).isEqualTo(model.getLastName());
        assertThat(response.getRole()).isEqualTo(model.getRole());
    }

    @Test
    void updateUserThatDoesNotExist() {
        sk.dekret.ereports.models.UserAccount model = new sk.dekret.ereports.models.UserAccount();
        model.setId(100L);
        model.setUsername("username2");
        model.setFirstName("firstname2");
        model.setLastName("lastname2");
        model.setRole(UserRole.ROLE_USER);

        assertThrows(EReportsException.class, () -> userService.updateUser(100L, model));
    }

    @Test
    void deleteUser() throws EReportsException {
        sk.dekret.ereports.db.entities.UserAccount entity = new sk.dekret.ereports.db.entities.UserAccount();
        entity.setPassword("pass");
        entity.setRole(UserRole.ROLE_MANAGER);
        entity.setUsername("username");
        entity.setLastName("lastName");
        entity.setFirstName("firstName");

        entity = this.userAccountRepository.save(entity);

        Boolean response = this.userService.deleteUser(entity.getId());

        assertThat(response).isEqualTo(Boolean.TRUE);
    }

    @Test
    void deleteUserThatDoesNotExist() {
        assertThrows(EReportsException.class, () -> userService.deleteUser(100L));
    }

}
