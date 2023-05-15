import React, {useCallback, useEffect, useRef, useState} from 'react';
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Toast} from 'primereact/toast';
import {Toolbar} from 'primereact/toolbar';
import './employersPage.css';
import * as EmployerService from "~/services/EmployerService";
import {showError, showSuccess} from "~/utils/ToastUtil";
import {Button} from "primereact/button";
import {Dialog} from "primereact/dialog";
import {InputText} from "primereact/inputtext";
import {classNames} from 'primereact/utils';
import {RadioButton} from "primereact/radiobutton";
import {Password} from "primereact/password";
import {confirmDialog, ConfirmDialog} from 'primereact/confirmdialog';

export default function EmployersPage() {
    const [users, setUsers] = useState([]);
    const [user, setUser] = useState({});
    const toast = useRef(null);
    const [loading, setLoading] = useState(false);
    const [loadingDialog, setLoadingDialog] = useState(false);
    const [userDialog, setUserDialog] = useState(false);
    const [submitted, setSubmitted] = useState(false);
    const dt = useRef(null);

    useEffect(() => {
        setLoading(true);
        EmployerService.findAll().then((response) => {
            if (response.data) {
                setUsers(response.data);
            } else {
                setUsers([]);
            }

            setLoading(false);
        }, (error) => {
            setLoading(false);
            showError(toast, error, 'Unexpected error while loading employers data.');
        });
    }, []);

    const saveUser = () => {
        setSubmitted(true);

        if (user.username && user.firstName && user.lastName && user.role) {
            if (user.id) { //edit
                setLoadingDialog(true);
                EmployerService.updateUser(user.id, user).then((response) => {
                    if (response.data) {
                        const updatedUsers = [...users];

                        const filteredUsers = updatedUsers.map((u) => u.id === response.data.id ? response.data : u);

                        setUsers(filteredUsers);

                        showSuccess(toast, 'User was successfully updated.');
                    }
                    setLoadingDialog(false);
                    setUserDialog(false);
                }).catch((error) => {
                    setLoadingDialog(false);
                    showError(toast, error, 'It was not possible to create the user. Please try again later.');
                });
            } else if (user.password) { //password needs to be set when creating new user account
                setLoadingDialog(true);
                EmployerService.createUser(user).then((response) => {
                    if (response.data) {
                        const updatedUsers = [...users];
                        updatedUsers.push(response.data);
                        setUsers(updatedUsers);

                        showSuccess(toast, 'User was successfully created.');
                    }
                    setLoadingDialog(false);
                    setUserDialog(false);
                }).catch((error) => {
                    setLoadingDialog(false);
                    showError(toast, error, 'It was not possible to create the user. Please try again later.');
                });
            }
        }
    }

    const deleteUserAccount = (rowData) => {
        setLoading(true);

        EmployerService.deleteUser(rowData.id).then((response) => {
            const allUsers = [...users];

            const usersWithoutDeletedUser = allUsers.filter(u => u.id !== rowData.id);

            setUsers(usersWithoutDeletedUser);

            setLoading(false);
            showSuccess(toast, 'User account was successfully deleted.');
        }).catch((error) => {
            setLoading(false);
            showError(toast, error, 'User account was not deleted. Please try later.');
        });
    }

    const openNew = () => {
        setUser({});
        setSubmitted(false);
        setUserDialog(true);
    }

    const editUser = (user) => {
        setUser({...user});
        setUserDialog(true);
    }

    const hideDialog = () => {
        setSubmitted(false);
        setUserDialog(false);
    }

    const showConfirm = useCallback((rowData) => {
        confirmDialog({
            header: 'Delete user account?',
            message: 'Do you really want to delete ' + rowData.firstName + ' ' + rowData.lastName + ' from your list of employers?',
            icon: 'pi pi-info-circle',
            acceptClassName: 'p-button-danger',
            rejectClassName: 'p-button-secondary',
            accept: () => deleteUserAccount(rowData),
        });
    }, [users]);

    const rightToolbarTemplate = () => {
        return (
            <React.Fragment>
                <Button label="New employer" icon="pi pi-plus" className="p-button-success mr-2" onClick={openNew}/>
            </React.Fragment>
        )
    }

    const header = (
        <div className="table-header">
            <h5 className="mx-0 my-1">Manage Employers</h5>
        </div>
    );

    const userDialogFooter = (
        <React.Fragment>
            <Button label="Cancel" icon="pi pi-times" className="p-button-text" onClick={hideDialog}/>
            <Button label="Save" icon="pi pi-check" className="p-button-text" onClick={saveUser} loading={loadingDialog}/>
        </React.Fragment>
    );

    const onCategoryChange = (e) => {
        let _user = {...user};
        _user['role'] = e.value;
        setUser(_user);
    }

    const onInputChange = (e, name) => {
        const val = (e.target && e.target.value) || '';
        let _user = {...user};
        _user[`${name}`] = val;

        setUser(_user);
    }

    const actionBodyTemplate = (rowData) => {
        return (
            <React.Fragment>
                <Button icon="pi pi-pencil" className="p-button-rounded p-button-success mr-2" onClick={() => editUser(rowData)}/>
                <Button icon="pi pi-trash" className="p-button-rounded p-button-warning" onClick={() => showConfirm(rowData)}/>
            </React.Fragment>
        );
    }

    const roleTemplate = (rowData) => {
        return rowData.role.substr(5);
    }

    return <div className="employers">
        <Toast ref={toast}/>
        <ConfirmDialog/>

        <div className="datatable-employers">
            <Toolbar className="mb-4" right={rightToolbarTemplate}></Toolbar>
            <DataTable ref={dt} value={users} loading={loading}
                       dataKey="id" paginator rows={10} rowsPerPageOptions={[5, 10, 25]}
                       paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                       currentPageReportTemplate="Showing {first} to {last} of {totalRecords} employers" header={header}
                       responsiveLayout="scroll">
                <Column field="username" header="Username" sortable style={{minWidth: '12rem'}}></Column>
                <Column field="firstName" header="First name" sortable style={{minWidth: '16rem'}}></Column>
                <Column field="lastName" header="Last name" sortable style={{minWidth: '16rem'}}></Column>
                <Column field="role" header="Role" body={roleTemplate} style={{minWidth: '16rem'}}></Column>
                <Column body={actionBodyTemplate} exportable={false} style={{minWidth: '8rem'}}></Column>
            </DataTable>
        </div>

        <Dialog visible={userDialog} style={{width: '450px'}} header="Employer Details" modal className="p-fluid"
                footer={userDialogFooter} onHide={hideDialog}>
            <div className="formgrid grid">
                <div className="field col">
                    <label htmlFor="firstName">First name:</label>
                    <InputText id="firstName" value={user.firstName} onChange={(e) => onInputChange(e, 'firstName')} required autoFocus
                               className={classNames({'p-invalid': submitted && !user.firstName})}/>
                    {submitted && !user.firstName && <small className="p-error">First name is required.</small>}
                </div>
                <div className="field col">
                    <label htmlFor="lastName">Last name:</label>
                    <InputText id="lastName" value={user.lastName} onChange={(e) => onInputChange(e, 'lastName')} required autoFocus
                               className={classNames({'p-invalid': submitted && !user.lastName})}/>
                    {submitted && !user.lastName && <small className="p-error">Last name is required.</small>}
                </div>
            </div>
            <div className="field">
                <label className="mb-3">Role:</label>
                <div className="formgrid grid">
                    <div className="field-radiobutton col-6">
                        <RadioButton inputId="role1" name="role" value="ROLE_MANAGER" onChange={onCategoryChange}
                                     checked={user.role === 'ROLE_MANAGER'}/>
                        <label htmlFor="category1">Manager</label>
                    </div>
                    <div className="field-radiobutton col-6">
                        <RadioButton inputId="role2" name="role" value="ROLE_USER" onChange={onCategoryChange}
                                     checked={user.role === 'ROLE_USER'}/>
                        <label htmlFor="category2">User</label>
                    </div>
                </div>
                {submitted && !user.role && <small className="p-error">Role is required.</small>}
            </div>
            <div className="field">
                <label htmlFor="username">Username:</label>
                <InputText id="username" value={user.username} onChange={(e) => onInputChange(e, 'username')} required autoFocus
                           className={classNames({'p-invalid': submitted && !user.username})}/>
                {submitted && !user.username && <small className="p-error">Username is required.</small>}
            </div>
            <div className="field">
                <label htmlFor="password">Password:</label>
                <Password id="password" value={user.password} onChange={(e) => onInputChange(e, 'password')} required autoFocus
                          toggleMask disabled={user.id}
                          className={classNames({'p-invalid': submitted && !user.password})}/>
                {!user.id && submitted && !user.password && <small className="p-error">Password is required.</small>}
            </div>
        </Dialog>
    </div>
}
