import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import {BrowserRouter as Router, Redirect, Route} from 'react-router-dom';
import LoginPage from "./components/login/LoginPage";
import {isAuthenticated} from "./utils/AuthenticationUtil";
import 'primereact/resources/themes/md-light-indigo/theme.css';
import 'primeflex/primeflex.css'; // Import PrimeFlex CSS

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
        <Route exact path="/login" component={LoginPage}/>
        <PrivateRoute path="/" component={App}/>
    </Router>
);

