import Vue from 'vue'
import VueRouter from "vue-router";

import login from '../views/login'
import classManagement from '../views/home/classManagement'
import courseManagement from '../views/home/courseManagement'
import inforManagement from '../views/home/inforManagement'
import courseDetails from '../views/courseDetails'
import classDetails from '../views/classDetails'
import inforDetails from '../views/inforDetails'
import addCourse from '../views/classDetails/addCourse'
import dataScreen from '../views/home/dataScreen/index'
import myMessage from '../views/home/myMessage'
import messageDetails from '../views/home/myMessage/messageDetails'
import courseShow from '../views/courseDetails/courseShow'
import couPreview from '../views/home/coursePerview/couPreview'
import practiceManagement from '../views/home/practiceManagement'
import practiceDetails from '../views/home/practiceManagement/practiceDetails'

Vue.use(VueRouter);

let router = new VueRouter({
  routes: [{
      path: '/',
      redirect: '/login',
    },
    {
      path: '/login',
      component: login,
      meta: {
        title: "企业内部培训系统-教师端-登陆"
      },
    },
    {
      path: '/home',
      component: () => import('../views/home'),
      children: [{
          path: 'classManagement',
          component: classManagement,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: 'courseManagement',
          component: courseManagement,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: 'inforManagement',
          component: inforManagement,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: 'dataScreen',
          component: dataScreen,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: '/home',
          redirect: '/home/dataScreen',
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: "courseDetails",
          component: courseDetails,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: "courseShow",
          component: courseShow,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: "myMessage",
          component: myMessage,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: "messageDetails",
          component: messageDetails,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: "couPreview",
          component: couPreview,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: "/classDetails",
          component: classDetails,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: "inforDetails",
          component: inforDetails,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: "addCourse",
          component: addCourse,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: 'practiceManagement',
          name: 'practiceManagement',
          component: practiceManagement,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        },
        {
          path: 'practiceDetails',
          name: 'practiceDetails',
          component: practiceDetails,
          meta: {
            isShow: true,
            title: "企业内部培训系统-教师端"
          },
        }
      ]
    },
  ],



})

router.beforeEach((to, from, next) => { //beforeEach是router的钩子函数，在进入路由前执行
  if (to.meta.title) { //判断是否有标题
    document.title = to.meta.title
  }
  next() //执行进入路由，如果不写就不会进入目标页
})

export default router;
