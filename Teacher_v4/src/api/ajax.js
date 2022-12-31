import store from "@/store";
import axios from "axios";
//进度条
import nProgress from "nprogress";
import "nprogress/nprogress.css";

//对axios进行二次封装
let requests = axios.create({
    baseURL: "/api",
    timeout: 5000,
})

//请求拦截器
requests.interceptors.request.use((config) => {
    nProgress.start();
    return config;
});

//响应拦截器
requests.interceptors.response.use(
    (res) => {
        nprogress.done();
        return res.data;
    },
    (err) => {
        alert("服务器响应数据失败");
    }
);

//对外暴露
export default requests;
