// module.exports = {
//   productionSourceMap: false,
//   // 关闭ESLINT校验工具
//   lintOnSave: false,
//   //配置代理跨域
//   devServer: {
//     proxy: {
//       "/api": {
//         // target: "http://39.98.123.211",
//       },
//     },
//   },
// };
chainWebpack: (config) => {
    config
      .plugin('html')
      .tap(args => {
        args[0].title = '企业内部培训系统-教师端'
        return args
      })
  }
