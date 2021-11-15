import { RouteComponentProps } from 'react-router-dom';

interface LoginPageProps extends RouteComponentProps {
    username: string,
    password: string
};

export default LoginPageProps;