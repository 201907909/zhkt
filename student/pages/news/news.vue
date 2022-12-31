<template>
	<view class="news">
		<!-- <scroll-view scroll-x class="scroll" scroll-with-animation="true"> -->
		<view class="scroll">
			<block v-for="(item, index) in tab" :key="index">
				<view class="list-cont" @click="tabs(item.id, item.title)" :class="{ active: item.id == tabId }">
					<text class="con-text">{{ item.title }}</text>
					<view class="swiper-tab-line"></view>
				</view>
			</block>
		</view>
		<!-- </scroll-view> -->
		<view class="news-main">
			<!-- 回答模块 -->
			<view class="news-items" v-show="tabId == 1">
				<view class="none" v-show="answerList.length==0">
					<image src="../../static/img/none.png" mode="widthFix"></image>
					<text>暂无消息</text>
				</view>
				<view class="news-item" v-for="(item,index) in answerList"  @click="navToAnswer(item.queId)">
					<view class="news-info">
						{{ item.content }}
					</view>
					<view class="item-bottom">
						<view class="news-title">
							{{ item.queTitle }}
						</view>
						<view class="news-state" v-show="item.isSelect == 1">
							已采纳
						</view>
					</view>
				</view>
			</view>
			<!-- 回答模块结束 -->
			
			<!-- 提问模块 -->
			<view class="news-items" v-show="tabId == 2">
				<view class="none" v-show="questionList.length==0">
					<image src="../../static/img/none.png" mode="widthFix"></image>
					<text>暂无消息</text>
				</view>
				<view class="news-item" v-for="(item,index) in questionList" @click="navToAnswer(item.queId)">
					<view class="news-top">
						<view class="news-info-q">
							{{ item.queTitle }}
						</view>
						<view class="news-content">
							{{ item.content }}
						</view>
					</view>
					<!-- <view class="item-bottom">
						<view class="news-title"></view>
						<view class="news-title">
							{{ item.title }}
						</view>
					</view> -->
				</view>
			</view>
			<!-- 提问模块结束 -->
			
			<!-- 系统通知模块 -->
			<view class="news-items" v-show="tabId == 3">
				<view class="none" v-show="noticeList.length==0">
					<image src="../../static/img/none.png" mode="widthFix"></image>
					<text>暂无消息</text>
				</view>
				<view class="news-item" v-for="(item,index) in noticeList">
					<view class="item-top">
						<view class="news-info">
							<view v-if="item.isPass == 0">
								标题为“{{ item.title }}”的提问内容疑似存在违规信息，可提交复审
							</view>
							<view v-if="item.isPass == 1">
								标题为“{{ item.title }}”的提问，内容正在复审中
							</view>
							<view v-if="item.isPass == 2">
								标题为“{{ item.title }}”的提问，审核已通过
							</view>
							<view v-if="item.isPass == 3">
								标题为“{{ item.title }}”的提问，内容不符合规范，审核不通过
							</view>
						</view>
						<view class="del" @click="del(item.noticeId)">
							<image src="../../static/img/close.png"></image>
						</view>
					</view>
					
					<view class="item-bottom">
						<view class="news-title-l">
							<view v-if="item.isPass == 0">
								内容疑似存在违规信息，可提交复审
							</view>
							<view v-if="item.isPass == 1">
								内容复审中
							</view>
							<view v-if="item.isPass == 2">
								审核通过
							</view>
							<view v-if="item.isPass == 3">
								审核不通过
							</view>
						</view>
						<view class="news-title">
							<view v-if="!item.isPass == 0">
								{{ item.respondent }}
							</view>
							<view v-if="item.isPass == 0" class="application" @click="application(item.noticeId)">
								申请复审
							</view>
						</view>
					</view>
				</view>
			</view>
			<!-- 系统通知模块结束 -->
		</view>
	</view>
</template>

