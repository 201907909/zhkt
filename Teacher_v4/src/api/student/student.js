import request from '@/utils/request'

export function classQueryAllStu(email){
    return request({
        url:'/user/student/classQueryAllStu',
        method:'get',
        params:{email:email}
    })      
}