<template>
  <view class="bac_color">
    <!-- 签到次数 -->
    <view class="sign_bac">
      <image :src="headImage" class="headimg"></image>
      <view class="sign_count">
        <!-- <image :src="avatarUrl" class="avatar"></image> -->

        <view class="sign_des">
          <view class="conti_day">已连续签到<text class="num"> {{sign.day}} </text>
            天</view>
          <view>明日可得<text class="num"> {{sign.integral}} </text>积分</view>
        </view>
      </view>
    </view>

    <!-- 签到日期 -->
    <view class="sign_date">
		<uni-calendar 
		    :insert="true"
		    :lunar="false"
			:selected="selected"
			:end-date="new Date().toISOString().slice(0, 10)"
			:showMonth="false"/>
		<view class="signIn-btn" @click="handelSign()" v-if="!signState">点击签到</view>
		<view class="signIn-btn" v-if="signState">今日已签</view>
    </view>

    <!-- 签到规则 -->
    <view class=" sign_rule">
      <text>签到规则</text>
      <p>1.用户每日登录进行签到，可获得积分奖励</p>
      <p>2.签到可获得5积分</p>
      <p>3.如7天内连续签到，每天都会比前一天多拿1积分</p>
    </view>

    <!-- 签到弹框 -->
    <view class="pop_shade" v-if="signSucc">
      <view class="pop">
        {{signInfo}}
        <text class="sign_get" v-show="signAga">加{{signGet}}积分</text>
        <image :src="signImage" class="sign_logo" mode="aspectFill"></image>
        <image src="/static//img/guanbi.png" class="close_logo" @click="closePop()"></image>
      </view>
    </view>
  </view>

</template>

<script>
  export default {
    data() {
      return {
	    selected: [ //打点日期，实际开发中，这里需要请求后端接口，拿取数据后在进行渲染
		  {
			date: "2021-11-21",
			info: '已打卡', 
		  },
		  {
			date: "2022-11-22",
			info: '已打卡', 
		  },
		  {
			date: "2022-11-23",
			info: '已打卡', 
		  },
		  {
			date: "2022-11-24",
			info: '已打卡', 
		  },
		  {
			date: "2022-11-25",
			info: '已打卡', 
		  },
		],
		signState: false,//今日打卡状态
		signSucc: false,//打卡成功提示
        headImage: '/static/img/true.png',
        moneyImage: '/static/img/money.png',
        avatarUrl: '/static/img/350.png',
        signImage: '/static/img/sign.png',
        sign: {
          day: 0,
          integral: 5
        },
      }
    },
	onLoad() {
		uni.showLoading({
			title: '加载中',
			mask: true
		});
		this.getSignDate()
	},
    methods: {
      closePop() {
        this.signSucc = false;
      },
	  
		/* 获取打卡日期 */
		getSignDate(){
			var date = new Date().toISOString().slice(0, 10);//签到功能获取当前时间并格式化
			uni.request({
				url:'/api/user/studentClock/getSignInDaysOfTheMonth',
				method: 'post',
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				success: (res) => {
					console.log('打卡记录：',res)
					this.sign.day = res.data.data.continuedClock
					this.sign.integral = res.data.data.tomorrowScore
					this.selected = res.data.data.stuClockRecordVOList
					
					for(var i=0; i<this.selected.length; i++){
						if(this.selected[i].date == date){
							this.signState = true
							// console.log('签到了')
						}
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
	  
		/* 点击签到 */
	    handelSign(){
			var date = new Date().toISOString().slice(0, 10);//签到功能获取当前时间并格式化
			console.log(date) //当我们这里点击签到后就可以将当前日期参数返回给后端，存入表中
			
			uni.request({
				url:'/api/user/studentClock/clock',
				method: 'get',
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				success: (res) => {
					console.log('打卡结果：',res)
					if(res.data.code!=500 && res.data.code!=503){
						this.sign.day = res.data.data.continuedClock
						this.sign.integral = res.data.data.tomorrowScore
						this.selected = res.data.data.stuClockRecordVOList
						
						for(var i=0; i<this.selected.length; i++){
							if(this.selected[i].date == date){
								this.signState = true
								this.signSucc = true
							} 
						}
					}else if(res.data.code==500){
						uni.showToast({
							icon:'error',
							title: '打卡失败',
							duration: 2000,
						})
					}else if(res.data.code==503){
						uni.showToast({
							icon:'error',
							title: '今日已打卡',
							duration: 2000,
						})
					}
				},
			})
			
			// this.selected.push({
			// 	date: date,
			// 	info: '已打卡', 
			// })
			
			

		}
	  
    }

  }
</script>

<style lang="scss" scoped>
  .bac_color {
    background-color: #f5f3f7;
    padding-bottom: 80rpx;

    // 签到次数
    .sign_bac {
      .headimg {
        width: 100%;
        height: 340rpx;
      }

      .sign_count {
        display: flex;
        justify-content: center;
        align-items: center;
        position: absolute;
        top: 6%;
        left: 20%;

        .avatar {
          display: block;
          width: 100rpx;
          height: 100rpx;
          border-radius: 50rpx;
          border: 2rpx solid white;
        }

        .sign_des {
          margin-left: 30rpx;
          font-size: 40rpx;
          color: white;

          .conti_day {
            font-size: 32rpx;
          }

          .num {
            font-size: 44rpx;
            color: #50cf78;
			padding-left: 10rpx;
			padding-right: 10rpx;
          }
        }


      }
    }

    // 签到日期 
    .sign_date {
      position: relative;
      display: flex;
	  flex-direction: column;
      border-radius: 16rpx;
      width: 93%;
	  min-height: 600rpx;
      margin: 0 auto;
      margin-top: -80rpx;
      background-color: white;
      justify-content: space-evenly;
      // font-size: 24rpx;
      padding: 10rpx 0 10rpx 0;
	  align-items: center;
	  .signIn-btn{
		  display: flex;
		  justify-content: center;
		  align-items: center;
		  color: #FFFDEF;
		  background-color: #8fa9ff;
		  border-radius: 50rpx;
		  margin: 15rpx;
		  font-size: 36rpx;
		  width: 80%;
		  height: 80rpx;
	  }
    }

    // 签到规则
    .sign_rule {
      width: 93%;
      background-color: white;
      margin: 20rpx auto;
      border-radius: 16rpx;
      padding: 40rpx;

      text {
        font-size: 40rpx;
      }

      p {
        margin-top: 18rpx;
      }

    }

    // 签到弹框
    .pop_shade {
      display: flex;
      position: absolute;
      top: 0rpx;
      left: 0rpx;
      width: 100%;
      height: 100%;
      background: rgba(227, 227, 227, 0.12);

      .pop {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        width: 500rpx;
        height: 600rpx;
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        background-color: white;
        border-radius: 20rpx;
        padding: 20rpx;
        font-size: 40rpx;
		border-radius: 16rpx;
		box-shadow: 0 4px 24px -10px rgba(0,0,0,.44);
		
        .sign_get {
          font-size: 30rpx;
          color: #41a863;
        }

        .sign_logo {
          width: 100%;
		  margin: 0rpx;
		  padding: 0rpx;
		  height: 430rpx;
        }

        .close_logo {
          width: 60rpx;
          height: 60rpx;
        }
      }
    }

  }
</style>