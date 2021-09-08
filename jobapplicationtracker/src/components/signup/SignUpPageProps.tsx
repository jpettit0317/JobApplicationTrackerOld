import { withRouter, RouteComponentProps } from 'react-router-dom';

interface SignUpProps extends RouteComponentProps {
    username: string,
    email: string,
    password: string,
    confirmedPassword: string
};

export default SignUpProps;