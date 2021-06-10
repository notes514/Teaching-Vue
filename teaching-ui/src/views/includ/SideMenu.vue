<template>
  <el-menu
      default-active="2"
      class="el-menu-vertical-demo"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b">

    <router-link to="/index">
      <el-menu-item index="0-1">
        <template slot="title">
          <i class="el-icon-s-home"></i>
          <span slot="title">首页</span>
        </template>
      </el-menu-item>
    </router-link>
    <el-submenu :index="menu.name + ''" v-for="menu in this.menuList">
      <template slot="title">
        <i :class="menu.icon"></i>
        <span>{{ menu.title }}</span>
      </template>
      <router-link :to="item.path" v-for="item in menu.children">
        <el-menu-item :index="item.name + ''">
          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.title }} </span>
          </template>
        </el-menu-item>
      </router-link>
    </el-submenu>
  </el-menu>
</template>

<script>

export default {
  name: "SideMenu",
  data() {
    return {
    }
  },
  computed:  {
    menuList: {
      get() {
        return this.$store.state.menus.menuList
      }
    }
  },
  created() {
    console.log("this.menuList!!!!!!!!")
    console.log(this.menuList)
  }
}
</script>

<style scoped>
.el-menu-vertical-demo {
  height: 100%;
}

a {
  text-decoration: none;
}
</style>