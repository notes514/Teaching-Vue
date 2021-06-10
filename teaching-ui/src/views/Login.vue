<template>
  <div class="login">
    <el-form :label-position="labelPosition" :model="loginForm" :rules="rules" ref="loginForm" label-width="100px" class="login-form">
      <h3 class="title">桂电教学管理系统</h3>
      <el-form-item prop="username">
        <el-input class="el-input" type="text" v-model="loginForm.username" auto-complete="off" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码" style="width: 60%"></el-input>
        <el-image class="captchaImg" :src="captchaImg" @click="getCaptcha"></el-image>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0 0 25px 0;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
            :loading="loading"
            size="medium"
            type="primary"
            style="width:100%;"
            @click="submitForm('loginForm')"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2021 guidian.vip All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      labelPosition: 'top',
      loginForm: {
        username: 'notes_514',
        password: '123456',
        code: '11111',
        token: '',
        rememberMe: false
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ]
      },
      loading: false,
      captchaImg: ''
    };
  },
  methods: {
    /** 用户登录 */
    submitForm(loginForm) {
      this.$refs[loginForm].validate((valid) => {
        if (valid) {
          this.$axios.post('/login').then(res => {
            console.log(res.data)
            const jwt = res.headers['authorization']
            // 将jwt存储到应用store中
            this.$store.commit("SET_TOKEN", jwt)
            this.$router.push("/index")
          }).catch(() => {
            this.getCaptcha();
            console.log('error submit!!');
          })
        } else {
          this.getCaptcha();
          console.log('error submit!!');
          return false;
        }
      });
    },

    /** 获取验证码 */
    getCaptcha() {
      const _this = this;
      this.$axios.get("/captcha").then(res => {
        _this.loginForm.token = res.data.data.token;
        _this.captchaImg = res.data.data.captchaImg;
      });
    }
  },
  created() {
    this.getCaptcha();
  }
}
</script>

<style scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}

.title {
  margin: 0 auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
}

.captchaImg {
  width: 35%;
  float: right;
  margin-left: 8px;
  border-radius: 4px;
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial,serif;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>