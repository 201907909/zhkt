<template>
	<view class="userinfo">
		
		<view class="wrap">
			<view class="u-avatar-wrap">
				<image @tap="chooseAvatar" class="u-avatar-demo" :src="avatar.replace(/[\r\n]/g,'')" mode="aspectFill"></image>
			</view>
			<u-button @tap="chooseAvatar">修改头像</u-button>
		</view>
		
		<u-form :model="form" ref="uForm" class="form">
			<u-form-item label="姓名" prop="name">
				<u-input v-model="form.name" />
			</u-form-item>
			<u-form-item label="电话" prop="mobile">
				<u-input v-model="form.mobile" />
			</u-form-item>
			<u-form-item label="邮箱" prop="email">
				<u-input disabled v-model="form.email" />
			</u-form-item>
			<u-form-item label="性别">
				<u-input v-model="sex" type="select" @click="show = true" />
				<u-action-sheet :list="actionSheetList" v-model="show" @click="actionSheetCallback"></u-action-sheet>
			</u-form-item>
			
		</u-form>
		<u-button @click="submit">提交</u-button>
	</view>
</template>

<script>
export default {
	data() {
		return {
			sex:'',
			avatar: '/static/noLogin.png', //头像
			show: false,
			actionSheetList: [
				{
					text: '男',
					value: 1
				},
				{
					text: '女',
					value: 0
				}
			],
			form: {
				name: '',
				intro: '',
				gender: '',
				mobile: '',
				email: '',
				// oldImage: 'default.jpg',
				oldImage: '10414103207419576321668333168897.jpeg',
				newImage: '',
			},
			rules: {
				// 姓名
				name: [
					{ 
						required: true, 
						message: '请输入姓名', 
						// 可以单个或者同时写两个触发验证方式 
						trigger: ['change','blur'],
					}
				],
				// 简介
				// intro: [
				// 	{
				// 		min: 5, 
				// 		message: '简介不能少于5个字', 
				// 		trigger: 'change'
				// 	}
				// ],
				// 手机号
				mobile: [
					{
						required: true, 
						message: '请输入手机号',
						trigger: ['change','blur'],
					},
					{
						// 自定义验证函数，见上说明
						validator: (rule, value, callback) => {
							// 上面有说，返回true表示校验通过，返回false表示不通过
							// this.$u.test.mobile()就是返回true或者false的
							return this.$u.test.mobile(value);
						},
						message: '手机号码不正确',
						// 触发器可以同时用blur和change
						trigger: ['change','blur'],
					}
				],
				// 邮箱
				email: [
					{ 
						required: true, 
						message: '请输入邮箱', 
						// 可以单个或者同时写两个触发验证方式 
						trigger: ['change','blur'],
					}
				],
			}
		};
	},
	created() {
		// 监听从裁剪页发布的事件，获得裁剪结果
		uni.$on('uAvatarCropper', path => {
			this.avatar = path;
			console.log('图片路径',this.avatar)
			// 可以在此上传到服务端
			uni.uploadFile({
				url: '/api/user/student/stuFile',
				filePath: path,
				name: 'multipartFile',
				complete: (res) => {
					// console.log(res);
					console.log(res.data)
					this.form.newImage = res.data
				}
			});
		})
		
		// 获取用户信息
		this.getUserInfo()
		
		
	},
	methods: {
		submit() {	// 提交
			this.$refs.uForm.validate(valid => {
				if (valid) {
					console.log('验证通过');
					this.update()
				} else {
					console.log('验证失败');
				}
			});
		},
		
		// 点击actionSheet回调
		actionSheetCallback(index) {
			this.sex = this.actionSheetList[index].text;
			this.form.gender = this.actionSheetList[index].value
		},
		
		chooseAvatar() {
			// 此为uView的跳转方法，详见"文档-JS"部分，也可以用uni的uni.navigateTo
			this.$u.route({
				// 1
				// 关于此路径，请见下方"注意事项"
				url: 'pages/user/u-avatar-cropper/u-avatar-cropper',
				// 内部已设置以下默认参数值，可不传这些参数
				params: {
					// 输出图片宽度，高等于宽，单位px
					destWidth: 300,
					// 裁剪框宽度，高等于宽，单位px
					rectWidth: 200,
					// 输出的图片类型，如果'png'类型发现裁剪的图片太大，改成"jpg"即可
					fileType: 'jpg',
				}
			})
		},
		
		// 更新用户信息
		update(){
			uni.request({
				method: 'POST',
				url:'/api/user/student/updateInfo',
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				data:{
					stuName: this.form.name,
					gender: this.form.gender,
					phone: this.form.mobile,
					oldImage: this.form.oldImage,
					newImage: this.form.newImage
				},
				success: (res) => {
					console.log(this.form.oldImage)
					console.log(this.form.newImage)
				}
			})
		},
		
		// 获取用户信息
		getUserInfo(){
			var that = this;
			uni.request({
				url: '/api/user/student/getStuDetails',
				header: {
				  "Authorization": "Bearar " + uni.getStorageSync("userId")
				},
				success(res) {
					console.log('学生信息详情',res)
					that.form.name = res.data.data.stuName;
					if (res.data.data.image != null){
						that.avatar = res.data.data.imageIO;
						that.form.oldImage = res.data.data.image
					}
					if (res.data.data.phone) {
						that.form.mobile = res.data.data.phone;
					}
					if (res.data.data.gender) {
						that.form.gender = res.data.data.gender;
						if(res.data.data.gender == 1){
							that.sex = '男'
						}else{
							that.sex = '女'
						}
					}
					if (res.data.data.email) {
						that.form.email = res.data.data.email
					}
				}
			});
		}
	},
	// 必须要在onReady生命周期，因为onLoad生命周期组件可能尚未创建完毕
	onReady() {
		this.$refs.uForm.setRules(this.rules);
	}
};
</script>

<style lang="scss">
.userinfo{
	margin: 10rpx;
	padding: 20rpx;
	background-color: #FFFFFF;
}

	.wrap {
		padding: 40rpx;
	}

	.u-avatar-wrap {
		margin-top: 80rpx;
		overflow: hidden;
		margin-bottom: 80rpx;
		text-align: center;
	}

	.u-avatar-demo {
		width: 150rpx;
		height: 150rpx;
		border-radius: 100rpx;
	}


</style>
