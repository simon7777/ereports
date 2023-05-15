import React, {useCallback} from 'react';
import {Menubar} from 'primereact/menubar';
import {Button} from 'primereact/button';

import './header.css';
import {getRole} from "~/utils/JwtTokenUtil";
import {isAuthenticated, logout} from "~/utils/AuthenticationUtil";
import {useHistory} from "react-router-dom";

export default function Header() {
    const history = useHistory();
    const items = isAuthenticated() ? [
        {
            label: 'Home',
            icon: 'pi pi-home',
            command: () => {
                history.push('/');
            },
        },
        {
            label: 'Employers',
            icon: 'pi pi-users',
            command: () => {
                history.push('/employers');
            },
            hasRole: 'ROLE_MANAGER'
        },
        {
            label: 'Reports',
            icon: 'pi pi-list',
            to: '/reports',
            command: () => {
                history.push('/reports');
            },
        }
    ] : [];

    const onLogout = useCallback(() => {
        logout();
    }, []);

    const start = <div className="logo"><h2>e R e p o r t s</h2></div>;
    const end = <Button label="Log out" icon="pi pi-user" className="p-button-tertiary p-ml-auto" onClick={onLogout}/>;

    return (
        <div className="header">
            <Menubar model={items.filter(i => !i.hasRole || i.hasRole === getRole())} start={start} end={isAuthenticated() ? end : null}
                     className="p-d-flex p-justify-between"/>
        </div>
    );
}
;
