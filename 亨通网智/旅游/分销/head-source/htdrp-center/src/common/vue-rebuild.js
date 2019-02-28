import Vue from 'vue'
//日期格式化
Vue.prototype.$dateFormate = function (date, fmt) {
  var o = {
    "M+": date.getMonth() + 1, //月份   
    "d+": date.getDate(), //日   
    "h+": date.getHours(), //小时   
    "m+": date.getMinutes(), //分   
    "s+": date.getSeconds(), //秒   
    "q+": Math.floor((date.getMonth() + 3) / 3), //季度   
    "S": date.getMilliseconds() //毫秒   
  };
  if (/(y+)/.test(fmt))
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt))
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
}
/**
 * function buildFormData()
 * 处理{}=>FormData
 * @param {} obj 
 */
Vue.prototype.buildFormData = function (obj) {
  try {
    // 删除无用的或者影响后端存储的key（列如createTime）
    delete obj.createTime;
    delete obj.creator;
  } catch (error) {

  }
  let fm = new FormData();
  for (let key in obj) {
    if (undefined === obj[key] || null == obj[key] || '' === obj[key]) {
      continue;
    }

    if (typeof obj[key] === "string") {
      fm.append(key, obj[key].trim());
    } else {
      fm.append(key, obj[key]);
    }
  }
  return fm;
}
/**权限指令**/
Vue.directive('has', {
  bind: function (el, binding) {
    if (!Vue.prototype.$_has(binding.value)) {
      el.parentNode.removeChild(el);
    }
  }
});
//权限检查方法
Vue.prototype.$_has = function (value) {

  let isExist = false;
  let btnPermStr = sessionStorage.getItem("buttonPermissions");
  console.log(btnPermStr)
  if (btnPermStr == undefined || btnPermStr == null) {
    return false;
  }
  let buttonPerms = JSON.parse(btnPermStr);

  if (buttonPerms[location.href.split("#")[1]].perms.indexOf(value) > -1) {
    isExist = true;
  }

  return isExist;
};
export default Vue;
