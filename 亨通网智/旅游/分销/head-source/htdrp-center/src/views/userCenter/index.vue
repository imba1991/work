<template>
  <div class="container">
    <div class="panel">
      <div class="panel-header">
        <span class="medium-font">基本信息</span>
      </div>
      <div class="panel-body">
        <div class="info-list">
          <el-upload class="img-info"
                     :action="$uploadImgApi"
                     :show-file-list="false"
                     :on-success="handleAvatarSuccess"
                     :before-upload="beforeAvatarUpload">
            <img v-if="userInfo.userPortrait"
                 :src="userInfo.userPortrait"
                 class="avatar">
            <i v-else
               class="iconfont icon-shangchuantupian"></i>
          </el-upload>
          <div>
            <p class="medium-font">{{userInfo.companyName}}<i class="iconfont icon-bianji primary ml-10"
                 @click="configVisible = true"></i></p>
            <label class="mt-10">入驻日期：{{userInfo.registerTime.substr(0,10)}}</label>
          </div>
          <div>
            <span>商户id：{{userInfo.companyId}}</span>
          </div>
          <div><span>账号：{{userInfo.userMobile}}</span><span class="primary ml-10"><i class="iconfont icon-bianji"
                 @click="mobileVisible = true"></i></span></div>
          <div><span>密码：*****</span><span class="primary ml-10"
                  @click="passVisible = true"><i class="iconfont icon-bianji"></i></span></div>
        </div>
        <div class="user-list">
          <div class="item"
               v-for="(item,index) in employeeList"
               :key="index">
            {{item.roleName}}
            <span>{{item.count}}</span>
            人
          </div>
          <a class="more"
             @click="$router.push('/userCenter/userInfo')">详情</a>
        </div>
      </div>
    </div>
    <div class="panel"
         style="min-height:586px;">
      <div class="panel-header">
        <span class="medium-font">收款配置</span>
        <span class="primary link"
              @click="$router.push('/userCenter/receiptConfigRecord')">配置记录</span>
      </div>
      <div class="panel-body">
        <div class="card-list">
          <div class="card"
               v-if="alipayConfig">
            <i class="iconfont icon-iconfontrectangle390"></i>
            <div class="card-body">
              <p class="gray mb-16">支付宝收款账户</p>
              <p>{{alipayConfig.alipayAccount}}</p>
              <p>{{alipayConfig.alipayCompanyName}}</p>
            </div>
            <div class="card-button">
              <p class="primary"
                 @click="$router.push('/userCenter/alipayConfig')">编辑</p>
              <!-- <button class="btn-round active">默认收款</button> -->
            </div>
          </div>
          <div class="card"
               v-else>
            <i class="iconfont icon-iconfontrectangle390"></i>
            <div class="card-body">
              <p class="gray mb-16">支付宝收款账户</p>
              <p>配置好之后你将可以使用支付宝进行收款，应用场景有官网售票收款，后台售票收款，商户预存款收款等。</p>
            </div>
            <div class="card-button">
              <button class="btn-round active"
                      @click="$router.push('/userCenter/alipayConfig')">配置</button>
            </div>
          </div>
          <div class="card"
               v-if="weChatConfig">
            <i class="iconfont icon-weixin"></i>
            <div class="card-body">
              <p class="gray mb-16">微信收款账户</p>
              <p>{{weChatConfig.wxpayAccount}}</p>
              <p>{{weChatConfig.wxpayCompanyName}}</p>
            </div>
            <div class="card-button">
              <p class="primary"
                 @click="$router.push('/userCenter/weChatConfig')">编辑</p>
              <!-- <button class="btn-round">默认收款</button> -->
            </div>
          </div>
          <div class="card"
               v-else>
            <i class="iconfont icon-weixin"></i>
            <div class="card-body">
              <p class="gray mb-16">微信收款账户</p>
              <p>配置好之后你将可以使用微信进行收款，应用场景有微店售票收款，官网售票收款，后台售票收款，商户预存款收款等。</p>
            </div>
            <div class="card-button">
              <button class="btn-round active"
                      @click="$router.push('/userCenter/weChatConfig')">配置</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog title="修改密码"
               :visible.sync="passVisible"
               width="558px">
      <el-form :model="pass.model"
               inline
               ref="passForm"
               :rules="pass.rules"
               label-width="100px">
        <el-form-item label="旧密码："
                      prop="oldPsw">
          <el-input type="password"
                    placeholder="请输入旧密码"
                    v-model="pass.model.oldPsw"></el-input>
        </el-form-item>
        <el-form-item label="新密码："
                      prop="newPsw">
          <el-input type="password"
                    placeholder="请输入新密码"
                    v-model="pass.model.newPsw"></el-input>
        </el-form-item>
        <el-form-item label="确认密码："
                      prop="samePsw">
          <el-input type="password"
                    placeholder="请再次输入新密码"
                    v-model="pass.model.samePsw"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="passVisible = false">取消</el-button>
        <el-button type="primary"
                   @click="savePsw('/user/base/editPsw',pass.model)">确定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="修改账号"
               :visible.sync="mobileVisible"
               width="558px">
      <el-form :model="mobile.model"
               ref="configMobileForm"
               inline
               :rules="mobile.rules"
               label-width="120px">
        <el-form-item label="旧账号：">
          <div class="input-span">{{mobile.model.oldMobile}}</div>
        </el-form-item>
        <el-form-item label="短信验证码："
                      prop="oldMobileAuthCode">
          <el-input placeholder="验证码是6位数字"
                    v-model="mobile.model.oldMobileAuthCode"
                    class="cherify-input"></el-input>
          <button class="btn-medium"
                  type="button"
                  ref="oldMobileAuthCode"
                  @click="sendAuthCode()">获取验证码</button>
        </el-form-item>
        <el-form-item label="新账号："
                      prop="newMobile">
          <el-input type="text"
                    placeholder="请输入手机号作为账户名"
                    v-model="mobile.model.newMobile"></el-input>
        </el-form-item>
        <el-form-item label="短信验证码："
                      prop="newMobileAuthCode">
          <el-input placeholder="验证码是6位数字"
                    v-model="mobile.model.newMobileAuthCode"
                    class="cherify-input"></el-input>
          <button ref="newMobileAuthCode"
                  type="button"
                  class="btn-medium"
                  disabled
                  @click="getAuthCode()">获取验证码</button>
        </el-form-item>
      </el-form>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="mobileVisible = false">取消</el-button>
        <el-button type="primary"
                   @click="saveMobile('/user/base/editUserMobile',mobile.model)">确定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="系统名称"
               :visible.sync="configVisible"
               width="558px">
      <el-form :model="config.model"
               inline
               ref="configNameForm"
               :rules="config.rules"
               label-width="94px">
        <el-form-item label="原系统名称：">
          <div class="input-span">{{userInfo.companyName}}</div>
        </el-form-item>
        <el-form-item label="新系统名称："
                      prop="companyName">
          <el-input placeholder="请输入30字符以内的名称"
                    v-model="config.model.companyName"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="configVisible = false">取消</el-button>
        <el-button type="primary"
                   @click="saveSystemName()">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    var validatPassword = (rule, value, callback) => {
      if (!value) return callback(new Error("密码不能为空"));
      else if (rule.field == "samePsw" && value != this.pass.model.newPsw) {
        return callback(new Error("密码不一致"));
      } else if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(value))
        return callback(new Error("请输入8位以上数字与字母组合的密码"));
      else callback();
    };
    var validateMobile = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("手机号不能为空"));
      } else if (!/\d{11}$/.test(value) || value.length > 11) {
        return callback(new Error("手机号格式不正确"));
      } else callback();
    };
    var validateCherify = (rule, value, callback) => {
      if (!value) return callback(new Error("短信验证码不能为空"));
      else if (value.length > 6 || !/\d{6}$/.test(value))
        return callback(new Error("验证码是6位数字"));
      else callback();
    };
    var validateName = (rule, value, callback) => {
      if (!value) return callback(new Error("新系统名称不能为空"));
      else if (value.length > 30)
        return callback(new Error("请输入30字符以内的名称"));
      else callback();
    };
    return {
      userInfo: {
        registerTime: "",
        userPortrait: ""
      }, // 用户信息
      passVisible: false,
      mobileVisible: false,
      configVisible: false,
      pass: {
        model: {
          newPsw: "",
          oldPsw: "",
          samePsw: ""
        },
        rules: {
          newPsw: [
            {
              required: true,
              validator: validatPassword,
              trigger: ["blur", "change"]
            }
          ],
          oldPsw: [
            {
              required: true,
              validator: validatPassword,
              trigger: ["blur", "change"]
            }
          ],
          samePsw: [
            {
              required: true,
              validator: validatPassword,
              trigger: ["blur", "change"]
            }
          ]
        }
      },
      info: {},
      mobile: {
        model: {
          oldMobile: "",
          oldMobileAuthCode: "",
          newMobile: "",
          newMobileAuthCode: ""
        },
        rules: {
          oldMobileAuthCode: [
            {
              required: true,
              validator: validateCherify,
              trigger: ["blur", "change"]
            }
          ],
          newMobile: [
            {
              required: true,
              validator: validateMobile,
              trigger: ["blur", "change"]
            }
          ],
          newMobileAuthCode: [
            {
              required: true,
              validator: validateCherify,
              trigger: ["blur", "change"]
            }
          ]
        }
      },
      config: {
        model: {
          companyName: ""
        },
        rules: {
          companyName: [
            {
              required: true,
              validator: validateName,
              trigger: ["blur", "change"]
            }
          ]
        }
      },
      employeeList: [],
      alipayConfig: null,
      weChatConfig: null
    };
  },
  watch: {
    "mobile.model.newMobile": function(value) {
      let button = this.$refs.newMobileAuthCode;
      if (button.innerText == "获取验证码") {
        if (!/\d{11}$/.test(value) || value.length > 11) {
          button.disabled = true;
        } else button.disabled = false;
      }
    },
    passVisible: function(val) {
      if (!val) this.$refs.passForm.resetFields();
    },
    mobileVisible: function(val) {
      if (!val) this.$refs.configMobileForm.resetFields();
    },
    configVisible: function(val) {
      if (!val) this.$refs.configNameForm.resetFields();
    }
  },
  mounted() {
    // 获取缓存的用户信息
    this.userInfo = this.$store.getters.getUserInfo;
    // 设置用户账户的手机号
    this.mobile.model.oldMobile = this.userInfo.userMobile;
    // 初始化页面数据
    this.initPageData();
  },
  methods: {
    /**
     * 获取员工统计数据
     */
    getUserData() {
      return this.$axios.post("/user/base/userStatistics");
    },
    /**
     * 获取支付宝配置信息
     */
    getAlipayData() {
      return this.$axios.post("/aplipay/properties/detailsByUser");
    },
    /**
     * 获取微信配置信息
     */
    getWeChatData() {
      return this.$axios.post("/wxPayManage/detailsByUser");
    },
    /**
     * 初始化加载页面数据
     */
    initPageData() {
      let _this = this;
      this.$axios
        .all([this.getUserData(), this.getAlipayData(), this.getWeChatData()])
        .then(
          this.$axios.spread(function(userResult, alipayResult, weChatResult) {
            // 三个请求现在都执行完成
            if (userResult.data.code == 1001) {
              _this.employeeList = userResult.data.datas;
            }
            if (alipayResult.data.code == "1001") {
              _this.alipayConfig = alipayResult.data.datas;
            }
            if (weChatResult.data.code == "1001") {
              _this.weChatConfig = weChatResult.data.datas;
            }
          })
        );
    },

    /**
     * 保存手机号码
     */
    async saveMobile(url, param) {
      let result = await this.$axios.post(url, this.buildFormData(param));
      if (result.data.code == "1001") {
        this.mobileVisible = false;
        this.$message.success("修改账户成功");
        this.userInfo.userMobile = param.newMobile;
        this.$store.dispatch("setUserInfo", this.userInfo);
      }
    },
    /**
     * 保存密码
     */
    savePsw(url, param) {
      this.$refs.passForm.validate(valid => {
        if (valid) {
          this.$axios.post(url, this.buildFormData(param)).then(result => {
            if (result.data.code == "1001") {
              this.$message.success("修改密码成功");
              this.passVisible = false;
            }
          });
        }
      });
    },

    /**
     * 发送短信校验码
     */
    async sendAuthCode(mobile) {
      let result = await this.$axios.post(
        "/user/base/sendAuthCode",
        this.buildFormData({
          mobile: this.userInfo.userMobile
        })
      );
      if (result.data.code == "1001") {
        // 获取发送短信按钮按钮
        let button = this.$refs.oldMobileAuthCode;
        button.disabled = true; // 禁用按钮
        this.$message.success(
          "验证码短信已发送到你的手机上，有效时间为10分钟， 请及时查收。"
        );
        let time = 60; //定义时间变量。用于倒计时用
        let timer = null; //定义一个定时器；
        timer = setInterval(function() {
          ///开启定时器。函数内执行
          button.innerText = `重新获取短信(${time})s`;
          if (time == 0) {
            //判断,当时间值小于等于0的时候
            button.disabled = false; // 激活按钮
            button.innerText = "获取验证码"; //其文本内容变成……获取验证码……
            clearInterval(timer); //清除定时器
          }
          time--; //时间值自减
        }, 1000);
      }
    },

    /**
     * 获取新手机号的验证码
     */
    async getAuthCode() {
      let result = await this.$axios.post(
        "/user/base/getAuthCode",
        this.buildFormData({
          mobile: this.mobile.model.newMobile
        })
      );
      if (result.data.code == "1001") {
        // 获取发送短信按钮按钮
        let button = this.$refs.newMobileAuthCode;
        button.disabled = true; // 禁用按钮
        this.$message.success(
          "验证码短信已发送到你的手机上，有效时间为10分钟， 请及时查收。"
        );
        let time = 60; //定义时间变量。用于倒计时用
        let timer = null; //定义一个定时器；
        timer = setInterval(function() {
          ///开启定时器。函数内执行
          button.innerText = `重新获取短信(${time})s`;
          if (time == 0) {
            //判断,当时间值小于等于0的时候
            button.disabled = false; // 激活按钮
            button.innerText = "获取验证码"; //其文本内容变成……获取验证码……
            clearInterval(timer); //清除定时器
          }
          time--; //时间值自减
        }, 1000);
      }
    },

    /**
     * 上传前事件
     */
    beforeAvatarUpload(file) {
      const isLt5M = file.size / 1024 < 800;
      if (!isLt5M) {
        this.$message.error("上传图片大小不能超过 800KB!");
      }
      return isLt5M;
    },

    /**
     * 成功上传头像
     */
    handleAvatarSuccess(response, file, fileList) {
      if (response.code == 1001) {
        let imageUrl = response.data.url;
        // 上传成功后保存在数据可再同步前台的数据
        this.$axios
          .post(
            "/user/base/editUserPortrait",
            this.buildFormData({ userPortrait: imageUrl })
          )
          .then(result => {
            if (result.data.code == "1001") {
              this.$message.success("上传成功");
              this.userInfo.userPortrait = imageUrl;
              // 存入store
              this.$store.dispatch("setUserInfo", this.userInfo);
            }
          });
      }
    },

    /**
     * 保存系统名称
     */
    saveSystemName() {
      this.$refs.configNameForm.validate(valid => {
        if (valid) {
          this.$axios
            .post(
              "/user/base/editCompanyName",
              this.buildFormData(this.config.model)
            )
            .then(result => {
              if (result.data.code == "1001") {
                this.configVisible = false;
                this.userInfo.companyName = this.config.model.companyName;
                // 存入store
                this.$store.dispatch("setUserInfo", this.userInfo);
              }
            });
        }
      });
    }
  }
};
</script>
<style scoped>
.container {
  padding-top: 15px;
  width: 100%;
  height: 100%;
  font-size: 12px;
  color: #333333;
  box-sizing: border-box;
}
.primary {
  color: #1e66dc;
}
.gray {
  color: #888888;
}
.ml-10 {
  margin-left: 10px;
}
.mb-16 {
  margin-bottom: 16px;
}
.medium-font {
  font-size: 14px;
}
.panel {
  margin-bottom: 10px;
  padding: 0 20px 20px 20px;
  background-color: #ffffff;
  box-shadow: 0px 1px 1.4px 0.6px rgba(0, 0, 0, 0.1);
  border-radius: 3px;
  box-sizing: border-box;
}
.panel > .panel-header {
  padding-top: 9px;
  margin-bottom: 20px;
}
.panel > .panel-header span {
  display: inline-block;
  line-height: 1;
  font-weight: bold;
}

