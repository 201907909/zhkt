<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style lang="scss">
	/*每个页面公共css */
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import 'uni_modules/uview-ui/index.scss';
	@import 'static/fonts/iconfont.css';
	
	body{
		background-color: #f7f6f8;
		// background-color: #F2F2F2;
	}
	/*每个页面公共css */
	uni-checkbox .uni-checkbox-input {
		border-radius: 50%;
		border: 1rpx solid rgb(149, 149, 151);
	}
	
	uni-checkbox .uni-checkbox-input.uni-checkbox-input-checked {
		border: 1rpx solid #9eb3ff;
		background-color: #93a3ff;
	}
	
	uni-checkbox .uni-checkbox-wrapper {
		width: 100%;
	}
	
	uni-checkbox .uni-checkbox-input.uni-checkbox-input-checked:before{
		color: #FFFFFF;
	}
	
/* 	uni-checkbox:not([disabled]) .uni-checkbox-input:hover{
		border-color: transparent;
	} */
</style>
