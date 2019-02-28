<template>
  <div class="interface">
    <div class="commomItem">
      <div class="supplyInterface">
        <div class="itemTitle">供应接口</div>
        <p class="interfaceDetail">
          供应接口是系统为商户提供的将第三方平台同步到系统的工具，
          助力分销商快速拓展了包括景区等直接供应商和ＯＴＡ等第三方供应商商品，
          商户可通过系统直接分销第三方平台商品或景区商品，有效丰富自身商品库。
        </p>
        <div class="contentBox">
          <div class="card" v-for="(item, index) in supplyInterface" :key="index">
            <div class="cardLeft inline-block">
              <div class="cardImg">
                <img :src="item.logoUrl" alt>
              </div>
              <div class="cardDetail">
                <p class="itemTitle margin-bt">{{item.supplierName}}</p>
                <p class="text-gray">{{item.supplierDes}}</p>
              </div>
              <div class="cardConfig" v-if="item.status == -1">
                <p class="margin-bt pointer primary" @click="openVisible = true">开通接口</p>
              </div>
              <div class="cardConfig" v-if="item.status == 0">
                <p class="margin-bt pointer primary" @click="openConfigForm(item.instanceId, 'supply')">配置接口</p>
                <p class="pointer primary" @click="openInterface(item.instanceId, 'supply')">开启接口</p>
              </div>
              <div class="cardConfig" v-if="item.status == 1">
                <p class="margin-bt text-lightBlack">已配置</p>
                <p class="pointer primary" @click="closeInterface(item.instanceId, 'supply')">关闭接口</p>
              </div>
            </div>
            <div class="cardAdd inline-block">+</div>
            <div class="clearfix"></div>
          </div>
        </div>
      </div>
      <div class="distriInterface">
        <div class="itemTitle">分销接口</div>
        <p class="interfaceDetail">
          分销接口是为满足商户多样化营销需求提供的渠道对接功能，商户可通过系统将商品分销给国内主要
          的旅游同业分销平台，ＯＴＡ平台等平台，丰富产品分销渠道，实现一站式营销管理。
        </p>
        <div class="contentBox">
          <div class="card" v-for="(item, index) in dustributeInterface" :key="index">
            <div class="cardLeft inline-block">
              <div class="cardImg">
                <img :src="item.logoUrl" alt>
              </div>
              <div class="cardDetail">
                <p class="itemTitle margin-bt">{{item.distributerName}}</p>
                <p class="text-gray">{{item.distributerDes}}</p>
              </div>
              <div class="cardConfig" v-if="item.status == -1">
                <p class="margin-bt pointer primary" @click="openVisible = true">开通接口</p>
              </div>
              <div class="cardConfig" v-if="item.status == 0">
                <p class="margin-bt pointer primary" @click="openConfigForm(item.instanceId, 'distribute')">配置接口</p>
                <p class="pointer primary" @click="openInterface(item.instanceId, 'distribute')">开启接口</p>
              </div>
              <div class="cardConfig" v-if="item.status == 1">
                <p class="margin-bt text-lightBlack">已配置</p>
                <p class="pointer primary" @click="closeInterface(item.instanceId, 'distribute')">关闭接口</p>
              </div>
            </div>
            <div class="cardAdd inline-block">+</div>
            <div class="clearfix"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- 配置接口dialog -->
    <el-dialog title="配置接口" class="configDialog" :visible.sync="configVisible" width="30%" :before-close="handleClose(configVisible)">
      <el-form label-width="100px" :model="dialogForm" ref="configForm">
        <el-form-item :label="formItem.propertyName" 
                      v-for="(formItem, index) in dialogForm.formGroups" 
                      :key="index"
                      :prop="'formGroups.'+index+'.propertyValue'"
                      :rules="{ required: true, message: formItem.propertyDes, trigger: ['blur', 'change'] }">
          <el-input v-model="formItem.propertyValue" 
                    :maxlength="formItem.maxLength" 
                    :type="formItem.propertyType" 
                    :placeholder="formItem.propertyDes"
                    ></el-input>
        </el-form-item>
      
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="medium" class="button" @click="configVisible = false">取消</el-button>
        <el-button type="primary" size="medium" class="button" @click="configInterface(itemId)">配置</el-button> 
      </span>
    </el-dialog>

    <!-- 开通接口 -->
    <el-dialog title="开通接口" class="openDialog" :visible.sync="openVisible" width="30%" :before-close="handleClose(openVisible)">
      <el-form label-width="80px" :model="openForm">
        <el-form-item label="所需金额">
          <span class="text-red text-lg">￥{{parseFloat(openForm.total).toFixed(2)}}</span>
        </el-form-item>
        <el-form-item label="开通方式">
          <el-radio-group v-model="openForm.payType">
            <el-radio-button label="商户预存款" class="prepaid"></el-radio-button>
            <el-radio-button label="支付宝支付" class="alipay"></el-radio-button>
            <el-radio-button label="微信支付" class="wechat"></el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="medium" class="button" @click="openVisible = false">取消</el-button>
        <el-button type="primary" size="medium" class="button" @click="openVisible = false">支付</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      supplyInterface: [],
      dustributeInterface: [],
      configVisible: false,
      dialogForm:{
        formGroups: [ ],
      },
      itemId: "",
      currentType: "",
      openVisible: false,
      openForm: {
        total: 160,
        payType: "支付宝支付"
      },
      closeVisible: true,
    };
  },
  mounted() {
    this.getSupplyInterface();
    this.getDistributeInterface();
  },
  methods: {
    /**
     * 加载所有的供应接口
     */
    async getSupplyInterface(){
      let _this = this;
      let result = await _this.$axios.post("/supplier/queryAll");

      if(result.data.code == "1001"){
        _this.supplyInterface = result.data.datas;
      }
    },

    /**
     * 加载所有的分销接口
     */
    async getDistributeInterface(){
      let _this = this;
      let result = await _this.$axios.post("/ditributer/queryAll");

      if(result.data.code == "1001"){
        _this.dustributeInterface = result.data.datas;
      }
    },

    /**
     * 关闭弹窗
     */
    handleClose(dialog) {
      dialog = false;
    },

    /**
     * 打开配置接口dialog
     */
    async openConfigForm(instanceId, type){
      let _this = this;
      let url = "";

      if(type == "supply"){
        url = "/supplier/queryInstancePropertyValue";
      } else if(type == "distribute"){
        url = "/ditributer/queryInstancePropertyValue";
      }

      let result = await _this.$axios.post(
        url, 
        _this.buildFormData({instanceId: instanceId}) 
      );

      if(result.data.code == "1001"){
        _this.dialogForm.formGroups = result.data.datas;
        _this.configVisible = true;
        _this.itemId = instanceId;
        _this.currentType = type;

      }
    },

    /**
     * 配置接口
     */
    configInterface(id){
      let _this = this;
      let type = _this.currentType;
      let url = "";
      let obj = {};
      for(let item of _this.dialogForm.formGroups){
        item.id = item.id.toString(); 
        obj[item.id] = item.propertyValue;
      }

      if(type == "supply"){
        url = "/supplier/updateInstance";
      } else if(type == "distribute"){
        url = "/ditributer/updateInstance";
      }
      
      _this.$refs.configForm.validate(function(valid) {
        if (valid) {
          _this.$axios.post(
            url,
            { 
              id: id,
              valueMap: obj,
            }
          ).then(result => {
            _this.dialogForm.formGroups = [];
            _this.configVisible = false;
          })   
        }
      });
    },

    /**
     * 关闭接口
     */
    closeInterface(instanceId, type){
      let _this = this;
      let url = "";

      if(type == "supply"){
        url = "/supplier/closeInstance";
      } else if(type == "distribute"){
        url = "/ditributer/closeInstance"
      }

      _this.$confirm('<i class="el-icon-warning" style="margin-left: -17px;"></i> 是否确定关闭该接口？<br>关闭了该接口之后，所有的商品及订单将取消对接。', '提示', {
          dangerouslyUseHTMLString: true,
          distinguishCancelAndClose: true,
          confirmButtonText: '关闭',
          cancelButtonText: '取消',
          cancelButtonClass: 'button',
        }).then(() => {
          _this.$axios.post(
              url,
              _this.buildFormData({instanceId: instanceId})
            ).then(result => {
              if(result.data.code == "1001"){
                _this.$message.success("接口已关闭");

                if(type == "supply"){
                  _this.getSupplyInterface();
                } else if(type == "distribute"){
                  _this.getDistributeInterface();
                }

              }else if(result.data.code == "1005"){
                _this.$message.warning(result.data.msg);
              }else{
                _this.$message.warning("接口未关闭");
              }
            })
        })
        .catch(action => {
          
        });
    }, 

    /**
     * 开启接口
     */
    openInterface(instanceId, type){
      let _this = this;
      let url = "";

      if(type == "supply"){
        url = "/supplier/openInstance";
      } else if(type == "distribute"){
        url = "/ditributer/openInstance"
      }

      _this.$axios.post( url,
        _this.buildFormData({instanceId: instanceId})
      ).then(result=>{
        if(result.data.code == "1001"){
          _this.$message.success("接口已开启");

          if(type == "supply"){
            _this.getSupplyInterface();
          } else if(type == "distribute"){
            _this.getDistributeInterface();
          }

        }else if(result.data.code == "1005"){
          _this.$message.warning(result.data.msg);
        }else{
          _this.$message.warning("接口开启失败")
        }
      })
    },
  }
};
</script>
<style lang="scss" scoped>
@import "../../../assets/css/childPage.css";
.commomItem {
  padding-bottom: 50px;
}
.itemTitle {
  margin-bottom: 0;
}
.margin-bt {
  margin-bottom: 15px;
}
.interfaceDetail {
  background: #f2f2f2;
  padding: 10px 20px;
  margin-top: 8px;
}
.distriInterface {
  margin-top: 50px;
}
.contentBox {
  flex-wrap: wrap;
  flex-direction: row;
  display: flex;
  justify-content: start;
  .card {
    border-radius: 3px;
    margin: 20px 20px 0 0;
    flex-flow: column wrap;
    margin-bottom: 20px;
    width: 594px;
    .cardLeft {
      width: 89%;
      float: left;
      height: 100%;
      display: flex;
      border: 1px solid #ccc;
      border-right: 0;
      padding: 20px 0 0;
      .cardImg {
        width: 17%;
        text-align: center;
        img {
          margin-top: 10px;
        }
      }
      .cardDetail {
        width: 67%;
      }
      .cardConfig {
        width: 16%;
      }
    }
    .cardAdd {
      padding: 20px 0 0;
      border: 1px dotted #ccc;
      width: 10%;
      float: left;
      height: 100%;
      text-align: center;
      font-size: 32px;
      color: #444;
      background: rgb(246, 246, 246);
      cursor: pointer;
    }
    .nonAdd {
      border: 1px solid #ccc;
      border-left: 0;
      background: transparent;
    }
  }
}
.configDialog{
  /deep/ .el-dialog__body{
    padding-right: 20%;
  }
}

