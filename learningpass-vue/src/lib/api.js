import axios from "axios";

export class API {
    server_url;
    constructor(server_url) {
        this.server_url = server_url;
    }

    async getClassMemberById(id) {
        const resp = await axios.get(this.server_url+`/class/member/${id}`)
        return resp.data.data
    }


}