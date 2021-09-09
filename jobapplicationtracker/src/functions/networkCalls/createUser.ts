import axios from "axios";
import { error } from "console";
import URLPath from "../../enums/URLPath_enum";
import User from "../../models/User";

async function createUser(user: User): Promise<string> {
    try {
        const result: string = await axios.post(URLPath.createUser, user);

        return result;
    } catch(error) {
        return  `error`;
    }
}

export default createUser;