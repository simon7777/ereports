package sk.dekret.ereports.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import sk.dekret.ereports.db.entities.UserRole;

@Data
@Validated
public class UserAccount {
    private Long id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotNull
    private UserRole role;

    private String password;

    public static class Builder {

        private String username;
        private String firstName;
        private String lastName;
        private String password;
        private UserRole role;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withRole(UserRole role) {
            this.role = role;
            return this;
        }

        public UserAccount build() {
            UserAccount model = new UserAccount();

            model.setRole(role);
            model.setUsername(username);
            model.setFirstName(firstName);
            model.setLastName(lastName);
            model.setPassword(password);

            return model;
        }
    }
}
