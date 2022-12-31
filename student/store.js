import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

// const store = new Vuex.Store({
// 	//配置选项 (state|mutations|actions)

// })

// export default store
const list = {
	listing: []
}

//tab切换的状态
const load = {
	loading: ''
}

const navmin = {
	loading: '',
	naving: 'zhaopin',
	pageid: 0,
	uniload: '',
	nonedata: ''
}

// tab切换没有数据
const nonemin = {
	nonedata: ''
}

// 订单分类tab的ID
const tid = {
	tabId: 0
}

// 活动tab的ID
const tbid = {
	tabId: 1
}

// 登录的数据
const lgmin = {
	nickName: '未登录',
	headImage: '/static/noLogin.png',
	phone: '未绑定手机号',
}

// 轮播图的id
const bid = {
	bnid: ''
}

// 活动的id
const activityid = {
	aid: ''
}

// 用户报名
const userSign = {
	name: '',
	idcard: '',
	phone: '',
	company: '',
	identity: ''
}

//报名次数统计
const acount = {
	activityCount: 0
}

// 数据仓库
const state = {
	list,
	load,
	navmin,
	nonemin,
	tid,
	tbid,
	lgmin,
	bid,
	activityid,
	userSign,
	acount,
}




export default new Vuex.Store({
	state,
	actions: {
		listact(listact, listdata) {
			console.log(listdata)
			listact.commit('listmut', listdata)
		}
	},
	mutations: {
		listmuta(state, listdata) {
			console.log(listdata)
			state.list = {
				listing: listdata
			}
		},

		//tab切换的loading状态
		loadmuat(state, loading) {
			state.load = {
				loading: loading
			}
		},

		// 以对象形式传过来的参数
		navmuta(state, pullobj) {
			state.navmin = {
				loading: pullobj.loading,
				naving: pullobj.nav,
				pageid: pullobj.pageid,
				uniload: pullobj.uniload,
				nonedata: pullobj.nonedata
			}
		},

		// tab切换没有数据
		nomemuta(state, noneion) {
			state.nonemin = {
				nonedata: noneion
			}
		},

		// 订单分类tab的id
		tabmuta(state, tabId) {
			state.tid = {
				tabId
			}
		},
		// 活动tab的id
		tabsId(state, tabId) {
			state.tbid = {
				tabId: tabId
			}
		},

		// 登录的参数
		lgmuat(state, loginobj) {
			state.lgmin = {
				nickName: loginobj.nickName,
				headImage: loginobj.headImage,
				phone: loginobj.phone,
			}
		},
		bannerId(state, id) {
			state.bid = {
				bnid: id
			}
		},
		actimuat(state, aid) {
			state.activityid = {
				aid: aid
			}
		},
		signUp(state, sign) {
			state.userSign = {
				name: sign.name,
				idcard: sign.idcard,
				tel: sign.tel,
				company: sign.company,
				identity: sign.identity
			}
		},
		acountmuat(state, act) {
			state.acount = {
				activityCount: act
			}
		},
	}
})