<script>
export default{
	data() {
		return {
			userId:'1037792125862802209',
			
			tabId: 1,
			tab: [
				{title:'我的回答',id:1},
				{title:'我的提问',id:2},
				{title:'系统通知',id:3}
			],
			answerList:[
				// {
				// 	content:'关于这个问题,我就说两句,这种事情见得多了,我只想说懂得都懂,不懂的我也不多解释,毕竟自己知道就好,细细品吧',
				// 	queTitle:'这是问题文章的标题',
				// 	queId:11,
				// 	isSelect:0
				// },
				// {
				// 	content:'你说的对，但是Uniapp是一款使用Vue.js开发所有前端应用的框架',
				// 	queTitle:'这是问题文章的标题',
				// 	queId:11,
				// 	isSelect:1
				// },
				// {
				// 	content:'开发者编写一套代码，可发布到iOS、Android、Web（响应式）、以及各种小程序、快应用等多个平台',
				// 	queTitle:'这是问题文章的标题',
				// 	queId:11,
				// 	isSelect:0
				// },
				// {
				// 	content:'不如问问神奇海螺',
				// 	queTitle:'海绵宝宝说的对吗？',
				// 	queId:11,
				// 	isSelect:1
				// },
			],
			questionList:[
				// {
				// 	queTitle:'只有红茶可以吗？',
				// 	content:'如题，只有红茶可以吗？',
				// 	title:'特制红茶饮用指南'
				// },
				// {
				// 	queTitle:'怎么办？急急急！具体怎么护理？',
				// 	content:'Rt',
				// 	title:'压疮预防与护理'
				// },
				// {
				// 	queTitle:'人可以不用吃饭就生存吗？',
				// 	content:'如题，很苦恼，望解答',
				// 	title:'生活自理能力训练'
				// },
			],
			noticeList:[
				// {
				// 	content:'您的文章疑似存在不合规信息，未通过审核',
				// 	title:'未通过审核通知',
				// 	respondent:'系统通知'
				// },
				// {
				// 	content:'您的图片疑似存在血腥、暴力、黄色等内容，未通过审核',
				// 	title:'未通过审核通知',
				// 	respondent:'系统通知'
				// },
				// {
				// 	content:'您的评论复审已驳回，原因："造成人身攻击"',
				// 	title:'未通过审核通知',
				// 	respondent:'系统通知'
				// },
			]
		}
	},
	created() {
		
	},
	
	//下拉刷新
	onPullDownRefresh() {
		this.getAll(); //获取所有我的消息
		uni.stopPullDownRefresh()
	},
	
	onLoad() {
		// console.log('App onLoad')
		this.getAll(); //获取所有我的消息
	},

	methods:{
		tabs(id, nav) {
			this.tabId = id;
		},
		
		getAll(){	//获取全部信息
			uni.showLoading({
				title: '加载中',
				mask: true
			});
			this.getMyAllReply();
			// 获取我的所有回答
			this.getMyIssues();
			// 获取我的所有提问
			this.getStuNotice();
			// 获得系统通知
		},
		
		getMyAllReply(){
			// 获取我的所有回答
			uni.request({
				// method: 'POST',	//请求方式
				url:'/api/interaction/wenda/getMyAllReply',	//访问接口
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				success: (res) => {	//访问成功后...
					// console.log('这是我的回答：',res);	//打印从后台获取到的res信息
					this.answerList=[]
					for(var i=0; i < res.data.data.length; i++){
						for(var j = 0; j<res.data.data[i].replyVOList.length;j++) {
							var obj = {
								'content':res.data.data[i].replyVOList[j].content,
								'queTitle':res.data.data[i].title,
								'isSelect':res.data.data[i].replyVOList[j].isSelect,
								'queId':res.data.data[i].replyVOList[j].answeredTqId
							}
							this.answerList.push(obj)
						}
					}
					// console.log('这是当前的回答列表',this.answerList)
					//关闭加载中
					setTimeout(function() {
					  uni.hideLoading();
					}, 100);
					uni.stopPullDownRefresh()
				},
				fail() {
					//关闭加载中
					setTimeout(function() {
					  uni.hideLoading();
					}, 100);
					uni.stopPullDownRefresh()
				}
			})
		},
		
		getMyIssues(){
			// 获取我的所有提问
			this.questionList=[]
			uni.request({
				// method: 'POST',
				url:'/api/interaction/wenda/getMyIssues',
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				success: (res) => {
					// console.log('这是我的提问：',res);
					for(var i=0; i < res.data.data.length; i++){
						var obj = {
							'queTitle':res.data.data[i].title,
							'content':res.data.data[i].content,
							'title':'我的',
							'queId':res.data.data[i].queId
						}
						this.questionList.push(obj)
					}
					//关闭加载中
					setTimeout(function() {
					  uni.hideLoading();
					}, 100);
					uni.stopPullDownRefresh()
				},
				fail() {
					//关闭加载中
					setTimeout(function() {
					  uni.hideLoading();
					}, 100);
					uni.stopPullDownRefresh()
				}
			})
		},
		
		getStuNotice(){
			// 获取所有系统通知
			this.noticeList=[]
			uni.request({
				method: 'GET',
				url:'/api/user/notice/stuNotice',
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				success: (res) => {
					// console.log('这是系统通知：',res);
					// this.noticeList=[]
					for(var i=0; i < res.data.data.length; i++){
						var obj = {
							'content':res.data.data[i].content,
							'title':res.data.data[i].title,
							'noticeId':res.data.data[i].noticeId,
							'respondent':res.data.data[i].createTime,
							'isPass':res.data.data[i].isPass
						}
						this.noticeList.push(obj)
					}
					//关闭加载中
					setTimeout(function() {
					  uni.hideLoading();
					}, 100);
					uni.stopPullDownRefresh()
				},
				fail() {
					//关闭加载中
					setTimeout(function() {
					  uni.hideLoading();
					}, 100);
					uni.stopPullDownRefresh()
				}
			})
		},
		
		// 提交复审
		application(noticeId){
			uni.request({
				method: 'GET',
				url:'/api/user/notice/application?noticeId='+noticeId,
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				success: (res) => {
					// console.log('提交复审：',res);
					uni.showToast({
					  title: '提交复审成功',
					  duration: 2000,
					})
					this.getStuNotice()//重新获取系统通知列表
				},
				fail() {
					uni.showToast({
					  title: '提交复审失败',
					  duration: 2000,
					  icon:'none'
					})
				}
			})
		},
		
		// 删除通知
		del(noticeId){
			var list = []
			list.push(noticeId)
			uni.request({
				method: 'POST',
				url:'/api/user/notice/deleted?noticeId='+list,
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				success: (res) => {
					console.log('删除通知：',res);
					uni.showToast({
					  title: '删除成功',
					  duration: 2000,
					})
					this.getStuNotice()//重新获取系统通知列表
				},
				fail() {
					uni.showToast({
					  title: '删除失败',
					  duration: 2000,
					  icon:'none'
					})
				}
			})
		},
		
		
		navToAnswer(id){
			// uni.navigateTo({
			// 	url:'compoents/answer?data='+encodeURIComponent(JSON.stringify(item))
			// })
			console.log('跳转到问题',id)
			uni.navigateTo({
			  url: '/pages/find/compoents/questionDetail?id=' + id
			})
		}
		
	}
}
</script>

