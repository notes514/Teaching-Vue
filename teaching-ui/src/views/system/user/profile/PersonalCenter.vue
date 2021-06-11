<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <div class="block"><el-avatar :size="100" :src="avatarUrl"></el-avatar></div>
            </div>
            <ul class="list-group">
              <li class="list-group-item">
                <svg-icon v-on:abort="user" />用户名称
                <div class="pull-right">admin</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone" />手机号码
                <div class="pull-right">15888888888</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email" />用户邮箱
                <div class="pull-right">ry@163.com</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree" />所属部门
                <div class="pull-right" >研发部门 / 董事长</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples" />所属角色
                <div class="pull-right">超级管理员</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date" />创建日期
                <div class="pull-right">2020-11-20 19:29:42</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <UserInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <ResetPwd :user="user"/>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import UserInfo from "@/views/system/user/profile/UserInfo";
import ResetPwd from "@/views/system/user/profile/ResetPwd";

export default {
  name: "PersonalCenter",
  components: {UserInfo, ResetPwd},
  data() {
    return {
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo",
      avatarUrl: "http://vue.ruoyi.vip/static/img/profile.473f5971.jpg"
    }
  }
}
</script>

<style scoped>

.app-container {
  padding: 0;
}

.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.text-center {
  display: block;
  text-align: center;
}

.list-group{
  border-left: 0;
  border-right: 0;
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
}

.list-group-item {
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0;
  font-size: 13px;
}

.pull-right {
  float: right!important;
}
</style>