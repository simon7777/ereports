import 'primeicons/primeicons.css';
import 'primereact/resources/themes/lara-light-indigo/theme.css';
import 'primereact/resources/primereact.css';
import 'primeflex/primeflex.css';
import '../../index.css';

import React, {useRef} from 'react';
import {useFormik} from 'formik';
import {InputText} from 'primereact/inputtext';
import {Button} from 'primereact/button';
import {Password} from 'primereact/password';
import {classNames} from 'primereact/utils';
import './loginPage.css';
import {login} from "../../services/AuthenticationService";
import * as AuthenticationUtil from '../../utils/AuthenticationUtil'
import {Toast} from 'primereact/toast';
import {showError} from "../../utils/ToastUtil";

export default function LoginPage() {
    const toast = useRef(null);

    const formik = useFormik({
        initialValues: {
            username: '',
            password: ''
        },
        validate: (data) => {
            let errors = {};

            if (!data.username) {
                errors.username = 'Username is required.';
            }

            if (!data.password) {
                errors.password = 'Password is required.';
            }

            return errors;
        },
        onSubmit: (data) => {
            login(data).then((response) => {
                if (AuthenticationUtil.login(response.data)) {
                    window.location.href = '/';
                    formik.resetForm();
                }
            }, (error) => {
                console.error(error);
                showError(toast, error, 'Unexpected error while logging in. Please try later.');
            });
        }
    });

    const isFormFieldValid = (name) => !!(formik.touched[name] && formik.errors[name]);
    const getFormErrorMessage = (name) => {
        return isFormFieldValid(name) && <small className="p-error">{formik.errors[name]}</small>;
    };

    return (
        <div className="form-login">
            <Toast ref={toast}/>
            <div className="flex justify-content-center">
                <div className="card">
                    <h1 className="text-center">Log in</h1>
                    <form onSubmit={formik.handleSubmit} className="p-fluid">
                        <div className="field">
                            <span className="p-float-label">
                                <InputText id="username" name="username" value={formik.values.username} onChange={formik.handleChange}
                                           autoFocus
                                           className={classNames({'p-invalid': isFormFieldValid('username')})}/>
                                <label htmlFor="username"
                                       className={classNames({'p-error': isFormFieldValid('username')})}>Username*</label>
                            </span>
                            {getFormErrorMessage('username')}
                        </div>
                        <div className="field">
                            <span className="p-float-label">
                                <Password id="password" name="password" value={formik.values.password} onChange={formik.handleChange}
                                          toggleMask
                                          className={classNames({'p-invalid': isFormFieldValid('password')})}/>
                                <label htmlFor="password"
                                       className={classNames({'p-error': isFormFieldValid('password')})}>Password*</label>
                            </span>
                            {getFormErrorMessage('password')}
                        </div>

                        <Button type="submit" label="Submit" className="mt-2"/>
                    </form>
                </div>
            </div>
        </div>
    );
}
