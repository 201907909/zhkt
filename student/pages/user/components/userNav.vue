<template>
  <view>
    <view class="grating">
      <view class="row">
        <view v-for="(item,index) in orderCmpt" :key="index" class="item">
          <view class="column" @tap="click(index)">
            <image :src="item.image" mode="aspectFit" class="img"></image>
            <text class="text">{{item.title}}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      orderCmpt: [
        {
          "image": '/static/user/cart.png',
          "title": '购物车'
        },
        {
          "image": '/static/user/gift.png',
          "title": '我的收藏'
        },
        {
          "image": '/static/user/message.png',
          "title": '我的消息'
        },
        {
          "image": '/static/user/point.png',
          "title": '签到领积分'
        }
      ]
    };
  },
  methods: {
	click(index) {
		if(!uni.getStorageSync('userId')){
			uni.showToast({
				title:'未登录，请先登录',
				icon:'none'
			})
			return;
		}
		if(index == 0){
			uni.navigateTo({
				url: '/pages/cart/cart',
			})
		}else if(index == 1){
			uni.navigateTo({
				url: '/pages/collection/collection'
			})
		}else if(index == 2){
			uni.navigateTo({
				url: '/pages/news/news'
			})
		}else if(index == 3){
			uni.navigateTo({
				url: '/pages/signIn/signIn'
			})
		}
		
		// if (!uni.getStorageSync('userid')) {
		// 	uni.showToast({
		// 	title: '未登录，请先登录',
		// 	icon: 'none',
		// 	duration: 2000
		// 	})
		// } else {
		// 	uni.navigateTo({
		// 	url: `/pages/activityOrder/activityOrder?id=${index}` //用于后续跳转到指定页面
		// 	})
		// }
    }
  }
}
</script>

<style lang="scss">
.row {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}
.item{
	width: 120rpx;
	height: 147rpx;
}
.column {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.grating {
  width: 92%;
  height: 155rpx;
  background: rgba(255, 255, 255, 1);
  border-radius: 33rpx;
  margin-left: 4%;
  position: relative;
  top: -81rpx;
  box-shadow: 0 0 12px 0 rgba(0,0,0,.06);
}
page {
  background-color: #f9f9f9;
}
.img {
  // width: 47upx;
  // height: 51upx;
  width: 56rpx;
  height: 56rpx;
  margin-top: 30rpx;
}
.text {
  font-size: 24rpx;
  font-family: PingFang SC;
  font-weight: 500;
  color: rgba(102, 102, 102, 1);
  margin-top: 20rpx;
}
</style>