.el-radio-button{
  margin-right: 10px;
}
/deep/ .el-radio-button__inner {
  padding: 15px 10px;
}
/deep/ .el-radio-button__orig-radio:checked+.el-radio-button__inner{
  background: transparent;
}
.prepaid{
  /deep/ .el-radio-button__inner{
    color: #1e66dc;
    font-size: 15px;
    border-radius: 0;
    align-items:center; 
    display: -webkit-flex;
  }
  /deep/ .el-radio-button__inner::before{
    display: inline-block;
    content: "";
    width: 32px;
    height: 20px;
    background: url("../../../assets/images/prepaid.png") no-repeat;
    background-size: auto 100%;
  }
}
.alipay{
  /deep/ .el-radio-button__inner{
    color: rgb(1,170,238);
    font-size: 15px;
    border-radius: 0;
    border-left: 1px solid #dcdfe6;
    align-items:center; 
    display: -webkit-flex;
  }
  /deep/ .el-radio-button__inner::before{
    display: inline-block;
    content: "";
    width: 32px;
    height: 20px;
    background: url("../../../assets/images/alipay.png") no-repeat;
    background-size: auto 100%;
  }
}
.wechat{
  /deep/ .el-radio-button__inner{
    color: rgb(17,193,25);
    font-size: 15px;
    border-radius: 0;
    border-left: 1px solid #dcdfe6;
    align-items:center; 
    display: -webkit-flex;
  }
  /deep/ .el-radio-button__inner::before{
    display: inline-block;
    content: "";
    width: 32px;
    height: 20px;
    background: url("../../../assets/images/wechat.png") no-repeat;
    background-size: auto 100%;
  }
}
</style>
<style scoped>
.interface>>> .el-dialog__footer{
  text-align: center;
}
</style>

