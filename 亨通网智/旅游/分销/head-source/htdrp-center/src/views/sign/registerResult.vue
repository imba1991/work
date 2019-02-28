<template>
  <div class="container">
    <div class="left-div">
      <div class="left-content">
        <div>
          <p>欢迎来到<br>亨通分销系统</p>
          <img src="../../assets/images/手.png"
               alt="">
        </div>
        <div>
          <div class="circle">
          </div>
        </div>
      </div>
    </div>
    <div class="right-div">
      <div class="form">
        <template v-if="status">
          <p><i class="el-icon-success"></i>恭喜你注册成功</p>
          <button @click="$router.push('/login')">立即登录</button>
          <p>系统将在 <span ref="time">3s</span> 后跳转</p>
        </template>
        <template v-else>
          <p><i class="el-icon-error"></i>很遗憾，注册失败</p>
          <button @click="$router.push('/register')">重新注册</button>
          <p>系统将在 <span ref="time">3s</span> 后跳转</p>
        </template>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      status: true
    };
  },
  beforeDestroy() {
    localStorage.removeItem("status");
  },
  mounted() {
    this.status = localStorage.getItem("status") || true;
    let _this = this;
    let time = 3;
    let timer = null; //定义一个定时器；
    let event = this.$refs.time;
    timer = setInterval(function() {
      ///开启定时器 函数内执行
      time--; //时间值自减
      event.innerHTML = `${time}s`;
      if (time == 0) {
        //判断,当时间值小于等于0的时候
        clearInterval(timer); //清除定时器
        if (_this.status) _this.$router.push("/login");
        else _this.$router.push("/register");
      }
    }, 1000);
  },
  methods: {}
};
</script>

<style scoped>
.left-div,
.right-div {
  position: relative;
  height: 100vh;
  float: left;
  overflow: hidden;
}
.left-div {
  width: 36.67%;
  background: url("../../assets/images/背景3.png") no-repeat;
  background-size: cover;
}
.left-div > .left-content {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  margin: auto;
  width: 398px;
  height: 656px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.21);
  -moz-box-shadow: 0 0 20px rgba(0, 0, 0, 0.21); /* 老的 Firefox */
  background: #1e66dc;
  border-top-left-radius: 3px;
  border-bottom-left-radius: 3px;
}
.left-content div {
  float: left;
}
.left-content > div:first-child {
  width: 120px;
}
.left-content > div:first-child p {
  margin-top: 62px;
  margin-left: 20px;
  width: 120px;
  font-size: 20px;
  line-height: 1.78;
  font-family: "MicrosoftYaHei";
  font-weight: normal;
  color: #ffffff;
}
.left-content > div:first-child img {
  margin-top: 280px;
}
.left-content > div:last-child {
  width: 278px;
  overflow: hidden;
}
.left-content .circle {
  position: relative;
  margin: 50px 0;
  width: 556px;
  height: 556px;
  border-radius: 50%;
  background: #ffffff;
}
.right-div {
  width: 63.33%;
  background: url("../../assets/images/背景4.png") no-repeat;
  background-size: cover;
}
.right-div .form {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  margin: auto;
  padding: 179px 420px 178px 213px;
  width: 782px;
  height: 556px;
  background: #ffffff;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.21);
  -moz-box-shadow: 0 0 20px rgba(0, 0, 0, 0.21); /* 老的 Firefox */
  box-sizing: border-box;
  text-align: center;
}
.form p {
  font-size: 14px;
  color: #333333;
}
.form p i {
  font-size: 28px;
  margin-right: 9px;
}
.form p i.el-icon-success {
  color: #07b851;
}
.form p i.el-icon-error {
  color: #e54545;
}
.form p span {
  color: #e54545;
}
.form button {
  margin: 110px auto 10px auto;
  width: 116px;
  height: 36px;
  line-height: 36px;
  font-size: 14px;
  border-radius: 3px;
  text-align: center;
  border: none;
  border-radius: 3px;
  border: solid 1px #1e66dc;
  background: #ffffff;
  color: #1e66dc;
  letter-spacing: -0.3px;
  outline: none;
  cursor: pointer;
}
.form button:hover {
  background: #1e66dc;
  color: #ffffff;
}
</style>
