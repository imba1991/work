<template>
  <div class="distributeTab">
    <div class="distributeTop">
      <div class="operateBtn">
        <el-button type="primary">添加分销商</el-button>
        <el-button type="primary">申请合作</el-button>
      </div>
      <div class="searchBar">
        <el-input placeholder="商户id" v-model="searchData.id"></el-input>
        <el-input placeholder="商户主体" v-model="searchData.shop"></el-input>
        <el-button type="primary" @click="search()">搜索</el-button>
      </div>
    </div>
    <div class="distributeBottom">
      <div class="leftNav">
        <el-menu default-active="three">
          <el-menu-item index="ziyou">
            <span slot="title">自由分组</span>
          </el-menu-item>
          <el-menu-item :index="item.id" v-for="(item, index) in group" :key="index">
            <span slot="title">{{item.name}}</span>
          </el-menu-item>
        </el-menu>
        <div class="text-center primary groupManage">
          <el-button type="text" @click="groupManageVisible = true">
            <i class="el-icon-setting"></i> 分组管理
          </el-button>
        </div>
      </div>
      <div class="rightTable">
        <el-table :data="groupData" style="width: 100%">
          <el-table-column label="序号" type="index"></el-table-column>
          <el-table-column prop="id" label="商品id"></el-table-column>
          <el-table-column prop="name" label="商品主体"></el-table-column>
          <el-table-column label="预存款余额">
            <template slot-scope="scope">
              <span>￥{{parseFloat(scope.row.money).toFixed(2)}}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="text" @click="groupChange(scope.row)">更换分组</el-button>
              <el-button type="text" @click="deposit(scope.row)">预存款</el-button>
              <el-button type="text" @click="authorization(scope.row)">授权商品</el-button>
              <el-button type="text" @click="deleteGoods(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="searchData.page"
                    :page-sizes="[10,20, 40, 80]"
                    :page-size="searchData.size"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
        </el-pagination>
      </div>
    </div>
    <!-- 分组管理 -->
    <el-dialog title="分组管理" class="dialog" :visible.sync="groupManageVisible" width="30%" :before-close="handleClose(groupManageVisible)">
      <div class="allGroup">
        <div class="groupItem" v-for="(item, index) in group" :key="index">
          <span>{{index + 1}}</span>
          <el-input v-model="item.name" :disabled="item.delete"></el-input>
          <el-button type="text" :disabled="item.delete" @click="deleteGroup(item, index)">删除</el-button>
          <el-button type="text" v-if="index != 0" :disabled="item.delete" @click="moveUp(index, item)">上移</el-button>
          <el-button type="text" v-if="index != group.length-1" :disabled="item.delete" @click="moveDown(index, item)">下移</el-button>
        </div>
      </div>
      <div class="addGroup">
        <span>新增分组</span>
        <el-input placeholder="请输入分组名称（最多8个字符）" v-model="newGroup"></el-input>
        <el-button type="text" @click="addGroup()">添加</el-button>
      </div>
      <div class="explain"><span>说明：</span>删除分组之后，该分组的商户默认进入到【自由分组】中，但是其在原来分组已授权的商品将不做改变。</div>
      <div slot="footer" class="dialog-footer text-center"> 
        <el-button @click="groupManageVisible = false">取消</el-button>
        <el-button type="primary" @click="groupManage()">确定</el-button>
      </div>
    </el-dialog>
    <!-- 更换分组 -->
    <el-dialog title="更换分组" :visible.sync="changeGroup" width="30%" :before-close="handleClose(changeGroup)">
      <el-form label-width="60px">
        <p class="change">该商户现在【{{nowGroup.group}}】组</p>
        <el-form-item label="新分组">
          <el-select v-model="selectGroup" placeholder="请选择其他分组">
            <el-option v-for="(item, index) in group" :key="index"
                       :label="item.name" 
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer text-center"> 
        <el-button @click="changeGroup = false">取消</el-button>
        <el-button type="primary" @click="configGroup()">配置</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import _clone from "lodash/clone";
