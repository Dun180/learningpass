import axios from "axios";

//接口封装
export class API {
    server_url;
    constructor(server_url) {
        this.server_url = server_url;
    }


    //#region login
    //登录
    async login(form){
        const resp = await axios.post("/login",form);
        return resp
    }

    //注册
    async register(form){
        const resp = await axios.post("/register",form)
        return resp.data.data
    }

    //#endregion

    //#region class
    //根据班级id获取班级
    async getClassById(id){
        const resp = await axios.get(this.server_url+`/class/${id}`)
        return resp.data.data
    }



    //根据班级id获取班级成员
    async getClassMemberById(id,currentPage) {
        const resp = await axios.get(this.server_url+`/class/member/${id}?currentPage=${currentPage}`)
        return resp.data.data
    }

    //创建班级
    async createClass(classInfo){
        const resp = await axios.post(this.server_url+'/class:',classInfo);
        return resp.data.data
    }


    //修改班级
    async modifyClassInfo(form){
        const resp = await axios.post(this.server_url+'/class/modify',form);
        return resp.data.data
    }

    //#endregion

    //#region student
    //根据学生id获取班级列表(分页)
    async getClassesByStudentId(studentId,currentPage){
        const {data:{data:resp}} = await axios.get(this.server_url+`/student/classes/${studentId}?currentPage=${currentPage}`)
        return resp
    }

    //加入班级
    async joinClass(form){
        const resp = await axios.post(this.server_url + '/student/class:',form)
        return resp.data.data
    }


    //#endregion

    //region teacher
    //根据教师id获取班级
    async getClassesByTeacherId(teacherId,currentPage){
        const resp = await axios.get(this.server_url+`/teacher/classes/${teacherId}?currentPage=${currentPage}`)
        return resp.data.data
    }
    //#endregion
}