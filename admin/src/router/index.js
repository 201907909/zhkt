import Vue from 'vue'
import Router from 'vue-router'
import Center from '../components/Center.vue'
import textAudit from '../components/textAudit.vue'
import textHistory from '../components/textHistory.vue'
import imgAudit from '../components/imgAudit.vue'
import imgHistory from '../components/imgHistory.vue'
import login from '../components/Login.vue'
import textSafe from  '../components/textSafe'
import imgSafe from  '../components/imgSafe'


Vue.use(Router)

export default new Router({
  routes: [{
      path: '/',
      redirect: '/login',

    },

    {
      path: '/login',
      component: login,
      meta: {
        title: "企业内部培训系统-管理端-登陆"
      },

    },
    {
      path: '/center',
      name: 'Center',
      component: Center,
      meta: {
        isShow: true,
        title: "企业内部培训系统-管理端"
      },
      children: [{
          path: 'textAudit',
          name: 'textAudit',
          component: textAudit,
          meta: {
            isShow: true,
            title: "企业内部培训系统-管理端"
          },
        },
        {
          path: 'textHistory',
          name: 'textHistory',
          component: textHistory,
          meta: {
            isShow: true,
            title: "企业内部培训系统-管理端"
          },
        },
        {
          path: 'textSafe',
          name: 'textSafe',
          component: textSafe,
          meta: {
            isShow: true,
            title: "企业内部培训系统-管理端"
          },
        },
        {
          path: 'imgAudit',
          name: 'imgAudit',
          component: imgAudit,
          meta: {
            isShow: true,
            title: "企业内部培训系统-管理端"
          },
        },
        {
          path: 'imgHistory',
          name: 'imgHistory',
          component: imgHistory,
          meta: {
            isShow: true,
            title: "企业内部培训系统-管理端"
          },
        },
        {
          path: 'imgSafe',
          name: 'imgSafe',
          component: imgSafe,
          meta: {
            isShow: true,
            title: "企业内部培训系统-管理端"
          },
        },
      ]

    }
  ]

})
