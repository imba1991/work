<template>
  <div>
    <div class="container">
      <p class="title">支付宝收款配置</p>
      <el-steps :active="status"
                align-center>
        <el-step title="收款配置说明"></el-step>
        <el-step title="收款配置"></el-step>
        <el-step title="身份校验"></el-step>
        <el-step title="配置成功"></el-step>
      </el-steps>

      <div v-if="status == 1"
           class="content">
        <p>支付宝收款在系统中的使用场景</p>
        <div class="plates">
          <span class="plate">
            <i class="iconfont icon-chenggong"></i>官网售票收款
          </span>
          <span class="plate">
            <i class="iconfont icon-chenggong"></i>后台售票收款
          </span>
          <span class="plate">
            <i class="iconfont icon-chenggong"></i>商户预存款收款
          </span>
        </div>
        <div class="description">
          <p>你需要准备</p>
          <span>
            1.在支付宝官网申请支付宝账户，并开通【当面付】功能，
            <a>查看教程</a>。
            <br>2.保持支付宝主体与认证主体一致。
            <br>3.并准备好相关的配置参数，
            <a>参数查找教程</a>。
            <br>
            <br>若支付宝主体与认证主体不一致
            <br>1.请出具相关的证明 【
            <a>企业委托书</a>】并加盖公司公章或财务专用章，不能是合同/业务专用章。
            <br>2.上传支付宝的身份证影印件。
          </span>
        </div>
      </div>
      <div v-else-if="status == 2"
           class="content">
        <el-form :model="form.model"
                 :rules="form.aliRules"
                 ref="configForm"
                 label-width="170px">
          <el-form-item label="支付宝账户"
                        prop="alipayAccount">
            <el-input v-model="form.model.alipayAccount"
                      placeholder="请输入支付宝账户"></el-input>
          </el-form-item>
          <el-form-item label="支付宝的认证主体名称"
                        prop="alipayCompanyName">
            <el-input v-model="form.model.alipayCompanyName"
                      placeholder="请输入支付宝认证的主体名称"></el-input>
            <p>
              请输入该账号对应的参数 &nbsp;&nbsp;
              <a>参数查找教程</a>
            </p>
          </el-form-item>
          <el-form-item label="appid"
                        prop="appid">
            <el-input v-model="form.model.appid"></el-input>
          </el-form-item>
          <el-form-item label="publicKey"
                        prop="publicKey">
            <el-input type="textarea"
                      :rows="3"
                      v-model="form.model.publicKey"></el-input>
          </el-form-item>
          <el-form-item label="privateKey"
                        prop="privateKey">
            <el-input type="textarea"
                      :rows="3"
                      v-model="form.model.privateKey"></el-input>
          </el-form-item>
          <el-form-item label="alipayPublicKey"
                        prop="alipayPublicKey">
            <el-input type="textarea"
                      :rows="3"
                      v-model="form.model.alipayPublicKey"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div v-else-if="status == 3"
           class="content">
        <el-form :model="identity.model"
                 :rules="identity.rules"
                 ref="identityForm"
                 label-width="100px">
          <el-form-item label="手机号">
            <span>{{identity.model.mobile}}</span>
          </el-form-item>
          <el-form-item label="短信验证码"
                        prop="authCode">
            <el-input placeholder="验证码是6位数字"
                      v-model="identity.model.authCode"
                      class="cherify-input"></el-input>
            <el-button type="primary"
                       class="large"
                       ref="authCode"
                       :disabled="disableValue"
                       @click="sendMessage(identity.model.mobile)">{{btnText}}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-else-if="status == 4"
           class="content">
        <template v-if="resultStatus">
          <p class="result-title">
            <i class="iconfont icon-chenggong"></i>收款配置成功
          </p>
          <div class="description">
            <p>您还需要做如下配置之后才可以进行正常收款，否则可能导致收款失败。</p>
            <span>
              请在支付宝系统中完成如下操作：
              <br>1.在系统中去开通【当面付】功能。&nbsp;&nbsp;
              <a>查看教程</a>。
              <br>2.建议在系统中下一个测试单，验证金额是否已到收款账户。
            </span>
          </div>
        </template>
        <template v-else>
          <p class="result-title">
            <i class="iconfont icon-jindushibai"></i>收款配置失败
          </p>
          <div>
            <p class="text-center">请核对参数是否正确</p>
          </div>
        </template>
      </div>

      <div class="footer">
        <el-button type="primary"
                   v-if="status >1 && status <4"
                   @click="status -= 1">上一步</el-button>
        <el-button type="primary"
                   v-if="status == 4 && resultStatus"
                   @click="$router.push('/userCenter')">返回</el-button>
        <el-button type="primary"
                   v-else-if="status == 4 && !resultStatus"
                   @click="applyModify">申请修改</el-button>
        <el-button type="primary"
                   v-else
                   @click="nextStep(status)">下一步</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    var validateCherify = (rule, value, callback) => {
      if (!value) return callback(new Error("短信验证码不能为空"));
      else if (value.length > 6 || !/\d{6}$/.test(value))
        return callback(new Error("验证码是6位数字"));
      else callback();
    };
    return {
      status: 1,
      resultStatus: true,
      disableValue: false,
      btnText: "获取验证码",
      form: {
        model: {},
        aliRules: {
          alipayAccount: [
            { required: true, message: "请输入支付宝账户", trigger: "blur" }
          ],
          alipayCompanyName: [
            {
              required: true,
              message: "请输入支付宝认证的主体名称",
              trigger: "blur"
            }
          ],
          appid: [{ required: true, message: "请输入appid", trigger: "blur" }],
          publicKey: [
            { required: true, message: "请输入publicKey", trigger: "blur" }
          ],
          privateKey: [
            { required: true, message: "请输入privateKey", trigger: "blur" }
          ],
          alipayPublicKey: [
            {
              required: true,
              message: "请输入alipayPublicKey",
              trigger: "blur"
            }
          ]
        }
      },
      identity: {
        model: {
          mobile: "",
          authCode: ""
        },
        rules: {
          authCode: [
            {
              required: true,
              validator: validateCherify,
              trigger: ["blur", "change"]
            }
          ]
        }
      }
    };
  },
  mounted() {
    this.getAliConfig();
    this.identity.model.mobile = sessionStorage.getItem("userInfo")
      ? JSON.parse(sessionStorage.getItem("userInfo")).userMainMobile
      : "";
  },
  methods: {
    /**
     * 获取支付宝公众号信息
     */
    getAliConfig() {
      let _this = this;

      _this.$axios.post("/aplipay/properties/detailsByUser").then(result => {
        if (result.data.datas) {
          _this.form.model = result.data.datas;
        }
      });
    },

    /**
     * 发送短信校验码
     */
    sendMessage(mobile) {
      let _this = this;
      _this.$axios
        .post(
          "/user/base/sendAuthCode",
          _this.buildFormData({
            mobile: mobile
          })
        )
        .then(result => {
          if (result.data.code == "1001") {
            // 获取发送短信按钮按钮
            let button = _this.$refs.authCode;
            _this.disableValue = true; // 禁用按钮
            _this.$message.success(
              "验证码短信已发送到你的手机上，有效时间为10分钟， 请及时查收。"
            );
            let time = 60; //定义时间变量。用于倒计时用
            let timer = null; //定义一个定时器；
            timer = setInterval(function() {
              ///开启定时器。函数内执行
              _this.btnText = `重新获取短信(${time})s`;
              if (time == 0) {
                //判断,当时间值小于等于0的时候
                _this.disableValue = false; // 激活按钮
                _this.btnText = "获取验证码"; //其文本内容变成……获取验证码……
                clearInterval(timer); //清除定时器
              }
              time--; //时间值自减
            }, 1000);
          }
        });
    },

    /**
     * 下一步
     */
    nextStep(status) {
      let _this = this;
      if (status == 1) {
        _this.status = 2;
      } else if (status == 2) {
        _this.$refs.configForm.validate(function(valid) {
          if (valid) {
            _this.status = 3;
          }
        });
      } else if (status == 3) {
        _this.$axios
          .post(
            "/user/base/checkAuthCode",
            _this.buildFormData({
              mobile: _this.identity.model.mobile,
              authCode: _this.identity.model.authCode
            })
          )
          .then(result => {
            if (result.data.code == "1001") {
              // 请求成功以后 提交表单数据，并且根据返回值判断是否配置成功

              let url = "";
              if (_this.form.model.id) {
                //判断是否有ID，有ID则为修改收款配置
                url = "/aplipay/properties/update";
              } else {
                // 没有ID则为新增收款配置，
                url = "/aplipay/properties/insert";
              }

              _this.$axios
                .post(url, _this.buildFormData(_this.form.model))
                .then(submitResult => {
                  // 提交表单 并根据返回值判断是否配置成功
                  if (submitResult.data.code == "1001") {
                    _this.resultStatus = true;
                  } else {
                    _this.resultStatus = false;
                  }
                  _this.status = 4;
                });
            }
          });
      }
    },

    /**
     * 申请修改
     */
    applyModify() {
      let _this = this;

      _this.status = 1;
      _this.identity.model.authCode = "";
    }
  }
};
</script>

