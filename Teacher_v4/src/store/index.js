import Vue from 'vue';
import Vuex from 'vuex';
import getters from './getters'
import user from '../store/modules/user'

Vue.use(Vuex);

//引入小仓库


//对外暴露Store类的一个实例
export default new Vuex.Store({
    modules: {
        user
    },
    getters,
})