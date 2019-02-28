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
            <p>登录</p>
          </div>
        </div>
      </div>
    </div>
    <div class="right-div">
      <div class="form">
        <div class="form-content">
          <div class="input-group"
               @click="$refs.userName.focus()">
            <span>账号</span>
            <input type="text"
                   ref="userName"
                   v-model="username"
                   @change="validateMobile"
                   @blur="validateMobile"
                   placeholder="请输入手机号">
            <span class="notification-error">{{mobileInfo}}</span>
          </div>
          <div class="input-group"
               @click="$refs.password.focus()">
            <span>密码</span>
            <input type="password"
                   ref="password"
                   v-model="password"
                   placeholder="请输入8位以上数字与字母组合"
                   @change="validatePass"
                   @blur="validatePass"
                   @keyup.enter="submitData">
            <span class="notification-error">{{passInfo}}</span>
          </div>
          <button class="btn-block"
                  @click="submitData">登录</button>
          <div class="right-label"><span>没有账号？</span><span @click="$router.push('/register')">免费注册</span></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      username: "",
      password: "",
      mobileInfo: "",
      passInfo: ""
    };
  },
  watch: {
    username: function() {
      this.validateMobile();
    },
    password: function() {
      this.validatePass();
    }
  },
  mounted() {},
  methods: {
    /**
     * 验证手机号
     */
    validateMobile() {
      if (!this.username) {
        this.mobileInfo = "手机号不能为空";
      } else if (!/\d{11}$/.test(this.username) || this.username.length > 11) {
        this.mobileInfo = "手机号格式不正确";
      } else this.mobileInfo = "";
      return Boolean(this.mobileInfo == "");
    },

    /**
     * 验证密码
     */
    validatePass() {
      if (!this.password || !/^[A-Za-z0-9]{8,}$/.test(this.password)) {
        this.passInfo = "请输入8位以上数字与字母组合的密码";
      } else this.passInfo = "";
      return Boolean(this.passInfo == "");
    },

    /**
     * 登录提交数据
     */
    submitData() {
      // 数据验证
      let mobileFlag = this.validateMobile();
      let passFlag = this.validatePass();
      if (mobileFlag && passFlag) {
        // 构造提交参数
        let param = {
          username: this.username,
          password: this.password
        };
        this.$axios
          .post("/user/base/login", this.buildFormData(param))
          .then(result => {
            if (result.data.code == "1001") {
              // 将登录的token存入sessionStorage的ticket中
              sessionStorage.setItem("ticket", result.data.token);
              // 存入store中
              this.$store.dispatch("setUserInfo", result.data.datas);
              this.$router.push("/main");
            }
          });
      }
    }
  }
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
  width: 63.33%;
  background: url("../../assets/images/背景1.png") no-repeat;
  background-size: cover;
}
.left-div > .left-content {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  margin: auto;
  width: 398px;
  height: 556px;
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
  width: 170px;
}
.left-content > div:first-child p {
  margin-top: 62px;
  margin-left: 20px;
  width: 120px;
  font-size: 20px;
  line-height: 1.78;
  font-weight: normal;
  color: #ffffff;
}
.left-content > div:first-child img {
  margin-top: 108px;
}
.left-content > div:last-child {
  width: 228px;
  overflow: hidden;
}
.left-content .circle {
  position: relative;
  margin: 50px 0;
  width: 456px;
  height: 456px;
  border-radius: 50%;
  background: #ffffff;
}
.left-content .circle p {
  margin-top: 180px;
  margin-left: 102px;
  width: 24px;
  font-size: 24px;
  line-height: 2;
  color: #666;
}
.right-div {
  width: 36.67%;
  /* min-width: 398px; */
  background: url("../../assets/images/背景2.png") no-repeat;
  background-size: cover;
}
.right-div .form {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  margin: auto;
  width: 398px;
  height: 456px;
  background: #ffffff;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.21);
  -moz-box-shadow: 0 0 20px rgba(0, 0, 0, 0.21); /* 老的 Firefox */
}
.form .form-content {
  margin-top: 118px;
  margin-left: 15px;
  width: 300px;
}
.form-content .input-group {
  position: relative;
  margin-bottom: 30px;
  padding-left: 10px;
  width: 300px;
  height: 36px;
  line-height: 34px;
  border: solid 1px #cccccc;
  border-radius: 3px;
  box-sizing: border-box;
}
.form-content .input-group span {
  display: inline-block;
  font-size: 14px;
  color: #444444;
}
.form-content .input-group input {
  width: 255px;
  text-indent: 20px;
  border: none;
  outline: none;
  font-size: 14px;
}
.form-content .input-group span.notification-error {
  color: #f56c6c;
  font-size: 12px;
  line-height: 1;
  padding-top: 4px;
  position: absolute;
  top: 100%;
  left: 0;
}
::placeholder {
  color: #bbbbbb;
}
::-webkit-input-placeholder {
  color: #bbbbbb;
}
::-moz-placeholder {
  color: #bbbbbb;
}
.form-content .btn-block {
  margin-top: 20px;
  width: 300px;
  height: 36px;
  background-color: #1e66dc;
  border: none;
  border-radius: 3px;
  font-size: 14px;
  color: #ffffff;
}
.form-content .right-label {
  display: block;
  margin-top: 20px;
  font-size: 12px;
  text-align: right;
  color: #666;
}
.form-content .right-label span:last-child {
  color: #1e66dc;
  text-decoration: underline;
  cursor: pointer;
}
</style>
