export const showGeneralError = (toastReference, message) => {
    toastReference.current.show({
        severity: 'error',
        summary: 'Error',
        detail: message,
        sticky: true,
    });
};

export const showError400 = (toastReference, errorData, defaultMessage) => {
    if (errorData.code) {
        showGeneralError(toastReference, errorData.code);
    } else {
        showGeneralError(toastReference, defaultMessage);
    }
};

export const showError = (toastReference, error, message) => {
    if (error && error.response) {
        if (error.response.status === 403) {
            showGeneralError(toastReference, 'You do not have enough privileges for this action.');
        } else if (error.response.status === 400 && error.response.data) {
            showError400(toastReference, error.response.data, message);
        } else {
            showGeneralError(toastReference, message);
        }
    } else {
        showGeneralError(toastReference, message);
    }
};

export const showSuccess = (toastReference, message) => {
    toastReference.current.show({
        severity: 'success',
        summary: 'Success',
        detail: message,
    });
};
