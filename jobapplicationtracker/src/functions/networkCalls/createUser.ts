import axios from "axios";
import { error } from "console";
import URLPath from "../../enums/URLPath_enum";
import User from "../../models/User";
import HttpResponse from "../../models/HttpResponse";

async function createUser(user: User): Promise<HttpResponse<string>> {
    const result = await axios.post(URLPath.createUser, user);
    const dataAsString: string = result.data;

    if (result.status >= 400) {
        return new HttpResponse<string>(result.status, dataAsString, "");
    } else {
        return new HttpResponse<string>(result.status, "", dataAsString);
    }
}

export default createUser;