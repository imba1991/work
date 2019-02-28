<template>
  <div class="container">
    <div class="panel">
      <el-form :model="form.model"
               status-icon
               :rules="form.rules"
               ref="addForm"
               label-width="80px"
               class="demo-ruleForm">
        <el-form-item label="账户类型"
                      prop="userType"
                      class="first-item">
          <el-radio-group v-model="form.model.userType">
            <el-radio :label="1">分销商</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选择分组"
                      prop="ditributerGroupId">
          <el-select v-model="form.model.ditributerGroupId"
                     placeholder="请选择分组">
            <el-option v-for="item in options"
                       :key="item.id"
                       :label="item.ditributerGroupName"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="验证码"
                      prop="cherify">
          <div class="flex-center">
            <el-input v-model="form.model.cherify"
                      class="cherify-input"
                      placeholder="请输入后面的图形验证码进行校验"></el-input>
            <cherify ref="cherifyCode">{{cherify}}</cherify>
          </div>
        </el-form-item>
        <el-form-item label="手机号"
                      prop="userMobile">
          <el-input v-model="form.model.userMobile"
                    placeholder="请输入你的手机号作为账户名"></el-input>
          <p v-if="sw">该账号已在分销系统中存在，可直接<span class="primary"
                  @click="$router.push('/distributionCenter')">分销授权</span></p>
        </el-form-item>
        <el-form-item label="设置密码"
                      prop="userPassword">
          <el-input type="password"
                    v-model="form.model.userPassword"
                    placeholder="请输入8位以上数字与字母组合的密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码"
                      prop="samePsw">
          <el-input type="password"
                    v-model="form.model.samePsw"
                    placeholder="请输入8位以上数字与字母组合的密码"></el-input>
        </el-form-item>
        <button type="button"
                class="btn btn-large"
                @click="save()">保存
        </button>
      </el-form>
    </div>
  </div>
</template>

<script>
import cherify from "@/components/cherify.vue";
export default {
  components: {
    cherify: cherify
  },
  data() {
    var validateMobile = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("请输入你的手机号作为账户名"));
      } else if (!/\d{11}$/.test(value) || value.length > 11) {
        return callback(new Error("手机号格式不正确"));
      } else {
        this.$axios
          .post(
            "/ditributerMerchant/checkDitrMobile",
            this.buildFormData({ userMobile: value })
          )
          .then(result => {
            if (result.data.code == "1001") {
              this.sw = false;
              return callback();
            } else {
              this.sw = true;
              return callback(new Error(""));
            }
          });
      }
    };
    var validatPassword = (rule, value, callback) => {
      if (!value) return callback(new Error("密码不能为空"));
      else if (
        rule.field == "samePsw" &&
        value != this.form.model.userPassword
      ) {
        return callback(new Error("密码不一致"));
      } else if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(value))
        return callback(new Error("请输入8位以上数字与字母组合的密码"));
      else callback();
    };
    var validateCherify = (rule, value, callback) => {
      // 获取验证码
      let code = this.$refs.cherifyCode.code;
      if (!value) {
        return callback(new Error("请输入后面的图形验证码进行校验"));
      } else if (value !== code) {
        return callback(new Error("图形验证码错误"));
      } else callback();
    };
    return {
      sw: false,
      cherify: "swHT",
      form: {
        model: {
          userType: 1,
          cherify: "",
          ditributerGroupId: "",
          userMobile: "",
          userPassword: "",
          samePsw: ""
        },
        rules: {
          userType: [
            {
              required: true,
              message: "请选择账户类型",
              trigger: ["blur", "change"]
            }
          ],
          ditributerGroupId: [
            {
              required: true,
              message: "请选择分组",
              trigger: ["blur", "change"]
            }
          ],
          cherify: [
            {
              required: true,
              validator: validateCherify,
              trigger: ["blur", "change"]
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
      list: [],
      options: [
        {
          ditributerGroupName: "分组1",
          id: "1"
        },
        {
          ditributerGroupName: "分组2",
          id: "2"
        },
        {
          ditributerGroupName: "分组3",
          id: "3"
        }
      ]
    };
  },
  mounted() {
    this.getUserGroup();
  },
  methods: {
    /**
     * 获取分组
     */
    async getUserGroup() {
      let result = await this.$axios.post(
        "/ditributerMerchant/findDitributerGroup"
      );
      if (result.data.code == "1001") {
        this.options = result.data.datas;
      }
    },

    /**
     * 保存
     */
    save() {
      this.$refs.addForm.validate(valid => {
        if (valid) {
          this.$axios
            .post(
              "/ditributerMerchant/addDitributer",
              this.buildFormData(this.form.model)
            )
            .then(result => {
              if (result.data.code == "1001") {
                this.$message.success("添加成功");
              }
            });
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  .panel {
    padding: 20px;
    min-height: calc(100vh - 156px);
    background-color: #ffffff;
    box-shadow: 0px 1px 1.4px 0.6px rgba(0, 0, 0, 0.1);
    border-radius: 3px;
    .el-form {
      width: 548px;
      margin: 100px auto;
      .el-form-item {
        margin-bottom: 30px;
        .el-select {
          width: 300px;
          /deep/ .el-input__suffix {
            display: none;
          }
        }
        .el-input {
          width: 300px;
          /deep/ .el-input__suffix {
            margin-right: -35px;
          }
        }
        /deep/ .el-input__inner {
          -webkit-appearance: none;
          background-color: #fff;
          padding: 0 10px;
          height: 36px;
          line-height: 36px;
          font-size: 14px;
          border-radius: 3px;
          border: 1px solid #e5e5e5;
          color: #000;
        }
        p {
          position: absolute;
          font-size: 12px;
          line-height: 1.6;
          z-index: 100;
          background: #fff;
          .primary {
            color: #1e66dc;
            cursor: pointer;
          }
        }
        .flex-center {
          display: flex;
          align-items: center;
          .cherify-input {
            /deep/ .el-input__suffix {
              display: none;
            }
          }
        }
        /deep/ .el-form-item__label,
        /deep/ .el-radio__label {
          font-size: 12px;
        }
      }
      .btn-large {
        margin-left: 80px;
      }
    }
  }
}
</style>

