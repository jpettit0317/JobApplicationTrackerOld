import LoginPageErrorPair from "./LoginPageErrorPair";

class Login {
    readonly username: string;
    readonly password: string;

    constructor(username: string = "", password: string = "") {
        this.username = username;
        this.password = password;
    }

    validateLogin = (): LoginPageErrorPair => {
        let usernameError = "";
        let passwordError = "";

        if (this.username === "") {
            usernameError = "Please provide a username";
        }

        if (this.password === "") {
            passwordError = "Please provide a password";
        }

        return {
            usernameError: usernameError,
            passwordError: passwordError
        };
    }
}

export default Login;