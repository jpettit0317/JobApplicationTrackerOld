
class SignUpErrors {
    readonly emailError: string = "";
    readonly usernameError: string = "";
    readonly passwordError: string = "";
    readonly confirmPasswordError: string = "";

    constructor(emailError: string = "", usernameError: string = "", passwordError: string = "", confirmPasswordError: string = "") {
        this.confirmPasswordError = confirmPasswordError;
        this.usernameError = usernameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
    }
}

export default SignUpErrors;