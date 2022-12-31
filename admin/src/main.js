// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'   //在页面中引入vue.js
import App from './App'   //引入自定义组件
import router from './router'  //引入vue router.js
import axios from 'axios';  //  引入axios
import ElementUI from 'element-ui'
import request from "./utils/request";//修改request
import 'element-ui/lib/theme-chalk/index.css'
// import {compareTime} from './utils/compareTime.js'
// import moment from 'moment'//数据库时间格式化
// import Vuex from 'vuex'
// import store from './store'

Vue.config.productionTip = false
Vue.use(ElementUI);
// Vue.use(Vuex);

// Vue.prototype.$Time = compareTime//对比时间工具
Vue.prototype.$http = request;  //修改内部的$http为axios 如$http.get("")和post("")方法
Vue.prototype.$axios = axios;
axios.defaults.withCredentials=true; //跨域


/* eslint-disable no-new */
 router.beforeEach((to,from,next)=>{//beforeEach是router的钩子函数，在进入路由前执行
      if(to.meta.title){//判断是否有标题
          document.title = to.meta.title
      }
      next()  //执行进入路由，如果不写就不会进入目标页
  })
new Vue({

  el: '#app',	//绑定Vue实例全局作用范围
  components: { App },	//注册App组件
  template: '<App/>',
  router,	//注册路由对象
  //store,//state对象
})
