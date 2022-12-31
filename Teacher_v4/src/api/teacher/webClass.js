import request from '@/utils/request'

export function getAllClassById(){
    return request({
        url:'/class/class/queryClassByTeaId',
        method:'get'
    })
}

export function deleteClass(id){
    return request({
        url:'/class/class/deleteClassById',
        method:'get',   
        params:{id:id}
    })
}

export function getClassById(id){
    return request({
        url:'/class/class/queryClassByClaId',
        method:'get',
        params:{id:id}
    })
}

//新增
export function createClass(data){
    JSON.stringify(data)
    return request({
        url:'/class/class/addClass',
        method:'post',
        data
    })
}

export function classFile(data){
    return request({
        url:'/class/class/classFile',
        method:'post',
        data
    })
}

export function addClassStudent(data){
    return request({
        url:'/class/class/classAddStuList',
        method:'post',
        data
    })
}


export function getAllCourseInClass(classId){
    return request({
        url:'/course/course/queryCourseByClassId',
        method:'get',
        params:{classId:classId}
    })
}

export function teaQueryAllCourse(){
    return request({
        url:'/course/course/teaQueryAllCourse',
        method:'get'
    })
}

export function deleteBanch(data){
    return request({
        url:'/class/class/classDeletedStuList',
        method:'post',
        data
    })
}

export function deleteCourseBanch(data){
    return request({
        url:'/class/class/classDeletedCouList',
        method:'post',
        data
    })
}

export function updateClass(data){
    return request({
        url:'/class/class/updateClassById',
        method:'post',
        data
    })
}

export function classAddCouList(data){
    return request({
        url:'/class/class/classAddCouList',
        method:'post',
        data
    })
}

export function queryCourseByClassId(data){
    return request({
        url:'/class/class/queryClassCourseByClaId',
        method:'post',
        data
    })
}

export function pageCourse(cou){
    return request({
        url:'course/course/selectAllCourse',
        method:'post',
        params:{classId:cou.classId,startIndex:cou.startIndex, pageSize:cou.pageSize}
    })
}

export function getCourseByStuId(stuId){
    return request({
        url:'course/course/getCourseByStuId',
        method:'get',
        params:{stuId:stuId}
    })
}

//课程预览
export function getAllAnyFieryCourse(){
    return request({
        url:'course/course/getAllAnyFieryCourse',
        method:'get'
    })
}