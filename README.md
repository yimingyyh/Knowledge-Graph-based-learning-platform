## 项目部署

- 前端使用vscode进行开发，使用了PanJiaChen的vue-element-admin前端框架，因文件过多这里只上传本系统开发过程中修改过的文件。
- 后端使用IntelliJ IDEA 开发。
- 数据库方面使用Mysql与Neo4j两种数据库，使用自己习惯的工具就行，这里使用的是navicat以及Neo4j Desktop。配置完数据库去后端修改配置。

## 启动

```bash
###前端
# 克隆项目
git clone https://github.com/PanJiaChen/vue-element-admin.git

# 进入项目目录
cd vue-element-admin

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 修改完之后启动服务
npm run dev
```

```bash
###后端
运行BackendJavaApplication.java即可
```