<style scoped>
.container {
  position: relative;
  min-height: 778px;
  min-width: 540px;
  padding: 10px 20px 20px;
  background: #ffffff;
  color: #444;
}
.title {
  font-size: 14px;
  font-weight: bold;
  color: #333333;
  line-height: 1;
  margin-bottom: 10px;
}
.el-steps {
  width: 65%;
  margin: 0 auto;
}
.content {
  width: 540px;
  margin: 0 auto;
}
.el-steps {
  margin-bottom: 90px;
}
p {
  font-size: 14px;
  line-height: 1;
  color: #000;
}
.text-center {
  text-align: center;
}
p.result-title {
  text-align: center;
  margin: 124px auto 65px auto;
}
a {
  color: #1e66dc;
}
.plates {
  margin: 20px 0 58px 0;
}
.plates .plate {
  display: inline-block;
  width: 160px;
  height: 56px;
  line-height: 56px;
  background-color: #f6f6f6;
  border-radius: 3px;
  border: solid 1px #e5e5e5;
  font-size: 14px;
  text-align: center;
}
.plates .plate + .plate {
  margin-left: 22px;
}
.icon-chenggong {
  color: #07b851;
  margin-right: 10px;
}
.description {
  width: 450px;
  margin: 0 auto;
}
.description p {
  margin-bottom: 14px;
}
.description span {
  display: block;
  font-size: 12px;
  line-height: 2;
}
.el-input,
.el-textarea {
  width: 300px;
}
.el-input.cherify-input {
  width: 174px;
  margin-right: 6px;
}
.el-form-item {
  margin-bottom: 30px;
}
.el-form-item p {
  font-size: 12px;
  line-height: 1;
  margin-top: 6px;
}
.icon-chenggong {
  font-size: 28px;
  color: #07b851;
}
.icon-jindushibai {
  display: inline-block;
  line-height: 1;
  margin-right: 10px;
  font-size: 28px;
  color: #e54545;
}
.footer {
  position: absolute;
  right: 0;
  left: 0;
  bottom: 20px;
  width: 540px;
  margin: auto;
  text-align: center;
}
</style>

