<template>
	<!-- 购买成功页面 -->
	<view class="buy">
		<view class="info-main">
			<image src="../../static/img/success.png" mode=""></image>
			<text>您已成功购买课程</text>
			<view class="button" @click="toStudy()">前往学习</view>
		</view>
	</view>
</template>

<script>
export default{
	methods:{
		//去学习
		toStudy() {
			uni.switchTab({
				url:'/pages/study/study'
			})
		}, 
	}
}
</script>

<style lang="scss">
	.buy{
		background-color: #FFFFFF;
		height: 1300rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		.info-main{
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			margin-top:-300rpx;
			image{
				width: 200rpx;
				height: 200rpx;
			}
			text{
				margin-top: 30rpx;
				font-size: 40rpx;
				// text-align: center;
			}
			.button{
				width: 250rpx;
				height: 80rpx;
				display: flex;
				justify-content: center;
				align-items: center;
				font-size: 36rpx;
				margin-top: 200rpx;
				border-radius: 50rpx;
				background-color: #7f94ff;
				color: #FFFFFF;
			}
		}
	}
</style>
