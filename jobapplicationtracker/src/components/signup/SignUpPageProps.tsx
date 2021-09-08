import { withRouter, RouteComponentProps } from 'react-router-dom';

interface SignUpProps extends RouteComponentProps {
    fullName: string
    username: string,
    email: string,
    password: string,
    confirmedPassword: string
};

export default SignUpProps;