# Teaching-Vue
基于SprinigBoot+Jwt+Vue的前后端分离的教学管理系统

2021年6月10日项目完成进度（前端Vue）

1.安装element-ui

cnpm install element-ui --save

在main.js中引入依赖

import Element from 'element-ui'

import "element-ui/lib/theme-chalk/index.css"

Vue.use(Element)

2.安装axios、qs、mockjs

cnpm install axios --save

在main.js中引入依赖

import axios from 'axios'

Vue.prototype.$axios = axios 

cnpm install qs --save

cnpm install mockjs --save-dev

require("./mock") //引入mock数据

3.实现登录页面、token状态同步、定义全局axios拦截器

Login.vue、axios.js

4.实现后台基本页面开发

Index.vue、Home.vue、User.vue、Role.vue ...

5.实现用户登录信息提示

Index.vue

6.实现动态菜单栏开发

router/index.js、store/index.js、store/modules/menus.js、includ/SideMenu.vue ...