.panel > .panel-header span + span {
  margin-left: 10px;
}

.panel > .panel-body {
  overflow: hidden;
}

.info-list {
  display: flex;
  align-items: center;
  width: 100%;
  height: 98px;
  border-radius: 3px 3px 0px 0px;
  border: solid 1px #e5e5e5;
  color: #444444;
  box-sizing: border-box;
}
.info-list div {
  display: inline-block;
  width: 280px;
}
.info-list div label,
.info-list div span,
.info-list div p {
  line-height: 1;
}
.info-list div label {
  display: block;
  margin-top: 10px;
  color: #888888;
}
.info-list img {
  width: 58px;
  height: 58px;
}
.info-list .img-info {
  margin: 20px;
  width: 58px;
  height: 58px;
  background-color: #f5f5f5;
  border: solid 1px #e5e5e5;
  border-radius: 50%;
  line-height: 58px;
  text-align: center;
  overflow: hidden;
}
.img-info .icon-shangchuantupian {
  font-size: 24px;
}
.user-list {
  position: relative;
  width: 100%;
  height: 54px;
  border-radius: 0 0 3px 3px;
  border: solid 1px #e5e5e5;
  border-top: none;
  box-sizing: border-box;
}
.user-list > .item {
  display: inline-block;
  margin: 13px 0;
  width: 317px;
  line-height: 28px;
  text-align: center;
  color: #999999;
}
.user-list > .item > span {
  display: inline-block;
  font-size: 18px;
  padding: 0 34px;
  color: #444444;
}
.user-list > .item:first-child {
  width: 347px;
  border-right: 1px solid #e5e5e5;
  color: #444444;
}
.user-list a.more {
  position: absolute;
  right: 36px;
  line-height: 54px;
  color: #1e66dc;
}
.card-list .card {
  display: flex;
  align-items: center;
  width: 440px;
  margin-right: 20px;
  padding: 0 20px;
  height: 98px;
  border-radius: 3px;
  border: solid 1px #e5e5e5;
  box-sizing: border-box;
  float: left;
}
.card i {
  display: inline-block;
  font-size: 30px;
}
.icon-iconfontrectangle390 {
  color: #00aaef;
}
.icon-weixin {
  color: #11c21a;
}
.card-body {
  flex: 0 0 272px;
  margin: 0 16px;
}
.card-button {
  flex: 0 0 66px;
}
.card-button p {
  text-align: center;
  margin-bottom: 20px;
}
.el-input {
  width: 300px;
}
.input-span {
  width: 300px;
  text-align: left;
}
.el-input.cherify-input {
  width: 174px;
  margin-right: 6px;
}
button:disabled {
  color: #fff;
  background-color: #909399;
  border-color: #909399;
  cursor: not-allowed;
}
</style>