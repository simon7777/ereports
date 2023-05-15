import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {BrowserRouter as Router, Redirect, Route} from 'react-router-dom';
import LoginPage from "./components/login/LoginPage";
import {isAuthenticated} from "./utils/AuthenticationUtil";
import 'primereact/resources/themes/md-light-indigo/theme.css';
import 'primeflex/primeflex.css';
import EmployersPage from "~/components/employers/EmployersPage";
import Layout from "~/components/layout/Layout";
import ReportsPage from "~/components/reports/ReportsPage"; // Import PrimeFlex CSS

const PrivateRoute = ({component: Component, ...rest}) => (
    <Route
        {...rest}
        render={props =>
            isAuthenticated() ? (
                <Component {...props} />
            ) : (
                <Redirect to="/login"/>
            )
        }
    />
);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <Router>
        <Layout>
            <Route exact path="/login" component={LoginPage}/>
            <PrivateRoute exact path="/reports" component={ReportsPage}/>
            <PrivateRoute exact path="/employers" component={EmployersPage}/>
            <PrivateRoute exact path="/" component={ReportsPage}/>
        </Layout>
    </Router>
);

