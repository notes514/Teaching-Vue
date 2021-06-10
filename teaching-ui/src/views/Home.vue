<template>
  <div class="el-container">
    <el-aside width="200px">
      <SideMenu></SideMenu>
    </el-aside>
    <el-container>
      <el-header>
        <strong>桂电教学管理系统</strong>
        <div class="header-avatar">
          <el-avatar :src="userInfo.avatar"></el-avatar>
          <el-dropdown>
          <span class="el-dropdown-link">
            {{userInfo.username}} <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <router-link :to="{name: 'PersonalCenter'}">个人中心</router-link>
              </el-dropdown-item>
              <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import SideMenu from "@/views/includ/SideMenu";
export default {
  name: "Home",
  components: {
    SideMenu
  },
  data() {
    return {
      userInfo: {
        id: '',
        username: '',
        avatar: ''
      }
    }
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    /**
     * 获取用户信息
     */
    getUserInfo() {
      this.$axios.get("/system/userInfo").then(res => {
        this.userInfo = res.data.data;
      });
    },

    /**
     * 退出登录
     */
    logout() {
      this.$axios.get("/logout").then(() => {
        localStorage.clear();
        sessionStorage.clear();
        this.$store.commit('REMOVE_INFO');
        this.$router.push('/login');
      });
    }
  }
}
</script>

<style scoped>
.el-container {
  margin: 0;
  padding: 0;
  height: 100%;
}

.header-avatar {
  float: right;
  width: 150px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
  color: #FFFFFF;
}

.el-header {
  background-color: #17B3A3;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  line-height: 200px;
}

.el-main {
  color: #333;
  text-align: center;
  /*line-height: 160px;*/
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

a {
  text-decoration: none;
}
</style>