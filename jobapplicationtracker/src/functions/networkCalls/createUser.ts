import axios from "axios";
import { error } from "console";
import URLPath from "../../enums/URLPath_enum";
import User from "../../models/User";

async function createUser(user: User): Promise<string> {
    const result = await axios.post(URLPath.createUser, user);

    if (result.status >= 400) {
        return `result.data`;
    } else {
        return "";
    }
}

export default createUser;