<style lang="scss">
.news{}
// 置顶导航栏
.scroll {
	white-space: nowrap;
	background-color: #fff;
	padding: 20rpx 0 0rpx 24rpx;
	box-sizing: border-box;
	display: flex;
	justify-content: space-around;
}
.list-cont {
	display: inline-block;
	margin-right: 56rpx;
}
.con-text {
	color: #888888;
	font-size: 30rpx;
	text-align: center;
}
//被选中状态下的样式
.active .con-text {
	color: #333333;
	font-weight: bold;
}
.active .swiper-tab-line {
	// border-bottom: 8rpx solid #5598e7;
	width: 100%;
	margin: 20rpx auto 0;
	border-top: 8rpx solid #899bff;
	
	border-radius: 20rpx;
}
// 置顶导航栏结束

.news-main{
	.news-items{
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
		.news-item:hover{
			box-shadow: 0 4px 24px -10px rgba(0,0,0,.44);
		}
		.news-item{
			background-color: #FFFFFF;
			margin-top: 20rpx;
			width: 94%;
			margin-left: 3%;
			height: 180rpx;
			border-radius: 10rpx;
			box-shadow: 0 0 12px 0 rgba(0,0,0,.06);
			transition: box-shadow 0.3s ease-in-out;
			padding: 15rpx;
			padding-left: 20rpx;
			padding-right: 20rpx;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			.news-top{
				display: flex;
				flex-direction: column;
				.news-content{
					// font-weight: 900;
					opacity: 0.8;
					margin-top: 10rpx;
					font-size: 26rpx;
					overflow: hidden;
					text-overflow: ellipsis;  /* 超出部分省略号 */
					word-break: break-all;  /* break-all(允许在单词内换行。) */  
					display: -webkit-box; /* 对象作为伸缩盒子模型显示 */
					-webkit-box-orient: vertical; /* 设置或检索伸缩盒对象的子元素的排列方式 */
					-webkit-line-clamp: 3; /* 显示的行数 */
					// max-height: 80rpx; /* 设置最大高度，根据行高，要几行乘以几倍 */
				}
			}
			.news-info{
				font-weight: 550;
				font-size: 30rpx;
				// font-weight: bold;
				overflow: hidden;
				text-overflow: ellipsis;  /* 超出部分省略号 */
				word-break: break-all;  /* break-all(允许在单词内换行。) */  
				display: -webkit-box; /* 对象作为伸缩盒子模型显示 */
				-webkit-box-orient: vertical; /* 设置或检索伸缩盒对象的子元素的排列方式 */
				-webkit-line-clamp: 2; /* 显示的行数 */
				max-height: 80rpx; /* 设置最大高度，根据行高，要几行乘以几倍 */
				// white-space: nowrap;
			}
			
			.item-top{
				display: flex;
				justify-content: space-between;
				.news-info{
					max-width: 660rpx;
				}
				.del{
					display: flex;
					justify-content: center;
					padding-top: 10rpx;
					image{
						width: 24rpx;
						height: 24rpx;
					}
					font-size: 24rpx;
					font-weight: normal;
					opacity: 0.8;
				}
			}
			
			
			.news-info-q{
				font-weight: 550;
				font-size: 30rpx;
				// font-weight: bold;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;  /* 超出部分省略号 */
			}
			.item-bottom{
				display: flex;
				justify-content: space-between;
				// align-items: flex-end;
				align-items: center;
				.news-title-l{
					font-size: 26rpx;
					color: #888888;
					// margin-bottom: 10rpx;
					width: 450rpx;
					// max-width: 500rpx;
					// white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;  /* 超出部分省略号 */
					word-break: break-all;  /* break-all(允许在单词内换行。) */  
					display: -webkit-box; /* 对象作为伸缩盒子模型显示 */
					-webkit-box-orient: vertical; /* 设置或检索伸缩盒对象的子元素的排列方式 */
					-webkit-line-clamp: 1; /* 显示的行数 */
				}
				.news-title{
					font-size: 26rpx;
					color: #888888;
					// margin-bottom: 10rpx;
					// width: 360rpx;
					max-width: 500rpx;
					overflow: hidden;
					text-overflow: ellipsis;  /* 超出部分省略号 */
					word-break: break-all;  /* break-all(允许在单词内换行。) */  
					display: -webkit-box; /* 对象作为伸缩盒子模型显示 */
					-webkit-box-orient: vertical; /* 设置或检索伸缩盒对象的子元素的排列方式 */
					-webkit-line-clamp: 1; /* 显示的行数 */
					
					.application{
						width: 130rpx;
						padding: 8rpx;
						padding-left: 15rpx;
						padding-right: 15rpx;
						background-color: #7f94ff;
						color: #FFFFFF;
						display: flex;
						justify-content: center;
						align-items: center;
						font-size: 24rpx;
						border-radius: 25rpx;
					}
					
				}
				.news-state{
					font-size: 28rpx;
					background-color: #74c83f;
					width: 130rpx;
					padding: 5rpx;
					text-align: center;
					border-radius: 30rpx;
					color: #FFFFFF;
				}
			}
		}
	}
}

</style>
