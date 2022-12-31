<template>
	<div class="footer">
		<h4>页尾</h4>
	</div>
</template>

<script>
	export default {
		name: "Footer"
	}
</script>

<style lang="scss">

.footer{
  //position: absolute;
  //bottom: 0;
  width: 100%;
  height: 200px;
  background: #f8fafd;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;

  h4{
    margin: 0 auto;
    font-size: 14px;
    color: #8d8d8d;
  }
}
</style>