export default {
  props: ["activeName"],
  data() {
    return {
      total: 0,
      searchData: {
        id: "",
        shop: "",
        page: 1,
        size: 10,
      },
      group: [
        {
          id: "one",
          name: "驴妈妈分销",
          order: 1,
        }, {
          id: "two",
          name: "同程分销",
          order: 2,
        }, {
          id: "three",
          name: "携程分销",
          order: 3,
        },
      ],
      newGroup: "",
      groupData: [
        {
          id: "21315646",
          name: "78454621",
          money: "213450",
          group: "驴妈妈分销",
          company: "亨通网智"
        }, {
          id: "21315646",
          name: "78454621",
          money: "213450",
          group: "同程分销",
          company: "亨通网智sdgasg"
        }, {
          id: "21315646",
          name: "78454621",
          money: "213450",
          group: "携程分销",
          company: "亨通网智123"
        }
      ],
      groupManageVisible: false,
      nowGroup: {},
      changeGroup: false,
      selectGroup: "",
    };
  },
  mounted() {
    this.search();
  },
  methods: {
    handleSizeChange(val) {
      this.searchData.size = val;
      this.search();
    },
    handleCurrentChange(val) {
      this.searchData.page = val;
      this.search();
    },

    /**
     * 关闭dialog
     */
    handleClose(dialogName){
      dialogName = false;
    },

    /**
     * 查询
     */
    search(){

    },

    /**
     * 更换分组
     */
    groupChange(data){
      this.nowGroup = data;
      this.changeGroup = true;
    },

    /**
     * 预存款
     */
    deposit(data){

    },

    /**
     * 授权商品
     */
    authorization(data){},

    /**
     * 删除
     */
    deleteGoods(data){
      this.$confirm(
        '<i class="el-icon-warning" style="margin-left: -17px;"></i> 是否确认删除该商户？<br>如果删除，则默认与【'
        + data.company +'】解除合作。同时删除相关商品，及相关配置。<br>如果想要重新建立合作关系，需重新申请。<br><span style="color: #f56c6c">*请线下与供应商做商户预存款结算。</span>', 
        '提示', 
        {
          dangerouslyUseHTMLString: true,
          distinguishCancelAndClose: true,
          confirmButtonText: '删除',
          cancelButtonText: '取消',
          cancelButtonClass: 'button',
        }).then(() => {
            
        })
        .catch(action => {
          
        });
    },

    /**
     * 上移
     */
    moveUp(index, item){
      let _this = this;

      let data = Object.assign([],_this.group);
      let preItem = data[index-1];

      data[index-1]=item;
      data[index]=preItem;

      _this.group=data;
      _this.group[index-1].order -= 1;
      _this.group[index].order += 1;
    },

    /**
     * 下移
     */
    moveDown(index, item){
      let _this = this;
      let data = Object.assign([], _this.group);
      let afterItem = data[index+1];

      data[index+1] = item;
      data[index] = afterItem;
      
      _this.group = data;
      _this.group[index].order -= 1;
      _this.group[index+1].order += 1;
    },


    /**
     * 添加分组
     */
    addGroup(){
      let _this = this;
      let obj = {};

      obj.name = _this.newGroup;
      obj.order = _this.group[_this.group.length-1].order + 1;

      _this.group.push(obj);
      _this.newGroup = "";
    },

    /**
     * 保存分组管理修改
     */
    groupManage(){
      let _this = this;
      _this.groupManageVisible = false;
    },

     /**
     * 删除分组
     */
    deleteGroup(item, index){
      let _this = this;

      let data = Object.assign([], _this.group);
      data[index].delete = true;

      _this.group = data;
    },

    /**
     * 更换分组
     */
    configGroup(){
      this.changeGroup = false;
    },
  }
};
</script>
<style lang="scss" scoped>
@import url('../../../assets/css/childPage.css');
.distributeTop {
  display: flex;
  justify-content: space-between;
  .el-input {
    width: 160px;
    margin-right: 8px;
  }
}
.distributeBottom {
  display: flex;
  border: 1px solid #e5e5e5;
  margin-top: 10px;
  .leftNav{
    border-right: 1px solid #e5e5e5;
    width: 14%;
    .el-menu{
      border-right: 0;
      height: 634px;
      padding: 0 25px;
      .el-menu-item {
        text-align: center;
        border-bottom: 1px solid #e9e9e9;
      }
    }
    .groupManage {
      line-height: 54px;
      border-top: 1px solid #e5e5e5;
      .el-icon-setting{
        font-size: 18px;
      }
    }
  }
  .rightTable{
    width: 86%;
    .el-table{
      border-left: 0;
      border-right: 0;
    }
  }
}
.dialog{
  .el-input {
    width: 300px;
    margin-right: 15px;
    margin-left: 10px;
  }
  .groupItem {
    margin-bottom: 20px;
  }
  .addGroup {
    background: #f2f2f2;
    text-align: center;
    padding: 20px;
  }
  .explain {
    margin-top: 10px;
    margin-left: 41px;
    span{
      margin-left: -41px;
    }
  }
}
.change{
  margin-bottom: 20px;
}
</style>

