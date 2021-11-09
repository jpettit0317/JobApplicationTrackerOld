import URLPath from "../../enums/URLPath_enum";
import Login from "../../models/Login";
import HttpResponse from "../../models/HttpResponse";
import axios from "axios";

const loginUser = async (login: Login): Promise<HttpResponse<string>> => {
    try {
        const response = await axios.post(URLPath.loginUser, login);
        const sessionId = response.data.value;
        return new HttpResponse<string>(response.status, "", sessionId);
    } catch(error: any) {
        const statusCode = Number(error.response.status);
        const reasonForFailure = String(error.response.data.message);

        return new HttpResponse<string>(statusCode, reasonForFailure, "");
    }
}

export default loginUser;