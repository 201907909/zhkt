import axios from 'axios'

const request = axios.create({
 // baseURL:'http://172.20.181.131:9999/course',
  baseURL: '/api',
  timeout: 60000,//请求超时时间
  headers: {
    "Content-Type": "application/json;charset=UTF-8", //表单数据类型
  },
  withCredentials: true,//自动同步获取后台服务器cookie同步本地
})

// request.interceptors.request.use(
//   config => {
//     if(getToken()) { //判断有无获取到token
//       config.headers['Authorization'] = getToken() // 携带token
//     }
//     return config;
//   },
//   error => {
//     console.log("axios中request报错", error);
//     return Promise.reject(error);
//   }
// )

// response  拦截器 获取后台数据前拦截
request.interceptors.response.use(
  response => {
    const code = response.data.code || 200 ;//若未设置默认成功状态
    switch (code) {
      case 200://为200执行该行
        return response;//成功直接返回
      case 401://为401执行该行，跳转至服务熔断页面
        return response
      default://都不相同执行该行
        return response
    }
  },
  error => {
    console.log("axios中response报错", error);
    Message({
      showClose: true,
      message: error.message,//弹出失败原因
      type: 'error'
    })
    return Promise.reject(error);
  }
)
export default request
