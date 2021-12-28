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

    //向班级中添加（创建）学生
    async addStudentToClass(form){
        const resp = await axios.post(this.server_url+'/class/add/student:',form);
        return resp.data.data
    }

    //删除学生
    async deleteStudent(form){
        const resp = await axios.post(this.server_url+'/class/delete',form);
        return resp.data.data
    }

    async selectStudent(form){
        const resp = await axios.get(this.server_url+`/class/select?classId=${form.classId}&select=${form.select}&value=${form.value}`)
        return resp.data.data
    }

    //根据班级id获取作业布置列表
    async getTaskArrangementList(classId){
        const resp = await axios.get(this.server_url+`/class/${classId}/taskArrangementList`)
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

    //根据学生获取作业列表
    async getTasksByStudentId(studentId){
        const resp = await axios.get(this.server_url+`/student/${studentId}/taskList`)
        return resp.data.data
    }

    //根据学生id，作业布置id获取详细作业信息
    async getTaskInfo(studentId,arrangementId){
        const resp = await axios.get(this.server_url+`/student/answer`,{params:{studentId:studentId,arrangementId:arrangementId}})
        return resp.data.data
    }

    //提交作业
    async submitAnswer(answer){
        const resp = await axios.post(this.server_url+`/student/submitAnswer`,answer)
        return resp.data.data
    }


    //#endregion

    //#region teacher
    //根据教师id获取班级
    async getClassesByTeacherId(teacherId,currentPage){
        const resp = await axios.get(this.server_url+`/teacher/classes/${teacherId}?currentPage=${currentPage}`)
        return resp.data.data
    }

    //创建或修改作业
    async createOrUpdateTask(taskInfo){
        const resp = await axios.post(this.server_url+'/teacher/task:',taskInfo);
        return resp.data.data
    }

    //根据教师id获取作业列表
    async getTasksByTeacherId(teacherId,currentPage){
        const resp = await axios.get(this.server_url+`/teacher/${teacherId}/taskList?currentPage=${currentPage}`)
        return resp.data.data
    }

    //根据教师id获取全部作业
    async getAllTaskByTeacherId(teacherId){
        const resp = await axios.get(this.server_url+`/teacher/${teacherId}/allTask`)
        return resp.data.data
    }

    //获取作业
    async getTaskDetails(id){
        const resp = await axios.get(this.server_url+`/teacher/tasks/${id}`)
        return resp.data.data
    }

    //布置作业
    async taskArrangement(arrangementInfo){
        const resp = await axios.post(this.server_url+'/teacher/taskArrangement',arrangementInfo)
        return resp.data.data
    }
    //#endregion


    //#region group
    //创建分组
    async creatGroup(groupInfo){
        const resp = await axios.post(this.server_url+'/teacher/group:',groupInfo);
        return resp.data.data
    }

    //获取分组列表
    async getGroupList(classId){
        const resp = await axios.get(this.server_url+`/class/${classId}/groupList`)
        return resp.data.data
    }

    //获取分组成员
    async getGroupMember(groupId){
        const resp = await axios.get(this.server_url+`/class/group/${groupId}`)
        return resp.data.data
    }
    //#endregion

}