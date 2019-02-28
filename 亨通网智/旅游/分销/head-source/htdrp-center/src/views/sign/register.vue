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
            <p>注册</p>
          </div>
        </div>
      </div>
    </div>
    <div class="right-div">
      <div class="form">
        <el-form :model="model"
                 status-icon
                 :rules="rules"
                 ref="registerForm"
                 label-width="82px"
                 class="demo-ruleForm">
          <el-form-item label="账户名称"
                        prop="userType"
                        class="first-item">
            <div>
              <el-radio-group v-model="model.userType">
                <el-radio :label="item.id"
                          v-for="item in list"
                          :key="item.id">{{item.desc}}</el-radio>
              </el-radio-group>
            </div>
            <span class="description">{{description}}</span>
          </el-form-item>
          <el-form-item label="手机号"
                        prop="userMobile">
            <el-input placeholder="请输入你的手机号作为账户名"
                      v-model="model.userMobile"></el-input>
          </el-form-item>
          <el-form-item label="短信验证码"
                        prop="authCode">
            <el-input placeholder="验证码是6位数字"
                      v-model="model.authCode"
                      class="cherify-input"></el-input>
            <el-button type="primary"
                       size="medium"
                       :disabled="btnDisabled && lockBtn"
                       @click="sendMessage($event)">获取验证码</el-button>
          </el-form-item>
          <el-form-item label="设置密码"
                        prop="userPassword">
            <el-input type="password"
                      placeholder="请输入8位以上数字与字母组合的密码"
                      v-model="model.userPassword"></el-input>
          </el-form-item>
          <el-form-item label="确认密码"
                        prop="comfirmPassword"
                        style="margin-bottom:14px;">
            <el-input type="password"
                      placeholder="请输入8位以上数字与字母组合的密码"
                      v-model="model.comfirmPassword"></el-input>
          </el-form-item>
          <el-form-item label="">
            <label></label>
            <el-checkbox v-model="model.checked">同意相关协议</el-checkbox><span class="primary">《分销系统服务使用协议》</span>
          </el-form-item>
          <div v-if="model.checked"
               class="btn-block"
               @click="submitData()">注册</div>
          <div v-else
               class="btn-block disabled">注册</div>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    var validateMobile = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("手机号不能为空"));
      } else if (!/\d{11}$/.test(value) || value.length > 11) {
        return callback(new Error("手机号格式不正确"));
      } else callback();
    };
    var validatePass = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("密码不能为空"));
      } else if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(value)) {
        return callback(new Error("请输入8位以上数字与字母组合的密码"));
      } else callback();
    };
    var validateCherify = (rule, value, callback) => {
      if (!value) return callback(new Error("短信验证码不能为空"));
      else if (value.length > 6 || !/\d{6}$/.test(value))
        return callback(new Error("验证码是6位数字"));
      else callback();
    };
    var validateComfirmPassword = (rule, value, callback) => {
      if (!value) return callback(new Error("请再次确认密码"));
      else if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(value))
        return callback(new Error("请输入8位以上数字与字母组合的密码"));
      else if (value != this.model.userPassword)
        return callback(new Error("密码不一致"));
      else callback();
    };
    return {
      description: "说明：需要补充个人信息（身份证等）才可以正常经营", // 账户名称描述
      list: [], // 账户类型列表
      btnDisabled: true, // 发送短信按钮是否可用
      lockBtn: true, // 锁定btn是否可点击
      model: {
        userType: 1,
        userMobile: "",
        authCode: "",
        userPassword: "",
        comfirmPassword: "",
        checked: false
      },
      rules: {
        userType: [
          {
            required: true
          }
        ],
        userMobile: [
          {
            required: true,
            validator: validateMobile,
            trigger: ["blur", "change"]
          }
        ],
        userPassword: [
          {
            required: true,
            validator: validatePass,
            trigger: ["blur", "change"]
          }
        ],
        authCode: [
          {
            required: true,
            validator: validateCherify,
            trigger: ["blur", "change"]
          }
        ],
        comfirmPassword: [
          {
            required: true,
            validator: validateComfirmPassword,
            trigger: ["blur", "change"]
          }
        ]
      }
    };
  },
  watch: {
    "model.userMobile": function(val) {
      if (/\d{11}$/.test(val) && val.length == 11) this.btnDisabled = false;
      else this.btnDisabled = true;
    },
    "model.userType": function(val) {
      if (val == 1)
        this.description = "说明：需要补充个人信息（身份证等）才可以正常经营";
      else
        this.description = "说明：需要补充企业信息（营业执照等）才可以正常经营";
    }
  },
  mounted() {
    // 获取账户类型
    this.getUserType();
  },
  methods: {
    /**
     *  获取账户类型
     */
    async getUserType() {
      let result = await this.$axios.post("/user/base/getUserType");
      if (result.data.code == "1001") {
        this.list = result.data.datas;
      }
    },

    /**
     * 发送短信按钮点击事件
     */
    async sendMessage(event) {
      let _this = this;
      // 锁定按钮
      this.btnDisabled = true;
      this.lockBtn = true;
      let result = await this.$axios.post(
        "/user/base/getAuthCode",
        this.buildFormData({
          mobile: this.model.userMobile
        })
      );
      if (result.data.code == "1001") {
        this.$message.success(
          "校验验证码已发送到你的手机上，有效时间为10分钟， 请及时查收。"
        );
        let time = 60; //定义时间变量。用于倒计时用
        let timer = null; //定义一个定时器；
        timer = setInterval(function() {
          ///开启定时器。函数内执行
          event.target.innerHTML = `重新获取短信(${time})s`;
          if (time == 0) {
            //判断,当时间值小于等于0的时候
            _this.btnDisabled = false;
            _this.lockBtn = false;
            event.target.innerHTML = "获取验证码"; //其文本内容变成……点击重新发送……
            clearInterval(timer); //清除定时器
          }
          time--; //时间值自减
        }, 1000);
      } else {
        this.btnDisabled = false;
        this.lockBtn = false;
        event.target.innerHTML = "获取验证码"; //其文本内容变成……点击重新发送……
      }
    },

    /**
     * 注册提交表单数据
     */
    submitData() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.$axios
            .post("/user/base/userRegister", this.buildFormData(this.model))
            .then(result => {
              if (result.data.code == "1001") {
                localStorage.setItem("status", true);
                this.$router.push("/registerResult");
              }
            });
        }
      });
    }
  }
};
</script>

