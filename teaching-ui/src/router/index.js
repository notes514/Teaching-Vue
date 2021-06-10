import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Index from '../views/Index.vue'
import Login from '../views/Login.vue'
import axios from 'axios'
import store from '../store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    children: [
      {
        path: '/index',
        name: 'Index',
        component: Index
      },
      {
        path: '/user/profile',
        name: 'PersonalCenter',
        component: () => import('../views/system/user/profile/PersonalCenter.vue')
      },
      // {
      //   path: '/system/user',
      //   name: 'User',
      //   component: () => import('../views/system/user/User.vue')
      // },
      // {
      //   path: '/system/role',
      //   name: 'Role',
      //   component: () => import('../views/ststem/role/Role.vue')
      // },
      // {
      //   path: '/system/menu',
      //   name: 'Menu',
      //   component: () => import('../views/ststem/menu/Menu.vue')
      // },
      // {
      //   path: '/system/dict',
      //   name: 'Dict',
      //   component: () => import('../views/ststem/dict/Dict.vue')
      // },
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {

  let hasRoute = store.state.menus.hasRoutes;

  if (!hasRoute) {
    axios.get("/system/menu/nav", {
      headers: {
        Authorization : localStorage.getItem("token")
      }
    }).then(res => {
      console.log("res.data.data");
      console.log(res.data.data);
      // 获取menuList
      store.commit("setMenuList", res.data.data.nav);
      // 获取用户权限
      store.commit("setAuthoritys", res.data.data.authoritys);

      // 动态绑定路由
      let newRoutes = router.options.routes;
      res.data.data.nav.forEach(menu => {
        if (menu.children) {
          menu.children.forEach(e => {
            // 转成路由
            let route = menuToRoute(e);
            // 把路由添加到路由管理中
            if (route) {
              newRoutes[0].children.push(route);
            }
          });
        }
      });
      // 绑定动态路由
      router.addRoutes(newRoutes);

      // 变更路由状态
      hasRoute = true;
      store.commit("changeRouteStatus", hasRoute);
    });
  }

  next();
});

// 导航转成路由
const menuToRoute = (menu) => {
  if (!menu.component) {
    return null;
  }

  return {
    name: menu.name,
    path: menu.path,
    component: () => import('../views/' + menu.component + '.vue'),
    meta: {
      icon: menu.icon,
      title: menu.title
    }
  };
}

export default router
