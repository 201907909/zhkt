<template>
	<view>
		<view class="news_item" @click="navigator(item.id)" v-for="item in list" :key="item.id">
			<image :src="item.img_url" mode=""></image>
				<view class="right">
					<view class="tit">
						{{item.title}}
					</view>
					<view class="info">
						<text>发表时间：{{item.add_time | formatDate}}</text>
						<text>浏览次数：{{item.click}}</text>
					</view>
				</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: ['list'],
		filters: {
			formatDate (date) {
				const nDate = new Date(date)
				const year = nDate.getFullYear()
				//利用这个方法.toString().padStart(2,)处理未满两位的数字前面加0
				//padStart(2,0)判断是否满两位，若未满，前面补0
				const month = (nDate.getMonth()+1).toString().padStart(2,0)
				const day = nDate.getDate().toString().padStart(2,0)//获取日期
				// console.log(day)
				return year+'-'+month+'-'+day
			}
		},
		methods:{
			navigator(id){
				this.$emit("itemClick",id)
			}
		}
	}
</script>

<style lang="scss">
	.news_item{
		display: flex;
		padding: 10rpx 20rpx;
		border-bottom: 1px solid $shop-color;
		image{
			min-width: 200rpx;
			max-width: 200rpx;
			height: 150rpx;
		}
		.right{
			margin-left: 15rpx;
			display: flex;
			flex-direction: column;//变成侧轴
			justify-content: space-between;//两侧贴边对齐
			.tit{
				font-size: 33rpx;
			}
			.info{
				font-size: 24rpx;
				text:nth-child(2){
					margin-left: 30rpx;
				}
			}
		}
		
	}
</style>
