import SignUpPage from "./SignUpPage";

function createSignUpPage(username: string = "", email: string = "", password: string = "", confirmedPassword: string = ""): JSX.Element {
    return (
        <SignUpPage username={username} email={email} password={password} confirmedPassword={confirmedPassword} />
    );
}

export default createSignUpPage;