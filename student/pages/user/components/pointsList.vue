<template>
	<view class="points_list">
		<view class="title">
			<view class="bar"></view>
			<view class="info">学习积分</view>
		</view>
		<view class="points">
			<view class="list_item">
				<view class="item_title">
					今日积分
				</view>
				<view class="item_main">
					{{todayPoint}}
				</view>
			</view>
			<view class="bar"></view>
			<view class="list_item">
				<view class="item_title">
					累计积分
				</view>
				<view class="item_main">
					{{cumPoint}}
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default{
		data() {
			return {
				todayPoint:0,
				cumPoint:0
			};
		},
		created() {
			this.getTodayPoint()
		},
		methods:{
			getTodayPoint() {
				var that = this;
				var storageUserId = uni.getStorageSync('userId') ;
				console.log('这是积分列表',uni.getStorageSync('userId'))
				if(storageUserId != null && storageUserId != ""){
					uni.request({
						url: '/api/user/student/getStuDetails',
						header: {
						  "Authorization": "Bearar " + uni.getStorageSync("userId")
						},
						success(res) {
							if (res.data.data.scoreTotal) {
								that.cumPoint = res.data.data.scoreTotal;
							}
						}
					});
				}
			}
		}
	}
</script>

<style lang="scss">
	.points_list {
		width: 92%;
		height: 240rpx;
		background: rgba(255, 255, 255, 1);
		border-radius: 33rpx;
		margin-left: 4%;
		margin-bottom: 20rpx;
		position: relative;
		top: -81upx;
		box-shadow: 0 0 12px 0 rgba(0,0,0,.06);
	}
	
	.title{
		display: flex;
		padding: 20rpx;
		align-items: center;
		.bar{
			width: 10rpx;
			height: 40rpx;
			background-color: #cfd6f3;
			margin-right:25rpx;
		}
		.info{
			// font-weight: bold;
		}
	}
	
	.points{
		display: flex;
		// justify-content: space-around;
		.list_item{
			width: 50%;
			display: flex;
			flex-direction: column;
			justify-content: center;
			text-align: center;
			.item_title{
				font-size: 25rpx;
				margin-bottom: 10rpx;
			}
			.item_main{
				font-size: 56rpx;
				color: #f78e5a;
			}
		}
		.bar{
			width: 3rpx;
			height: 120rpx;
			background-color: #d6d7f3;
		}
	}
	
</style>