<style scoped>
.primary {
  color: #1e66dc;
  cursor: pointer;
}
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
.left-content .circle p {
  margin-top: 230px;
  margin-left: 127px;
  width: 24px;
  font-family: "MicrosoftYaHei";
  font-size: 24px;
  line-height: 2;
  color: #666;
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
  padding: 70px 21px 74px 67px;
  width: 782px;
  height: 556px;
  background: #ffffff;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.21);
  -moz-box-shadow: 0 0 20px rgba(0, 0, 0, 0.21); /* 老的 Firefox */
  box-sizing: border-box;
}
.el-form-item {
  margin-bottom: 30px;
}
.el-form-item.first-item {
  margin-bottom: 14px;
}
.form-item > label > i {
  color: #e54545;
  margin-right: 4px;
}

.el-form-item >>> .el-form-item__label {
  line-height: 36px;
  font-size: 12px;
  color: #666;
}
.el-form-item.first-item >>> .el-form-item__label {
  line-height: 1;
}
.el-form-item >>> .el-form-item__content {
  line-height: 36px;
}
.el-form-item.first-item >>> .el-form-item__content {
  line-height: 1;
}
.el-form-item .description {
  display: block;
  margin-top: 6px;
  line-height: 1;
  font-size: 12px;
  color: #000;
}
.el-radio >>> .el-radio__label {
  font-size: 12px;
  padding-left: 5px;
}
.el-input {
  width: 300px;
}
.el-input.cherify-input {
  width: 174px;
  margin-right: 6px;
}
.el-checkbox {
  margin-right: 0;
}
/* 校验码行隐藏status-icon */
.el-input.cherify-input >>> .el-input__suffix {
  display: none;
}
.el-input >>> .el-input__suffix {
  margin-right: -35px;
}
.el-button {
  width: 116px;
  font-size: 12px;
  border-radius: 3px;
  padding: 11px 0;
  text-align: center;
}
.btn-block {
  margin-top: 40px;
  width: 300px;
  height: 36px;
  line-height: 36px;
  background-color: #1e66dc;
  border: none;
  border-radius: 3px;
  font-size: 14px;
  text-align: center;
  letter-spacing: 2.8px;
  color: #ffffff;
  cursor: pointer;
}
.btn-block {
  margin-left: 82px;
}
.el-button.is-disabled,
.btn-block.disabled {
  color: #fff;
  background-color: #909399;
  border-color: #909399;
  cursor: not-allowed;
}
.el-checkbox >>> .el-checkbox__label {
  padding-left: 6px;
  font-size: 12px;
  color: #333333;
}
::placeholder {
  color: #a8a8a8;
}
::-webkit-input-placeholder {
  color: #a8a8a8;
}
::-moz-placeholder {
  color: #a8a8a8;
}
</style>
