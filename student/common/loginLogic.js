
import Vue from 'vue'

export default {
	login(account,password,callback){
		if(account != null && password != null && account != '' && password!= '' ){
			uni.request({
				url: '/api/user/student/login',
				method: 'post',
				data: {
					account: account,
					password: password,
				},
				success(res) {
					console.log('登录返回的：',res)
					if(res.code == 500 || res.data.code == 500){
						callback(false,res);
						return
					}
					if(callback != null) {
						callback(true,res);
					}
					uni.setStorageSync('userId', res.data.data);	//保存userId
					uni.setStorageSync('account', account);			//保存账号
					uni.setStorageSync('password', password);		//保存密码
				},
				
			});
		}else{
			if(callback != null){
				callback(false,null);
			}
			// uni.setStorageSync('userId', null);
			uni.clearStorageSync(); //清除数据
		}
	}
}