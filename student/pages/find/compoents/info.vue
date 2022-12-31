<template>
  <view class="info">
    <view class="search-wrap">
      <u-search bg-color="#ffffff" border-color="#7f94ff" search-icon-color="#7f94ff" :height="56" inputAlign="center"
        :disabled="true" @click="toSearch" placeholder="请输入你要搜索的资讯" v-model="keyword" :show-action="false">
      </u-search>
    </view>
    <view class="info-wrapper">
      <view class="info-item" @click="goInfoDetail(item.infoId),item.isRead=1" v-for="item in infoList">
        <view class="info-image">
          <image v-show="item.isRead==0" src="/static/find/red-news.png" mode="aspectFit"></image>
        </view>
        <view class="info-title">
          <text>{{item.infoTitle}}</text>
        </view>
        <view class="info-author">
          <text>{{item.infoAuthor}}</text>
        </view>
        <view @ class="info-content">
          <text class="info-content-set">{{item.infoMain.replace(/<[^>]+>/gi,'')}}</text>
        </view>
        <view class="info-data">
          <view class="info-data-give">
            <text>{{item.infoLike}} 点赞</text>
          </view>
          <view class="info-data-comment">
            <text>{{item.count}} 评论</text>
          </view>
          <view class="info-data-time">
            <text>发表时间：{{item.createTime}}</text>
          </view>
        </view>
      </view>
    </view>
	<view class="empty">
		<u-empty v-show="infoList.length==0" text="暂无资讯" mode="news" icon-color="#7f94ff">
		</u-empty>
	</view>
  </view>
</template>
<script>
  export default {

    data() {
      return {
        keyword: '',
        infoList: [],
        page: 1, //页数，
        // //资讯记录数(供父find调用)
        // total: 0
      }
    },


    methods: {
      goInfoDetail(id) {
        uni.navigateTo({
          url: "/pages/find/compoents/infoDetail?id=" + id
        })
      },
      //去搜索
      toSearch() {
        uni.navigateTo({
          url: '/pages/find/compoents/toSearchInfo',
        });
      },

      //获取所有资讯(分页)
      //index 判断是否下拉和刷新(false)，或者下滑(true)
      getAllInfo(index) {
        if (!index) {
          this.infoList = []
          this.page = 1
        }
        if (index) {
          this.page += 1; //每触底一次 page +1
        }
        uni.request({
          method: 'GET',
          url: '/api/interaction/information/getAllInfor',
          data: {
            current: this.page
          },
          success: (res) => {
            // this.total = res.data.data.total
            if (this.page === 1) {
              this.infoList = res.data.data.data
            } else {
              this.infoList = this.infoList.concat(res.data.data.data) //将数据拼接在一起
            }


            if (index) {
              //如果返回没有数据了,数组长度为0
              if (res.data.data.data.length == 0) {
                // console.log(this.infoList);
                //没有数据就替换一下loadingText的值
                uni.showToast({
                  title: '没有更多数据了',
                  icon: 'none',
                  mask: false
                })
              }
            }

          },
          complete() {
            //关闭加载中
            setTimeout(function() {
              uni.hideLoading();
            }, 100);
          }

        });
      }

    },
    created() {
      this.getAllInfo(false)
      uni.showLoading({
        title: '加载中',
        mask: true
      });

    },

  }
</script>

<style lang="scss" scoped>
  .info {
    .info-content {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
    }

    .search-wrap {
      padding: 0 20rpx;
      padding-top: 20rpx;
      padding-bottom: 20rpx;
      flex: 1;
      background-color: #FFFFFF;
      width: 100%;
    }

    .info-wrapper {


      .info-item:hover {
        box-shadow: 0 4px 24px -10px rgba(0, 0, 0, .44);
      }

      .info-item {
        position: relative;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        padding-left: 20rpx;
        width: 94%;
        margin-left: 3%;
        margin-top: 20rpx;
        padding: 15rpx;
        padding-left: 20rpx;
        padding-right: 20rpx;
        max-height: 250rpx;
        border-radius: 10rpx;
        box-shadow: 0 0 12px 0 rgba(0, 0, 0, .06);
        transition: box-shadow 0.3s ease-in-out;
        background: rgba(255, 255, 255, 1);

        .info-image {
          position: absolute;
          top: 0px;
          right: 0px;

          image {
            width: 50rpx;
            height: 50rpx;
          }
        }

        .info-title {
          font-size: 36rpx;
          font-weight: bolder;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .info-author {
          font-size: 26rpx;
          color: #888888;
        }

        .info-content-set {
          /* margin-left: 20rpx; */
          margin-right: 15rpx;
          margin-bottom: 10rpx;
          display: -webkit-box;
          overflow: hidden;
          font-size: 28rpx;
          font-weight: 500;
          text-overflow: ellipsis;
          word-wrap: break-word;
          white-space: normal !important;
          //显示行
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }

        .info-data {
          // opacity: 0.5;
          color: #888888;
          font-size: 26rpx;
          // margin-top: 10rpx;
          display: flex;
          flex-direction: row;
          justify-content: space-between;
        }

      }
    }
  }
  
  .empty{
  	  margin-top: 200rpx;
  }
</style>
