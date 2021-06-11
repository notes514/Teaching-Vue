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
        hasRoute: false,
        editableTabsValue: 'Index',
        editableTabs: [{
            title: '首页',
            name: 'Index',
        }]
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

        addTab(state, tab) {
            // 获取index，防止tabs重复添加
            let index = state.editableTabs.findIndex(e => e.name === tab.name);
            if (index === -1) {
                state.editableTabs.push({
                    title: tab.title,
                    name: tab.name,
                });
            }
            state.editableTabsValue = tab.name;
        },

        resetState: (state) => {
            state.menuList = [];
            state.permList = [];
            state.hasRoutes = false;
            state.editableTabsValue = 'Index';
            state.editableTabs = [{
                title: '首页',
                name: 'Index',
            }];
        }
    },
    actions: {
    },
    modules: {
    }
}
