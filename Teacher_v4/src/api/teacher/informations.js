import request from '@/utils/request'
import qs from 'qs'


// 获取资讯
export function getInforList(current) {
    return request({
        url: 'interaction/information/getAllInforByTeaId',
        method: 'get',
        params: { current: current }
    })
}
// 搜索资讯
export function searchInfor(current, val) {
    return request({
        url: 'interaction/information/teaSearch',
        method: 'get',
        params: { current: current, val: val }
    })
}
// 发布资讯
export function addInforList(data) {
    return request({
        url: 'interaction/information/add',
        method: 'post',
        data
    })
}
// 修改资讯
export function updateList(data) {
    return request({
        url: 'interaction/information/update',
        method: 'post',
        data
    })
}
// 查询资讯详情
export function getInforDetail(current, infoId) {
    return request({
        url: 'interaction/information/DetailInfoByInfoId',
        method: 'get',
        params: { current: current, infoId: infoId }
    })
}
// 删除资讯
export function deleteInfor(data) {
    return request({
        url: 'interaction/information/deleted',
        method: 'post',
        data
    })
}
// 资讯评论
export function getComment(infoId) {
    return request({
        url: 'interaction/information/commentListByInfoId',
        method: 'post',
        method: 'get',
        params: { infoId: infoId }
    })
}
