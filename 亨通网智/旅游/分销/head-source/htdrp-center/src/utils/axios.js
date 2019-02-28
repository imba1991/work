import axios from 'axios';
import Vue from 'vue';
import router from '../router/index.js';
import {
  Message
} from 'element-ui';

// axios 配置
axios.defaults.withCredentials = true;
axios.defaults.baseURL = "http://10.39.1.50:18002/"; // 张庆
// axios.defaults.baseURL = "http://10.39.1.25:18002/"; // 邱哥
// axios.defaults.baseURL = "http://10.39.1.19:18002/"; // 鲜琳
axios.defaults.headers.common['ticket'] = sessionStorage.getItem('ticket') || null;
axios.defaults.headers.common['Content-Type'] = 'multipart/form-data';
axios.defaults.headers.common['viewPath'] = location.hash.replace('#', "");
axios.defaults.headers['X-Requested-With'] = 'XMLHttpRequest';
let loadinginstace = '';
let timer = null;

// http request 拦截器
axios.interceptors.request.use((config) => {
  clearTimeout(timer);
  timer = setTimeout(function () {
    loadinginstace = new Vue().$loading({
      lock: true,
      text: '加载中...',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
  }, 2000);
  return config;
}, (error) => {
  clearTimeout(timer);
  if (loadinginstace != '') {
    loadinginstace.close();
  }
  return Promise.reject(error);
});

// http response 拦截器
axios.interceptors.response.use((response) => {
  clearTimeout(timer);
  if (loadinginstace != '') {
    loadinginstace.close();
  }
  if (response && String(response.data.code) !== '1001') {
    let message = response.data.msg;

    switch (String(response.data.code)) {
      case '1000':
        responseMessage(message, 'error');
        router.push('/login')
        break;
      case '1002':
        responseMessage(message, 'warning');
        break;
      case '1003':
        responseMessage(message, 'error');
        break;
      case '1004':
        responseMessage(message, 'error');
        break;
      case '1005':
        responseMessage(message, 'error');
        break;
      case '1010':
        responseMessage(message, 'warning');
        break;
      case '9001': // Wechat jumps to authorization page
        let url = window.location.hash;
        window.location.href = response.data.redirectUrl + url + "#wechat_redirect";
        break;
      case '8001': // not login or invalidation
        router.push('/login');
        break;
      case '5001':
        responseMessage(message, 'error');
        break;

    }
  }
  return response;

}, (error) => {
  if (error && error.response) {

    switch (error.response.status) {
      case 400:
        errorNotify('请求错误(400)');
        break;
      case 401:
        errorNotify('未授权，请重新登录(401)');
        router.push('/login')
        break;
      case 403:
        errorNotify('拒绝访问(403)');
        break;
      case 404:
        errorNotify('请求出错(404)');
        break;
      case 408:
        errorNotify('请求超时(408)');
        break;
      case 500:
        errorNotify('服务器错误(500)');
        break;
      case 501:
        errorNotify('服务未实现(501)');
        break;
      case 502:
        errorNotify('网络错误(502)');
        break;
      case 503:
        errorNotify('服务不可用(503)');
        break;
      case 504:
        errorNotify('网络超时(504)');
        break;
      case 505:
        errorNotify('HTTP版本不受支持(505)');
        break;
      default:
        errorNotify('连接出错(' + error.response.status + ')!');
    }
  } else {
    errorNotify('连接服务器失败!');
    // router.push('/login')
  }
  clearTimeout(timer);
  if (loadinginstace != '') {
    loadinginstace.close();
  }

  return Promise.reject(error);
});


function errorNotify(msg) {
  new Vue().$notify.error({
    title: '错误',
    message: msg
  });
}

function responseMessage(msg, type) {
  if (msg !== '') {
    Message({
      message: msg,
      type: type
    });
  }

}

function redirect(url) {
  if (url != null || url != "") {
    top.location.href = url;
  }
}
export default axios;
