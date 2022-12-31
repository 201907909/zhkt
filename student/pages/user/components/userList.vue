<template>
  <view>
    <view class="view">
      <view class="outside" id="list">
        <block v-for="(item,index) in userList" :key="index">
          <view class="inside" @tap="jump(item)">
            <image :src="item.image" mode="aspectFit" class="imglaq" :class="'img-width'+item.id"></image>
            <text class="textlaq">{{item.title}}</text>
            <image class="imglaq2" :src="item.img" mode="aspectFit"></image>
          </view>
        </block>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userList: [
        {
          'id': 1,
          "image": '/static/user/21.png',
          "title": '收藏',
          'img': '/static/user/20.png',
		  "url" : '/pages/collection/collection'
        },
        {
          'id': 2,
          "image": '/static/user/message.png',
          "title": '消息',
          'img': '/static/user/20.png'
        },
        {
          'id': 3,
          "image": '/static/user/setting.png',
          "title": '设置',
          'img': '/static/user/20.png'
        },
        {
          'id': 4,
          "image": '/static/feedback.png',
          "title": '退出登录',
          'img': '/static/user/20.png'
        }
      ]
    };
  },
  methods: {
    jump(item) {
      switch (item.id) {
        case 1:
			if(!uni.getStorageSync('userId')){
				uni.showToast({
					title:'未登录，请先登录',
					icon:'none'
				})
				return;
			}
			uni.navigateTo({
				url: '/pages/collection/collection'
			})
			break;
        case 2:
			if(!uni.getStorageSync('userId')){
				uni.showToast({
					title:'未登录，请先登录',
					icon:'none'
				})
				return;
			}
			uni.navigateTo({
				url: '/pages/aboutus/aboutus'
			})
          break;
        case 3:
			uni.navigateTo({
				url: '/pages'
			})
          break;
        case 4:
			if(uni.getStorageSync('userId') != null){
				uni.clearStorageSync()
				uni.showToast({
				    title: '退出登录成功',
				    duration: 2000,
				});
				location.reload()
			}
		break;
        default:
          break;
      }

    },
  },
	nav(url){
		uni.navigateTo({
			url:url
		})
	}

}
</script>

<style lang="scss">
page {
  background-color: #f9f9f9;
}
.view {
  width: 92%;
  height: 100%;
  background: rgba(255, 255, 255, 1);
  border-radius: 33rpx;
  margin-left: 4%;
  margin-top: -61rpx;
  box-shadow: 0 0 12px 0 rgba(0,0,0,.06);
}
.outside {
  display: flex;
  flex-direction: column;
  margin-left: 33upx;
}
.inside {
  display: flex;
  flex-direction: row;
  margin-top: 20upx;
  margin-bottom: 20upx;
  align-items: center;
}
.imglaq {
  // width: 30upx;
  // height: 28upx;
  width: 32rpx;
  height: 32rpx;
}
.textlaq {
  font-size: 24upx;
  font-family: Adobe Heiti Std;
  font-weight: normal;
  color: rgba(51, 51, 51, 1);
  margin-left: 19upx;
}
.imglaq2 {
  width: 15upx;
  height: 23upx;
  position: absolute;
  left: 661upx;
}
.img-width4{
	width: 30rpx;
	height: 30rpx;
}
</style>
