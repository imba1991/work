<template>
  <div>
    <div class="container boxshadow">
      <div class="list">
        <template v-for="(item,index) in deptList">
          <div class="item"
               :class="item.roleId == param.roleId?'active':''"
               :key="index"
               @click="param.roleId = item.roleId">
            <div class="item-header">
              <p>{{item.roleName}}</p>
              <div>
                <span @click="deleteDept(item.roleId)">删除</span>
                <span @click="$router.push('authority?id='+item.roleId)">权限管理</span>
              </div>
            </div>
            <div class="item-body">
              <p>{{item.roleDes}}</p>
            </div>
          </div>
        </template>
        <div v-show="plusVisible"
             class="item-plus"
             @click="$router.push('authority')"> <i class="iconfont icon-zengjia"></i> 增加</div>
      </div>
      <div class="searchRow">
        <div>
          <button class="btn-medium"
                  @click="showuserDialog()">添加员工</button>
        </div>
        <div>
          <el-input placeholder="员工姓名"
                    v-model="param.userName"
                    style="width:140px"></el-input>
          <button class="btn"
                  v-if="user.model.roleId"
                  @click="searchUser(1)">搜索</button>
          <button class="btn"
                  disabled
                  v-else>搜索</button>
        </div>
      </div>
      <el-table :data="employeeList"
                height="250"
                style="width: 100%">
        <el-table-column label="序号"
                         type="index">
        </el-table-column>
        <el-table-column prop="userMobile"
                         label="员工账号">
        </el-table-column>
        <el-table-column prop="userName"
                         label="员工姓名">
        </el-table-column>
        <el-table-column prop="createTime"
                         label="创建时间">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text"
                       @click="deleteUser(scope.row.userId)">删除</el-button>
            <el-button type="text"
                       @click="showDialog(scope.row)">移动</el-button>
            <el-button type="text"
                       @click="resetPsw(scope.row.userId)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="text-center">
        <el-pagination @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="param.page"
                       :page-sizes="[5, 10, 20, 30, 40]"
                       :page-size="param.size"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="total"></el-pagination>
      </div>
    </div>
    <el-dialog title="添加员工"
               :visible.sync="user.addUserDialog"
               width="558px">
      <el-form :model="user.model"
               inline=""
               ref="userForm"
               :rules="user.rules"
               label-width="120px">
        <el-form-item label="员工账号："
                      prop="userMobile">
          <div>
            <el-input placeholder="请输入手机号，不可更改"
                      v-model="user.model.userMobile"></el-input>
          </div>
          <span class="desc-text">密码默认为 HTWZ123456，请联系用户修改</span>
        </el-form-item>
        <el-form-item label="员工姓名："
                      prop="userName">
          <el-input placeholder="请输入员工姓名"
                    v-model="user.model.userName"
                    c></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="user.addUserDialog = false">取消</el-button>
        <el-button type="primary"
                   @click="addUser">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="移动员工"
               :visible.sync="user.moveUserDialog"
               width="558px">
      <div style="line-height:36px"><span style="margin-right:20px">{{user.data.userName}}</span><span>{{user.data.userMobile}}</span></div>
      <div><span>从【{{user.data.roleName}}】切换部门到</span>
        <el-select v-model="user.data.roleId"
                   placeholder="请选择部门"
                   style="width:150px">
          <el-option v-for="item in deptFilterList"
                     :key="item.roleId"
                     :label="item.roleName"
                     :value="item.roleId">
          </el-option>
        </el-select>
      </div>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="user.moveUserDialog = false">取消</el-button>
        <el-button type="primary"
                   @click="moveUser">确定</el-button>
      </span>
    </el-dialog>
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
    return {
      total: 0,
      param: {
        roleId: "",
        userName: "",
        page: 1,
        size: 10
      },
      employeeList: [], // 员工列表
      deptList: [], // 部门列表
      deptFilterList: [], // 部门过滤列表
      user: {
        addUserDialog: false,
        moveUserDialog: false,
        data: {},
        model: {
          roleId: "",
          userMobile: "",
          userName: ""
        },
        rules: {
          userMobile: [
            {
              required: true,
              validator: validateMobile,
              trigger: ["blur", "change"]
            }
          ],
          userName: [
            {
              required: true,
              message: "请输入员工姓名",
              trigger: ["blur", "change"]
            }
          ]
        }
      }
    };
  },
  watch: {
    "user.addUserDialog": function(val) {
      if (!val) {
        this.$refs.userForm.resetFields();
      }
    },
    "param.roleId": function(val) {
      this.user.model.roleId = val; // 设置添加员工的roleId
      this.searchUser();
    }
  },
  computed: {
    plusVisible: function() {
      return this.deptList.length < 10;
    }
  },
  mounted() {
    this.searchDept();
  },
  methods: {
    /**
     * 搜索部门数据
     * @param page
     */
    async searchDept() {
      let result = await this.$axios.post("/role/findRoleListByCompanyId");
      if (result.data.code == "1001") {
        this.deptList = result.data.datas;
        // 初始化roleId
        if (this.deptList.length) {
          this.param.roleId = this.deptList[0].roleId;
        }
      }
    },

    /**
     * 删除部门
     * @param roleId
     */
    deleteDept(roleId) {
      this.$confirm(
        '<i class="el-icon-warning" style="margin-left: -17px;"></i> 是否确定删除该部门？',
        "提示",
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: "删除",
          cancelButtonText: "取消"
        }
      )
        .then(() => {
          this.$axios
            .post("/role/delete", this.buildFormData({ roleId: roleId }))
            .then(result => {
              if (result.data.code == "1001") {
                this.$message.success("删除成功");
                // 删除员工的roleId数据缓存
                if (this.user.model.roleId == roleId)
                  this.user.model.roleId = "";
                // 刷新role列表
                this.searchDept();
              }
            });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },

    /**
     * 搜索员工数据
     */
    async searchUser(page = "") {
      if (page) this.param.page = page;
      let result = await this.$axios.post(
        "/role/findUserListByRoleId",
        this.buildFormData(this.param)
      );
      if (result.data.code == "1001") {
        this.employeeList = result.data.datas;
        this.total = result.data.count;
      }
    },

    showuserDialog() {
      if (this.user.model.roleId) {
        this.user.addUserDialog = true;
      } else this.$message.error("请选择要添加员工的部门");
    },

    /**
     * 添加员工
     */
    addUser() {
      this.$refs.userForm.validate(valid => {
        if (valid) {
          this.$axios
            .post("/role/addUser", this.buildFormData(this.user.model))
            .then(result => {
              if (result.data.code == "1001") {
                this.$message.success("添加员工成功");
                this.user.addUserDialog = false;
                // 刷新员工table
                this.searchUser();
              }
            });
        }
      });
    },

    /**
     * 显示移动员工的弹窗
     * @param model
     */
    showDialog(model) {
      let roleId = this.user.model.roleId;
      this.user.moveUserDialog = true;
      // 拷贝用户信息
      this.user.data = JSON.parse(JSON.stringify(model));
      // 过滤用户的本部门信息
      this.deptFilterList = this.deptList.filter(
        element => element.roleId != roleId
      );
    },

    /**
     * 移动员工
     */
    async moveUser() {
      let param = {
        userId: this.user.data.userId,
        roleId: this.user.data.roleId
      };
      let result = await this.$axios.post(
        "/role/editUserRole",
        this.buildFormData(param)
      );
      if (result.data.code == "1001") {
        this.$message.success("移动员工成功");
        this.user.moveUserDialog = false;
        // 重置table列表
        this.searchUser();
      }
    },

    /**
     * 删除员工
     * @param userId
     */
    deleteUser(userId) {
      this.$confirm(
        '<i class="el-icon-warning" style="margin-left: -17px;"></i> 是否确定删除该员工？',
        "提示",
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: "删除",
          cancelButtonText: "取消"
        }
      )
        .then(() => {
          this.$axios
            .post("/user/base/delete", this.buildFormData({ userId: userId }))
            .then(result => {
              if (result.data.code == "1001") {
                this.$message.success("删除成功");
                // 重置table列表
                this.searchUser();
              }
            });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },

    /**
     * 重置密码
     * @param userId
     */
    resetPsw(userId) {
      this.$confirm(
        '<i class="el-icon-warning" style="margin-left: -17px"></i> 是否确定重置该员工密码？',
        "提示",
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: "重置",
          cancelButtonText: "取消"
        }
      )
        .then(() => {
          this.$axios
            .post("/user/base/resetPsw", this.buildFormData({ userId: userId }))
            .then(result => {
              if (result.data.code == "1001") {
                this.$message.success(
                  "重置密码成功，请联系用户修改，密码为：HTWZ123456"
                );
              }
            });
        })
        .catch(() => {
          this.$message.info("已取消重置密码");
        });
    },

    /**
     * 修改显示条数
     * @param {*} val
     */
    handleSizeChange(val) {
      this.param.size = val;
      this.search();
    },

    /**
     * 修改当前页数
     * @param {*} val
     */
    handleCurrentChange(val) {
      this.param.page = val;
      this.search();
    }
  }
};
</script>
<style scoped>
.el-table >>> thead,
.el-table >>> th {
  color: #000;
  background: #e5e5e5;
}
.container {
  padding: 20px;
  background: #ffffff;
}
.list {
  display: flex;
  margin-bottom: 20px;
  padding: 20px;
  width: 100%;
  height: 216px;
  overflow-x: scroll;
  overflow-y: hidden;
  box-sizing: border-box;
  border-radius: 3px;
  border: solid 1px #e5e5e5;
}
.list .item,
.list .item-plus {
  flex-shrink: 0;
  margin-right: 20px;
  width: 166px;
  height: 166px;
  border-radius: 3px;
  border: 1px solid #e5e5e5;
  font-size: 12px;
}
.list .item-plus {
  line-height: 166px;
  border: 1px dashed #cccccc;
  font-size: 14px;
  color: #444;
  text-align: center;
  background-color: #f6f6f6;
}
.icon-zengjia {
  font-size: 14px;
}
.item > .item-header {
  padding: 16px 0;
  height: 66px;
  background-color: #f5f5f5;
  border-bottom: solid 1px #e5e5e5;
  text-align: center;
  box-sizing: border-box;
}
.item.active > .item-header {
  background: #e5f0ff;
}
.item > .item-header p {
  font-size: 14px;
  color: #000;
}
.item > .item-header span {
  color: #1e66dc;
  margin: 0 10px;
  cursor: pointer;
}
.item > .item-body {
  padding: 15px 10px;
  color: #444444;
  box-sizing: border-box;
}
.item-body p {
  width: 146px;
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 5;
  overflow: hidden;
}
.searchRow {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 10px;
}
.el-table {
  border: 1px solid #e5e5e5;
  border-radius: 3px;
}
/* 设置marin-left为20 */
.ml-20 {
  margin-left: 20px;
}
.el-form-item .desc-text {
  font-size: 12px;
}
.el-form-item {
  margin-bottom: 30px;
}
.el-form-item .desc-text .primary {
  color: #1e66dc;
}
.el-input {
  width: 300px;
}
.el-input >>> .el-input__inner {
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
.el-form-item >>> .el-form-item__content {
  line-height: 1;
}
</style>
