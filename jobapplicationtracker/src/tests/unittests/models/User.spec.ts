import User from "../../../models/User";
import { expect } from "chai";
import SignUpErrors from "../../../models/SignUpErrors";
import UserSignUpErrors from "../../../enums/UserSignUpErrors_enum";

describe("User tests", () => {
    describe("doPasswordsMatch", () => {
        it('when passing in the same passwords, doPasswordsMatch should return true', () => {
            const expectedErrors = new SignUpErrors("", "", "", "");
            const confirmedPassword = "password";

            const sut = new User("email", "username", confirmedPassword);

            const actualErrors = sut.validateUser(confirmedPassword);

            expect(actualErrors.usernameError).to.equal(expectedErrors.usernameError);
            expect(actualErrors.emailError).to.equal(expectedErrors.emailError);
            expect(actualErrors.passwordError).to.equal(expectedErrors.passwordError);
            expect(actualErrors.confirmPasswordError).to.equal(expectedErrors.confirmPasswordError);
        });

        it('when passing in empty fields, doPasswordsMatch should all errors', () => {
            const expectedErrors = new SignUpErrors(UserSignUpErrors.emailEmpty, UserSignUpErrors.userNameEmpty, UserSignUpErrors.passwordEmpty, UserSignUpErrors.confirmedPasswordEmpty);
            const confirmedPassword = "";

            const sut = new User("", "", "");

            const actualErrors = sut.validateUser(confirmedPassword);

            expect(actualErrors.usernameError).to.equal(expectedErrors.usernameError);
            expect(actualErrors.emailError).to.equal(expectedErrors.emailError);
            expect(actualErrors.passwordError).to.equal(expectedErrors.passwordError);
            expect(actualErrors.confirmPasswordError).to.equal(expectedErrors.confirmPasswordError);
        });
        
        it('when passing in different passwords, doPasswordsMatch should five errors for passwords', () => {
            const expectedErrors = new SignUpErrors("", "", UserSignUpErrors.passwordsDontMatch, UserSignUpErrors.passwordsDontMatch);
            const confirmedPassword = "p";

            const sut = new User("email", "username", "j");

            const actualErrors = sut.validateUser(confirmedPassword);

            expect(actualErrors.usernameError).to.equal(expectedErrors.usernameError);
            expect(actualErrors.emailError).to.equal(expectedErrors.emailError);
            expect(actualErrors.passwordError).to.equal(expectedErrors.passwordError);
            expect(actualErrors.confirmPasswordError).to.equal(expectedErrors.confirmPasswordError);
        });
    });
});