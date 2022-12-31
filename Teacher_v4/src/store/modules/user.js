import { getToken, setToken, removeToken } from '../../utils/auth'
import { login} from '../../api/user'

const getDefaultState = () => {
    return {
      token: getToken(),
      email: '',
    }
  }
  
  const state = getDefaultState()
  
  const mutations = {
    RESET_STATE: (state) => {
      Object.assign(state, getDefaultState())
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, email) => {
      state.email = email
    },
   
  }
  
  const actions = {
    // user login
    async login({ commit }, userInfo) {
      const { account, password } = userInfo
      const result = await login({ account: account, password: password })
      console.log(result)
      let usertoken=result.data.data.token
      console.log(result.data.data.token)
      commit('SET_TOKEN', usertoken)
      commit('SET_NAME',userInfo.email)
      setToken(usertoken)
    },
  
    // get user info
    // getInfo({ commit }) {
    //   return new Promise((resolve, reject) => {
    //     getInfo().then(response => {
    //       const data = response
  
    //       console.log('我是getinfo'+JSON.stringify(data));
          
    //       var onlyroles=[]
    //      for(var i=0;i<data.length;i++){
    //        onlyroles.push(data[i].roleName);
    //      }
    //      console.log(onlyroles)
    //       var roles = onlyroles
  
    //       // const name = data.phone
    
    //       // // 角色必须是非空数组
    //       if (!roles || roles.length <= 0) {
    //         reject('getInfo: 角色必须是非空数组!')
    //       }
    //       // commit('SET_NAME', phone)
    //       commit('SET_ROLES', roles)
  
    //       const users = {
    //         roles: roles,
    //         // phone: phone
    //       }
    //       // console.log(users)
    //       resolve(users)
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },
  
  
    // user logout
    // logout({ commit, state }) {
    //   return new Promise((resolve, reject) => {
    //     logout(state.token).then(() => {
    //       removeToken() // must remove  token  first
    //       resetRouter()
    //       commit('RESET_STATE')
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },
  
    // remove token
    // resetToken({ commit }) {
    //   return new Promise(resolve => {
    //     removeToken() // must remove  token  first
    //     commit('RESET_STATE')
    //     resolve()
    //   })
    // }
  }
  
  export default {
    namespaced: true,
    state,
    mutations,
    actions
  }