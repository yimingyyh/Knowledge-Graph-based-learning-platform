import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],

  server:{
    proxy: { 
      '/bj': {
        target: 'http://localhost:8080/',  //你要跨域访问的网址
        changeOrigin: true,   // 允许跨域
        rewrite: (path) => path.replace(/^\/bj/, '') // 重写路径把路径变成空字符
      }
    }
  }
})
