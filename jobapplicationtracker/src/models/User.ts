import UserSignUpErrors from "../enums/UserSignUpErrors_enum";
import SignUpErrors from "./SignUpErrors";

class User {
    readonly email: string;
    readonly username: string;
    readonly password: string;
    
    constructor(email: string = "", username: string = "", password: string = "") {
           this.email = email;
           this.username = username;
           this.password = password;
    }

    validateUser(confirmedPassword: string = ""): SignUpErrors {
        let userNameError: string = "";
        let passwordError: string = "";
        let emailError: string = "";
        let confirmPasswordError: string = "";

        if (this.username === "") {
            userNameError = UserSignUpErrors.userNameEmpty;
        }

        if (this.password === "") {
            passwordError = UserSignUpErrors.passwordEmpty;
        }

        if (this.email === "") {
            emailError = UserSignUpErrors.emailEmpty;
        }

        if (confirmedPassword === "") {
            confirmPasswordError = UserSignUpErrors.confirmedPasswordEmpty;
        }

        if (this.password !== "" && confirmedPassword !== "" && this.password !== confirmedPassword) {
            confirmPasswordError = UserSignUpErrors.passwordsDontMatch;
            passwordError = UserSignUpErrors.passwordsDontMatch;
        }

        return new SignUpErrors(emailError, userNameError, passwordError, confirmPasswordError);
    } 
}

export default User;