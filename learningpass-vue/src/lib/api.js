import axios from "axios";

export class API {
    server_url;
    constructor(server_url) {
        this.server_url = server_url;
    }

    //根据班级id获取班级成员
    async getClassMemberById(id) {
        const resp = await axios.get(this.server_url+`/class/member/${id}`)
        return resp.data.data
    }

    //根据学生id获取班级列表(分页)
    async getClassesByStudentId(studentId,currentPage){
        const {data:{data:resp}} = await axios.get(this.server_url+`/student/classes/${studentId}?currentPage=${currentPage}`)
        return resp
    }


}