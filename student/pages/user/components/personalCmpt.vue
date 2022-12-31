<template>
	<view>
		<view class="view1">
			<view class="view2">
				<button style="border: none;" plain="true" class="head" @click="gotoLogin">
					<image :src="headImage.replace(/[\r\n]/g,'')" mode="aspectFill" class="headimg" @click="changeimg"></image>
				</button>
				<button style="border: none;" plain="true" class="info" @click="gotoLogin">
					<view class="up">
						<text class="text1">{{ nickName }}</text>
						<image class="moreimg" :src="more" mode="aspectFit"></image>
					</view>
					<view class="dn">
						<text class="text2">{{ phone }}</text>
					</view>
				</button>
			</view>
		</view>
	</view>
</template>

<script>
	// import {
	// 	mapState,
	// 	mapMutations
	// } from 'vuex';
	// import getToken from "../../../utils/refreshToken"

	export default {
		data() {
			return {
				more: '/static/user/20.png',
				nickName: '未登录',
				headImage: '/static/noLogin.png',
				phone: '未绑定手机号',
			};
		},
		created() {
		},
		onShow() {
			
		},
		methods: {
			changeimg() {
				
			},
			gotoLogin() {
				var storageUserId = uni.getStorageSync('userId') ;	//获取localStore中的‘userId’
				if(storageUserId){
					uni.navigateTo({
						url: '/pages/user/userInfo/userInfo'
					})
				}else{
					uni.navigateTo({
						url: '/pages/login/login'
					});
				}
			},
			showLogin() {
				var that = this;
				var storageUserId = uni.getStorageSync('userId') ;
				if(storageUserId){
					// console.log(uni.getStorageSync('userId'))
					uni.request({
						url: '/api/user/student/getStuDetails',
						header: {
						  "Authorization": "Bearar " + uni.getStorageSync("userId")
						},
						success(res) {
							// console.log('学生信息详情',res)
							if(res.data.data.stuName){
								that.nickName = res.data.data.stuName;
							}
							if (res.data.data.image){
								that.headImage = res.data.data.imageIO;
								// console.log(that.headImage)
							}
							if (res.data.data.phone) {
								that.phone = res.data.data.phone;
							} else {
								that.phone = '未绑定手机号';
							}
						}
					});
				}
			}
		},
		// computed: {
		// 	...mapState(['lgmin']),
		// 	countLog() {
		// 		this.nickName = this.lgmin.nickName;
		// 		this.headImage = this.lgmin.headImage;
		// 		this.phone = this.lgmin.phone;
		// 	}
		// }
	};
</script>

<style lang="scss">
	.view1 {
		width: 100%;
		height: 388rpx;
		padding-top: 88rpx;
		// background-color: #daf3cb;
		// background-color: #dae0fb;
		// background:repeating-linear-gradient(to bottom,#9b9ffb,#dae0fb);
		// background:repeating-linear-gradient(to bottom,#baf3cb,#daf3cb);
		background:repeating-linear-gradient(to bottom,#8b9dff,#dae0fb);
		border-bottom-left-radius: 50rpx;
		border-bottom-right-radius: 50rpx;
	}

	.view2 {
		height: 116rpx;
		width: 100%;
		position: relative;
		top: 52upx;
		display: flex;
		flex-direction: row;
	}

	.head {
		height: 140upx;
		width: 140upx;
		position: relative;
		left: 45upx;
	}

	.info {
		width: 558upx;
		height: 116upx;
		position: relative;
		left: 30upx;
	}

	.headimg {
		
		height: 112rpx;
		width: 112rpx;
		border-radius: 100%;
	}

	.up {
		height: 30upx;
		position: relative;
		top: 24upx;
		display: flex;
		flex-direction: row;
	}

	.dn {
		position: relative;
		top: 40upx;
	}

	.text1 {
		font-size: 32upx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #333333;
		position: relative;
		top: -15upx;
	}

	.text2 {
		display: inline-block;
		font-size: 24upx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #333333;
		position: absolute;
		top: 0upx;
		left: 0upx;
	}

	.moreimg {
		width: 17upx;
		height: 28upx;
		position: absolute;
		top: 18upx;
		left: 446upx;
	}
</style>
