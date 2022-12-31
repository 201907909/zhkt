export default function () {
    uni.getUserInfo({
        success(res) {
            uni.login({
                success(res1) {
                    uni.request({
						// 访问后台登录接口
                        url: this.server_url+'/user/commonUser/wx/login',
                        method: 'post',
                        data: {
                            code: res1.code,
                            encryptedData: res.encryptedData,
                            iv: res.iv
                        },
                        header: {
                            // 'content-type': 'application/x-www-form-urlencoded' // 默认值
                        },
                        success(res2) {
                            console.log(res2);
                            uni.setStorageSync('token', res2.data.token);	//保存token
                            uni.setStorageSync('stuid', res2.data.data.id);
                            // uni.setStorageSync('expireTime', res2.data.expire);
                        }
                    })
                }
            })
        },
    })
    console.log("刷新token");
}