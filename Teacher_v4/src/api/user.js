import request from '@/utils/request'

export function login(data){
    return request({
        url:'/user/teacher/login',
        method:'post',
        data
    })
}