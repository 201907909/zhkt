<template>
  <view v-cloak>
	<personalCmpt ref="child"></personalCmpt>
	<pointsList ref="point"></pointsList>
	<userNav></userNav>
	<userNavTwo></userNavTwo>
  </view>
</template>

<script>
import personalCmpt from "./components/personalCmpt.vue"
import userNav from "./components/userNav.vue"
import userList from "./components/userList.vue"
import pointsList from "./components/pointsList.vue"
import userNavTwo from "./components/userNavTwo.vue"
import logicLogic from '../../common/loginLogic.js'

export default {
	components: {
		personalCmpt,
		userNav,
		userNavTwo,
		userList,
		pointsList
	},
	data() {
		return {
		}
	},
	created() {
		// this.loginUser()
		// this.showLogin()
	},
	onShow() {
		this.showLogin()
	},
	onReady() {
		this.showLogin()
	},

	methods:{
		showLogin(){
			this.$refs.child.showLogin() //登录验证
			this.$refs.point.getTodayPoint()//获取点数
		},
		loginUser(){
			if(uni.getStorageSync('userId')) {
				let account = uni.getStorageSync('account');
				let password = uni.getStorageSync('password');
				logicLogic.login(account,password,function(status,res) { 
					if(status){
						uni.showToast({
							title: '自动登录成功',
							duration: 2000,
						}).then(
							uni.switchTab({
								url:'../user/user'
							})
						)
						
					}else{
						// uni.showToast({
						// 	title: '登录失败',
						// 	duration: 2000,
						// 	icon:'none'
						// });
					}
				});
			}
		}
	}
}
</script>

<style>
page {
  background-color: #f9f9f9;
}
</style>
