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

2021年6月20日前后端(Java、Vue)

1.实现教师教学管理模块的成绩更新的增删查改，并对接前端Vue

2.实现教师教学管理模块课表的查询，并对接前端Vue

3.完善教师教学管理模块课程管理的新增和编辑功能，并对接前端Vue

2021年6月22日前后端(Java、Vue)

1.更新数据库tb_course表中的字段信息，完善课程添加

2.新增课程管理、学生选课查询接口

3.实现教学管理模块前后端对接

2021年6月24日前后端(Java、Vue)

1.对教师模块、学生模块的学生选课表添加字段，完善教师和学生对课程的添加和选课功能

2.新增对教师对学生选课表的增删查改、课表查询、成绩更新等

3.实现对学生的课表查询、成绩查询功能

2021年6月26日前后端(Java、Vue)

1.更新数据库课程表字段，实现管理员课程教学模块的必修、选修授课的更改，并实现添加、更新、删除功能

2.完善教师管理模块添加课程、查看课表、更新成绩的增删查改功能，并对接前端Vue

3.实现学生模块网上选课的添加、退选功能以及显示课表和成绩