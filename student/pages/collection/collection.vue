<template>
	<view class="collection">
		<view class="none" v-show="goods.length==0">
			<image src="../../static/img/none.png" mode="widthFix"></image>
			<text>你的收藏空荡荡的</text>
		</view>
		<view class="list-item" @click="" v-for="(item, index) in goods" :key="index">
			<image @click="toDetail(item.couId)" class="item-img" :src="item.img.replace(/[\r\n]/g,'')" mode="aspectFill"></image>
			<view class="item-info">
				<view class="info-title" @click="toDetail(item.couId)">{{item.name}}</view>
				<view class="info-bottom">
					<view class="info-money"  @click="toDetail(item.couId)">
						<!-- <image src="../../static/img/point.png" mode=""></image> -->
						<text>{{item.couIntroduction}}</text>
					</view>
					<view class="info-del" @click="del(index,item.couId)">
						<text>删除</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default{
		data() {
			return {
				goods:[
					// {
					// 	img:'/static/lesson/yisheng.jpg',
					// 	couId:123,
					// 	name:'压疮预防与护理',
					// 	couIntroduction:'护理是一门学问，本课程属于护理学一的进阶'
					// },
					// {
					// 	img:'/static/lesson/xianbei.jpg',
					// 	couId:123,
					// 	name:'生活自理能力训练',
					// 	couIntroduction:'护理是一门学问，本课程属于护理学一的进阶'
					// },
					// {
					// 	img:'/static/lesson/hongcha.jpg',
					// 	couId:123,
					// 	name:'特制红茶饮用指南',
					// 	couIntroduction:'护理是一门学问，本课程属于护理学一的进阶'
					// },
				]
			};
		},
		created() {
			uni.showLoading({
				title: '加载中',
				mask: true
			});
			this.getAllColletion()
		},
		methods:{
			getAllColletion(){ //获取收藏
				uni.request({
					method: 'GET',
					url:'/api/user/colletion/getAllColletion',
					header: {
					  "Authorization": "Bearar " + uni.getStorageSync("userId")
					},
					success: (res) => {
						console.log(res)
						this.goods=[];
						for(var i=0; i < res.data.data.length; i++){
							var obj = {
								couId:res.data.data[i].couId,
								name:res.data.data[i].couName,
								img:res.data.data[i].couPic,
								couIntroduction:res.data.data[i].couIntroduction
							}
							this.goods.push(obj)
						}
					},
					complete() {
						//关闭加载中
						setTimeout(function() {
						  uni.hideLoading();
						}, 100);
						uni.stopPullDownRefresh()
					}
				})
			},
			del(index,couId){	//删除收藏
				uni.request({
					method: 'POST',
					url:'/api/user/colletion/deleted?couId='+couId,
					header: {
					  "Authorization": "Bearar " + uni.getStorageSync("userId")
					},
					success: (res) => {
						this.goods.splice(index,1)
					}
				})
				
			},
			
			//去课程详情
			toDetail(courseId) {
			  uni.navigateTo({
			    url: '/pages/index/compoents/courseDetail?couId=' + courseId,
			  });
			}, 
			
		}
	}
</script>

<style lang="scss">
	.none{
		padding-top: 130rpx;
		padding-left: 3%;
		padding-right: 3%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		image{
			width: 350rpx;
		}
		text{
			padding-top: 20rpx;
			font-size: 40rpx;
			opacity: 0.7;
		}
	}
	
	
	.list-item{
		display: flex;
		height: 200rpx;
		width: 92%;
		margin-left: 4%;
		padding-top: 25rpx;
		padding-bottom: 30rpx;
		border-radius: 20rpx;
		background: rgba(255, 255, 255, 1);
		box-shadow: 0 0 12px 0 rgba(0,0,0,.06);
		margin-top: 20rpx;
		
		.checkbox{
			margin-left: 3%;
			width: 10%;
			display: flex;
			align-items: center;
			justify-content: center;
		}
		.item-img{
			width: 254rpx;
			height: 150rpx;
			align-items: center;
			overflow: hidden;
			border-radius: 12rpx;
			margin-left: 20rpx;
		}
		.item-info{
			margin-left: 24rpx;
			flex: 1;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			.info-title{
				font-size: 28rpx;
				color: #333333;
				word-break: break-all;
				overflow: hidden;
				text-overflow: ellipsis;
			}
			.info-bottom{
				display: flex;
				align-items: flex-end;
				justify-content: space-between;
				margin-bottom: 10rpx;
				.info-money{
					      overflow: hidden;
					      white-space: nowrap;
					      text-overflow: ellipsis;
					
					width: 280rpx;
					font-size: 20rpx;
					color: #888888;
				}
				.info-del{
					margin-right: 24rpx;
					font-size: 24rpx;
					color: #888888;
					display: flex;
					justify-content: center;
					align-items: flex-end;
					// margin-bottom: 6rpx;
				}
			}
		}
	}
</style>
