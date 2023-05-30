const { defineConfig } = require('@vue/cli-service')

const server = "http://127.0.0.1:8080"

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 7070,
    proxy:{
      '/tax/*':{
        target: server,
        changeOrigin:true,
        pathReWrite: {
          '^/tax/*': ''
        }
      }
    }
  }
})
