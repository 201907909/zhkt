//对  uni.request  进行封装

export default function (config) {

    const { baseURL, headers } = config;

    return function (Vue) {

        //在Vue 的原型上添加了一个request方法
        Vue.prototype.request = async function (params) {

            const { url, data, method, header = {},loading } = params;

            // 显示加载状态,默认显示加载框，传true不显示
            if (!loading) {
                uni.showLoading({ title: '正在加载...' });
			}

            const res = await uni.request({
                url: baseURL + url,
                header: { ...headers, ...header },
                data,
                method
            });

            // 关闭加载状态
            uni.hideLoading();

            return res[1].data;
        }
    }

}

