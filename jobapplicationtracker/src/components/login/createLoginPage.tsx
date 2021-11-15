import LoginPage from "./LoginPage";

const createLoginPage = (username: string = "", password: string = "") : JSX.Element => {
    return (
        <LoginPage username={username} password={password} />
    )
};

export default createLoginPage;