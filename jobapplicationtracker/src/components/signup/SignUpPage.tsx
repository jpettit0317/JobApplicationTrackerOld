import SignUpProps from "./SignUpPageProps";
import SignUpPageTestIds from "./SignUpPageTestIds_enum";
import signUpStyles from "../../styles/signuppagestyles";

import React, { useState } from "react";
import { withRouter } from "react-router";

import { Box, Grid, Container, TextField, makeStyles, Button, Typography, CssBaseline, Link} from "@material-ui/core"; 
import NavBar from "../navbar/NavBar";
import User from "../../models/User";
import SignUpErrors from "../../models/SignUpErrors";
import { ErrorSharp } from "@material-ui/icons";
import createUser from "../../functions/networkCalls/createUser";
import { Alert } from "@material-ui/lab";

const SignUpPage: React.FC<SignUpProps> = props => {
    const classes = signUpStyles();

    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [userNameError, setUserNameError] = useState("");
    const [emailError, setEmailError] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [confirmPasswordError, setConfirmPasswordError] = useState("");

    const renderUsernameField = (): JSX.Element => {
        if (userNameError === "") {
            return (
                <TextField
                    autoComplete="uname"
                    name="userName"
                    variant="outlined"
                    required
                    fullWidth
                    label="Username"
                    inputProps={{ "data-testid": SignUpPageTestIds.userName }}
                    onChange={(e) => setUsername(e.target.value)}
                />
            );
        } else {
            return (
                <TextField
                    autoComplete="uname"
                    name="userName"
                    variant="outlined"
                    required
                    fullWidth
                    label="Username"
                    inputProps={{ "data-testid": SignUpPageTestIds.userName }}
                    onChange={(e) => setUsername(e.target.value)}
                    error
                    helperText={userNameError}
                />
            );
        }
    }

    const renderEmailField = (): JSX.Element => {
        if (emailError === "") {
            return (
                <TextField
                    autoComplete="email"
                    name="email"
                    variant="outlined"
                    required
                    fullWidth
                    label="Email Address"
                    inputProps={{ "data-testid": SignUpPageTestIds.email }}
                    onChange={(e) => setEmail(e.target.value)}
                />
            );
        } else {
            return (
                <TextField
                    autoComplete="email"
                    name="email"
                    variant="outlined"
                    required
                    fullWidth
                    label="Email Address"
                    inputProps={{ "data-testid": SignUpPageTestIds.email }}
                    onChange={(e) => setEmail(e.target.value)}
                    error
                    helperText={emailError}
                />
            );
        }
    }

    const renderPasswordField = (): JSX.Element => {
        if (passwordError === "") {
            return (
                <TextField
                    autoComplete="password"
                    name="password"
                    variant="outlined"
                    required
                    fullWidth
                    label="Password"
                    inputProps={{ "data-testid": SignUpPageTestIds.password }}
                    type="password"
                    onChange={(e) => setPassword(e.target.value)}
                />
            );
        } else {
            return (
                <TextField
                    autoComplete="password"
                    name="password"
                    variant="outlined"
                    required
                    fullWidth
                    label="Password"
                    inputProps={{ "data-testid": SignUpPageTestIds.password }}
                    type="password"
                    onChange={(e) => setPassword(e.target.value)}
                    error
                    helperText={passwordError}
                />
            );
        }
    }

    const renderConfirmPasswordField = (): JSX.Element => {
        if (confirmPasswordError === "") {
            return (
                <TextField
                    autoComplete="password"
                    name="confirmPassword"
                    variant="outlined"
                    required
                    fullWidth
                    label="Confirm Password"
                    inputProps={{ "data-testid": SignUpPageTestIds.confirmPassword }}
                    type="password"
                    onChange={(e) => setConfirmPassword(e.target.value)}
                />
            );
        } else {
            return (
                <TextField
                    autoComplete="password"
                    name="confirmPassword"
                    variant="outlined"
                    required
                    fullWidth
                    label="Confirm Password"
                    inputProps={{ "data-testid": SignUpPageTestIds.confirmPassword }}
                    type="password"
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    error
                    helperText={confirmPasswordError}
                />
            )
        }   
    }

    const onSubmitButtonPressed = async () => {
        const user = new User(email, username, password);
        const userAsString = JSON.stringify(user);

        const errors = user.validateUser(confirmPassword);

        setErrors(errors);
        
        if (areThereErrors()) {
            return;
        }

        const result = await createUser(user);

        if (result !== "") {
            <Alert onClose={() => { }}>{result}</Alert>
        }
    };

    const setErrors = (errors: SignUpErrors) => {
        setUserNameError(errors.usernameError);
        setPasswordError(errors.passwordError);
        setEmailError(errors.emailError);
        setConfirmPasswordError(errors.confirmPasswordError);
    }

    const areThereErrors = (): boolean => {
        if (userNameError === "" && 
            passwordError === "" 
            && emailError === "" && confirmPasswordError === "") {
            return false;
        } else {
            return true;
        }
    }

    return ( 
        <div>
            <NavBar navBarTitle="JobApplicationTracker" />
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <div className={classes.paper}>
                    <Typography component="h1" variant="h5">
                        Sign up
                    </Typography>
                    <form className={classes.form} noValidate>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                               {renderUsernameField()} 
                            </Grid>
                            <Grid item xs={12}>
                                {renderEmailField()}
                            </Grid>
                            <Grid item xs={12}>
                                {renderPasswordField()}
                            </Grid>
                            <Grid item xs={12}>
                                {renderConfirmPasswordField()}
                            </Grid>
                            <Grid item xs={12}> 
                                <Button
                                    type="button"
                                    fullWidth
                                    variant="contained"
                                    color="primary"
                                    className={classes.submit}
                                    onClick={onSubmitButtonPressed}
                                > 
                                Sign Up 
                                </Button>
                            </Grid>
                        </Grid>
                        <Grid container justifyContent="flex-end">
                            <Grid item>
                                <Link href="#" variant="body2">
                                    Already have an account? Sign in
                                </Link>
                            </Grid>
                        </Grid>
                    </form>
                </div>
            </Container>
        </div>
    );
};

export default withRouter(SignUpPage);