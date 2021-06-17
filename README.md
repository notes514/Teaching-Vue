# Teaching-Vue
基于SprinigBoot+Jwt+Vue的前后端分离的教学管理系统

2021年6月10日前端(Vue)

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

2021年6月11日前端(Vue)

1.实现动态标签页切换显示

Tabs.vue

2.实现个人中心、菜单管理、角色管理、用户管理页以及按钮权限控制等
PersonalCenter.vue、UserInfo.vue、ResetPwd.vue、Menu.vue ...

2021年6月11日后端(Java)

1.整合Mybatis Plus，生成代码

2.导入jar包

3.编写配置文件，开启mapper接口扫描，添加分页、防全表更新插件

4.创建数据库表，生成代码

2021年6月12日后端(Java)

1.引入Security与Jwt

2.实现用户认证、生成验证码以及验证码认证过滤器

KaptchaConfig、RedisConfig、SecurityConfig、CaptchaFilter、LoginSuccessHandler ...

3.解决跨域问题

CorsConfig

2021年6月14日后端(Java)

1.解决用户身份认证

2.实现密码加密，解决登录过程账户密码从数据库获取问题

2021年6月16日后端(Java)

1.实现管理员的教务管理模块的添加学院、专业、班级、教师、学生的增删查改，并对接前端Vue

2.实现管理员的课程教学模块的课程管理的增删查改，并对接前端Vue

2021年6月17日前后端(Java、Vue)

1.完善管理员模块添加教师、学生并默认创建一个用户，使其能够进行登录，以及处理创建数据表主键存在问题

2.初步实现教师管理模块学生管理子模块的增删查改

3.初步实现教师管理模块教学管理子模块的课程管理的增删查改，并实现前端Vue的课表显示
