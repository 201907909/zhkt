<template>
  <div id="app">
    <Top  v-show="$route.meta.isShow"></Top>
    <router-view></router-view>

  </div>
</template>

<script>
  import Top from "./components/Top";
  import Footer from "./components/Footer";
  export default {
    name: 'App',
    components: {
      Footer, //注册组件
      Top,
    }, //组件

    methods: { //方法

    },
    created() { //初始化

    }
  }
</script>

<style>
  body {
    background-color: white;
    margin: 0 auto;
    padding: 0;
    /*display: flex; !* 容器为flex布局 *!*/
    /*flex-direction: column; !* 方向为纵向，保持正常的从上到下文档流 *!*/
  }
</style>
