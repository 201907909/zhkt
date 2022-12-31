import request from '@/utils/request'

export function getPracticeInfoByTea(id) {
    return request({
        url: '/course/teacherPractice/getPracticeInfoByTea',
        method: 'get',
        params: { id:id }
    })
}

export function uploadPicture(data) {
    return request({
        url: '/course/teacherPractice/uploadPicture',
        method: 'post',
        data
    })
}

export function deletePicture(oldImg) {
    return request({
        url: '/course/teacherPractice/deletePicture',
        method: 'post',
        params: { oldImg:oldImg }
    })
}

export function addPractice(data) {
    return request({
        url: '/course/teacherPractice/addPractice',
        method: 'post',
        data
    })
}

export function deletePractice(id,teaId) {
    return request({
        url: '/course/teacherPractice/deletePractice',
        method: 'post',
        params: { id: id, teaId: teaId }
    })
}

export function editPractice(data) {
    return request({
        url: '/course/teacherPractice/editPractice',
        method: 'post',
        data
    })
}

export function getAllPracticeInfoByTea() {
    return request({
        url: '/course/teacherPractice/getAllPracticeInfoByTea',
        method: 'get'
    })
}

export function getAllPracticeType() {
    return request({
        url: '/course/teacherPractice/getAllPracticeType',
        method: 'get'
    })
}

export function getAllCourseByTeaId() {
    return request({
        url: '/course/teacherPractice/getAllCourseByTeaId',
        method: 'get'
    })
}

export function getPracticeByInfo(info) {
    return request({
        url: '/course/teacherPractice/getPracticeByInfo',
        method: 'get',
        params: { info: info }
    })
}

export function getOverduePractice() {
    return request({
        url: '/course/teacherPractice/getOverduePractice',
        method: 'get'
    })
}


//*************课程预览****************

export function getParentTypeId() {
    return request({
        url: '/course/course/getParentTypeId',
        method: 'get'
    })
}

export function getFieryCouTypeId() {
    return request({
        url: '/course/course/getFieryCouTypeId',
        method: 'get'
    })
}

export function getCourseByInfo(courseInfo) {
    return request({
        url: '/course/course/getCourseByInfo',
        method: 'get',
        params: { courseInfo: courseInfo }
    })
}

export function getCouTypeByParentTypeId(parentTypeId) {
    return request({
        url: '/course/course/getCouTypeByParentTypeId',
        method: 'get',
        params: { parentTypeId: parentTypeId }
    })
}
