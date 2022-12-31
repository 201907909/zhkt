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
          "image": '/static/user/setting.png',
          "title": '设置'
        },
        {
          "image": '/static/user/exit.png',
          "title": '退出登录'
        }
      ]
    };
  },
  methods: {
	click(index) {
		if(index == 0){	//设置页面
			// uni.navigateTo({
			// 	url: '/pages/cart/cart',
			// })
			uni.showToast({
			    title: '暂未开放设置',
			    duration: 2000,
				icon:'none'
			});
		}else if(index == 1){	//退出登录
			if(!uni.getStorageSync('userId')){
				uni.showToast({
					title:'您还没有登录哦',
					icon:'none'
				})
				// .then(
				// 	uni.navigateTo({
				// 		url: '/pages/login/login',
				// 	})
				// )
				return;
			}
			if(uni.getStorageSync('userId') != null) {
				uni.request({
					url:'/api/user/student/logout',
					header: {
					  "Authorization": "Bearar " + uni.getStorageSync("userId")
					},
					success() {
						uni.clearStorageSync()
						uni.showToast({
						    title: '退出登录成功',
						    duration: 2000,
						});
						location.reload()
					}
				})
				
			}
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
  // justify-content: space-around;
}
.item{
	width: 120rpx;
	height: 147rpx;
	margin-left: 20rpx;
	margin-right: 40rpx;
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
  border-radius: 33upx;
  margin-left: 4%;
  margin-top: 20rpx;
  position: relative;
  top: -81upx;
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
