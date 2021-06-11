import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Element from 'element-ui'
import axios from 'axios'
import VueCropper from 'vue-cropper'

import "element-ui/lib/theme-chalk/index.css"
import "./axios"
import "./globalAccessControl"

Vue.config.productionTip = false
Vue.prototype.$axios = axios
Vue.use(VueCropper)

require("./mock.js")

Vue.use(Element)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
