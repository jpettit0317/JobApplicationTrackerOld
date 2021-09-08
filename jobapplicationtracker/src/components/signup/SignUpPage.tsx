import SignUpProps from "./SignUpPageProps";
import { withRouter } from "react-router";

const SignUpPage: React.FC<SignUpProps> = props => {
    return (
        <div>
            <h1>Sign Up Page!</h1>
        </div>
    );
};

export default withRouter(SignUpPage);