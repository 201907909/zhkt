import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import request from "./utils/request";
import axios from 'axios';
import API from '@/api';
import './assets/fonts/iconfont.css'
Vue.prototype.$API = API;

// 引入echarts
import * as echarts from 'echarts';
Vue.prototype.$echarts = echarts

ElementUI.Dialog.props.lockScroll.default = false;
axios.defaults.withCredentials=true;

Vue.config.productionTip = false
Vue.use(ElementUI);
// import moment from 'moment'//数据库时间格式化
// import {compareTime} from './utils/compareTime.js'
// Vue.prototype.$Time = compareTime//对比时间工具
Vue.prototype.$http = request;  //修改内部的$http为axios 如$http.get("")和post("")方法
Vue.prototype.$axios = axios;
axios.defaults.withCredentials=true; //session跨域


new Vue({
  beforeCreate() {
    Vue.prototype.$bus = this
  },
  el: '#app',
  components: { App },
  template: '<App/>',
  router,
  store
})
