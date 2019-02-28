import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
const store = new Vuex.Store({
  // 定义状态
  state: {
    userInfo: null
  },
  getters: {
    getUserInfo: (state) => {
      state.userInfo = sessionStorage.getItem("userInfo");
      return JSON.parse(state.userInfo);
    }
  },
  mutations: {
    setUserInfo: (state, userInfo) => {
      state.userInfo = userInfo;
      sessionStorage.setItem("userInfo", JSON.stringify(userInfo));
    }
  },
  actions: {
    setUserInfo: (context, userInfo) => context.commit("setUserInfo", userInfo)
  }
})

export default store
