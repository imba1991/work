import Vue from './common/vue-rebuild'
import App from './App'
import router from './router'
import axios from './utils/axios'
import ElementUI from 'element-ui'
import './assets/theme/index.css';
import '@/assets/iconfont/iconfont.css'
import './assets/css/style.css';
import store from './store/index'

Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.prototype.$axios = axios;
Vue.prototype.$uploadImgApi = "http://10.39.1.47:9091/upload/ticketType/";

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: {
    App
  },
  template: '<App/>'
})
