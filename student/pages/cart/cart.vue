<template>
	<view class="cart">
		<!-- <view class="cartItem" v-for="(item, index) in goods" :key="index">
			<checkbox :checked="item.checked"/>
			<image src="item.img" mode=""></image>
		</view> -->
		<view class="none" v-show="goods.length==0">
			<image src="../../static/img/cart-none.png" mode="widthFix"></image>
			<text>你的购物车空荡荡的</text>
		</view>
		<view class="goods" v-show="goods.length>0">
			<view class="list-item" v-for="(item, index) in goods" :key="index">
				<view class="checkbox" @click="changeTotal(index)">
					<checkbox :checked="item.checked"/>
				</view>
				<image class="item-img" :src="item.img.replace(/[\r\n]/g,'')" mode="aspectFill" @click="toDetail(item.couId)"></image>
				<view class="item-info" @click="toDetail(item.couId)">
					<view class="info-title">{{item.name}}</view>
					<view class="info-bottom">
						<view class="info-money">
							<image src="../../static/img/point.png" mode=""></image>
							<text>{{item.money}}</text>
						</view>
						<view class="info-del" @click="del(index,item.couId)">
							<text>删除</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		
		<view class="goods_nav">
			<!-- <uni-goods-nav :fill="true"  :options="options" :buttonGroup="buttonGroup"  @click="onClick" @buttonClick="buttonClick" /> -->
			<view class="total">
				总价：
				<image src="../../static/img/point.png" mode=""></image>
				<text>{{total}}</text>
			</view>
			<view class="confirm" @click="buy">
				确认购买
			</view>
		</view>
	</view>
</template>

<script>

export default {
	data() {
		return {
			//全选是否选中
			allchecked:true,
			checked:true,
			total:0,
			goods:[
				// {
				// 	couId:1,
				// 	img:'/static/lesson/yisheng.jpg',
				// 	money:10,
				// 	name:'压疮预防与护理',
				// 	checked:true,
				// },
				// {
				// 	couId:2,
				// 	img:'/static/lesson/xianbei.jpg',
				// 	money:20,
				// 	name:'生活自理能力训练',
				// 	checked:true,
				// },
				// {
				// 	couId:3,
				// 	img:'/static/lesson/hongcha.jpg',
				// 	money:114,
				// 	name:'特制红茶饮用指南',
				// 	checked:true,
				// },
			]
		};
	},
	//下拉刷新
	onPullDownRefresh() {
	  this.total = 0
	  this.getCart()
	  uni.stopPullDownRefresh()
	},
	created() {
		// this.getCart();
		// for(var i = 0;i < this.goods.length;i++){
		// 	this.total += this.goods[i].money
		// }
	},
	onShow() {
		uni.showLoading({
			title: '加载中',
			mask: true
		});
		this.getCart();
		for(var i = 0;i < this.goods.length;i++){
			this.total += this.goods[i].money
		}
	},
	methods:{
		getCart() {
			uni.request({
				url: '/api/user/cart/getCouIdByStuId',
				method: 'GET',
				data: {},
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				success: (res) => {
					console.log('获取购物车内容',res)
					this.goods=[];
					for(var i=0; i < res.data.data.length; i++){
						var obj = {
							couId:res.data.data[i].couId,
							img:res.data.data[i].couPic,
							money:res.data.data[i].couPrice,
							name:res.data.data[i].couName,
							checked:true,
						}
						this.goods.push(obj)
					}
					this.getTotal()
				},
				complete() {
					//关闭加载中
					setTimeout(function() {
					  uni.hideLoading();
					}, 100);
					uni.stopPullDownRefresh()
				}
			});
		},
		buy() {
			var list = []
			for(var i=0; i < this.goods.length; i++){
				if(this.goods[i].checked){
					list.push(this.goods[i].couId)
				}
			}
			uni.request({
				url: '/api/user/cart/buyCartByCouId?couId='+list,
				method: 'POST',
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				success(res) {
					if(res.data.msg=="购买成功"){
						uni.navigateTo({
							url:'../buy/buy'
						})
					}else{
						uni.showToast({
							icon:'none',
							title:'积分不足'
						})
					}
				},
				fail() {
					uni.showToast({
						icon:'none',
						title:'发生错误'
					})
				}
			})
			
		},
		getTotal(){
			console.log('重新计算总价')
			this.total = 0
			for(var i = 0;i < this.goods.length;i++){
				console.log('这是第'+(i+1)+'个商品的选中情况：'+this.goods[i].checked)
				if(this.goods[i].checked == true){
					console.log('第'+(i+1)+'个累加了')
					this.total += this.goods[i].money
				}
			}
		},
		// 选中状态
		changeTotal(index){
			if(this.goods[index].checked ==false){
				this.goods[index].checked = true
			}else{
				this.goods[index].checked = false
			}
			this.getTotal()
		},
		
		// 删除
		del(index,couId){
			uni.request({
				url: '/api/user/cart/deleteCartByCouId?couId='+couId,				method: 'POST',
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				success(res) {
					console.log(res)
				}
			})
			this.goods.splice(index,1)
			this.getTotal()
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
	// .cartItem{
	// 	display: flex;
	// 	image {
	// 		width: 24rpx;
	// 		height: 24rpx;
	// 	}
	// }
	.none{
		padding-top: 130rpx;
		padding-left: 3%;
		padding-right: 3%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		image{
			width: 250rpx;
		}
		text{
			padding-top: 20rpx;
			font-size: 40rpx;
			opacity: 0.6;
		}
	}
	
	.goods{
		padding-bottom: 200rpx;
	}
	
	.list-item{
		display: flex;
		height: 200rpx;
		width: 92%;
		margin-left: 4%;
		padding-top: 26rpx;
		padding-bottom: 30rpx;
		border-radius: 20rpx;
		background: rgba(255, 255, 255, 1);
		margin-top: 20rpx;
		box-shadow: 0 0 12px 0 rgba(0,0,0,.06);
		
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
				justify-content: space-between;
				margin-bottom: 10rpx;
				.info-money{
					display: flex;
					align-items: flex-end;
					image {
						width: 26rpx;
						height: 26rpx;
						// margin-bottom: 10rpx;
						margin-right: 6rpx;
					}
					text{
						font-size: 48rpx;
						color: #ea0000;
						margin-bottom: -10rpx;
					}
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
	
	.goods_nav{
		width: 100%;
		position: fixed;
		bottom: 0;
		display: flex;
		align-items: center;
		background-color: #FFFFFF;
		height: 100rpx;
		.total{
			width: 50%;
			text-align: center;
			image {
				width: 24rpx;
				height: 24rpx;
				margin-right: 6rpx;
			}
			text{
				font-size: 48rpx;
				color: #ea0000;
			}
		}
		.confirm{
			display: flex;
			align-items: center;
			justify-content: center;
			color: #FFFFFF;
			width: 50%;
			height: 100%;
			text-align: center;
			background-color: #7f94ff;
		}
	}
</style>
