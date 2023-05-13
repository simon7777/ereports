package sk.dekret.ereports.exceptions;

import lombok.Getter;

@Getter
public class EReportsException extends Exception {

    private final EReportsErrors error;

    public EReportsException(EReportsErrors error) {
        this.error = error;
    }

    @Getter
    public enum EReportsErrors {
        USERNAME_OR_PASSWORD_NOT_CORRECT("Username or password are not correct."),
        USER_WITH_NAME_ALREADY_EXISTS("User with this username already exists."),
        USER_WITH_ID_DOES_NOT_EXIST("User with this ID does not exist.");

        String value;

        EReportsErrors(String value) {
            this.value = value;
        }
    }
}
