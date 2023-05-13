package sk.dekret.ereports.exceptions;

import lombok.Getter;

@Getter
public class EReportsException extends Exception {

    private EReportsErrors error;

    public EReportsException(EReportsErrors error) {
        this.error = error;
    }

    @Getter
    public enum EReportsErrors {
        USERNAME_OR_PASSWORD_NOT_CORRECT("Username or password are not correct.");

        String value;

        EReportsErrors(String value) {
            this.value = value;
        }
    }
}
