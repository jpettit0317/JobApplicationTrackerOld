import axios from "axios";
import URLPath from "../../enums/URLPath_enum";
import User from "../../models/User";
import HttpResponse from "../../models/HttpResponse";

async function createUser(user: User): Promise<HttpResponse<string>> {
    console.log("Making createUser request");
    console.log("The user is " + JSON.stringify(user)); 
    try {
        const response = await axios.post(URLPath.createUser, user);
        return new HttpResponse<string>(response.status, "", response.data);
    } catch(error: any) {
        const statusCode = Number(error.response.status);
        const reasonForFailure = String(error.response.data);

        return new HttpResponse<string>(statusCode, reasonForFailure, "");
    }
}

export default createUser;