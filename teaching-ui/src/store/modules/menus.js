import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default {
    state: {
        // 菜单栏数据
        menuList: [],
        // 权限数据
        authoritys: [],
        //
        hasRoute: false
    },
    mutations: {
        setMenuList(state, menus) {
            state.menuList = menus;
        },

        setAuthoritys(state, authoritys) {
            state.authoritys = authoritys;
        },

        changeRouteStatus(state, hasRoutes) {
            state.hasRoutes = hasRoutes
        },
    },
    actions: {
    },
    modules: {
    }
}
