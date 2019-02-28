<template>
  <div class="wrap">
    <div class="boxshadow authority">
      <p class="title">权限管理</p>

      <div class="contentBox">
        <el-form :model="authForm"
                 ref="authForm"
                 :rules="rules"
                 label-width="100px"
                 class="authForm">
          <el-form-item label="部门名称"
                        prop="partName">
            <el-input v-model.number="authForm.partName"
                      placeholder="请输入部门名称"></el-input>
          </el-form-item>
          <el-form-item label="权限描述"
                        prop="authDesc">
            <el-input type="textarea"
                      v-model.number="authForm.authDesc"
                      placeholder="请输入权限描述"></el-input>
          </el-form-item>
          <el-form-item class="authPart">
            <div class="flex-row">
              <div class="treeNodeContainer">
                <el-tree :data="permissionTreeData"
                         show-checkbox
                         node-key="id"
                         ref="author"
                         :check-strictly="false"
                         :props="defaultProps"
                         @node-click="searchAction"
                         @check="checkMenu">
                </el-tree>

              </div>
              <div class="checkBoxContainer">
                <el-checkbox-group class="flex-col"
                                   v-model="btnList"
                                   @change="changeBtn">

                  <el-checkbox v-for="option in actionList"
                               :key="option.actionId"
                               :label="option.actionId">{{option.actionName}}</el-checkbox>

                </el-checkbox-group>
              </div>
            </div>

          </el-form-item>
          <el-form-item class="authPart">
            <el-button type="primary"
                       @click="saveAuthor()">保存</el-button>
          </el-form-item>
        </el-form>

      </div>
    </div>
  </div>

</template>

<script>
export default {
  data() {
    return {
      roleId: "",
      authForm: {
        authDesc: "",
        partName: ""
      },
      rules: {
        authDesc: [{ required: true, message: "权限描述不能为空" }],
        partName: [{ required: true, message: "部门名称不能为空" }]
      },
      actionList: [],
      btnList: [],
      tempCheckedNode: new Set(),
      permissionTreeData: [
        {
          id: 1,
          checked: "0",
          menuName: "一级 1",
          actionList: [
            { id: "01", actionName: "一级 1增加", checked: "0" },
            { id: "02", actionName: "一级 1删除", checked: "0" },
            { id: "03", actionName: "一级 1修改", checked: "0" },
            { id: "04", actionName: "一级 1查询", checked: "0" }
          ],
          children: [
            {
              id: 4,
              checked: "0",
              menuName: "二级 1-1",
              actionList: [
                { id: "11", actionName: "二级 1-1增加", checked: "0" },
                { id: "12", actionName: "二级 1-1删除", checked: "0" },
                { id: "13", actionName: "二级 1-1修改", checked: "0" },
                { id: "14", actionName: "二级 1-1查询", checked: "0" }
              ],
              children: [
                {
                  id: 9,
                  checked: "1",
                  menuName: "三级 1-1-1",
                  actionList: [
                    { id: "21", actionName: "三级 1-1增加", checked: "0" },
                    { id: "22", actionName: "三级 1-1删除", checked: "0" },
                    { id: "23", actionName: "三级 1-1修改", checked: "0" },
                    { id: "24", actionName: "三级 1-1查询", checked: "0" }
                  ]
                },
                {
                  id: 10,
                  checked: "1",
                  menuName: "三级 1-1-2",
                  actionList: [
                    { id: "31", actionName: "三级 1-1-2增加", checked: "0" },
                    { id: "32", actionName: "三级 1-1-2删除", checked: "0" },
                    { id: "33", actionName: "三级 1-1-2修改", checked: "0" },
                    { id: "34", actionName: "三级 1-1-2查询", checked: "0" }
                  ]
                }
              ]
            }
          ]
        },
        {
          id: 2,
          checked: "0",
          menuName: "一级 2",
          children: [
            {
              id: 5,
              checked: "1",
              menuName: "二级 2-1",
              actionList: [
                { id: 1, actionName: "二级 2-1增加", checked: "0" },
                { id: 2, actionName: "二级 2-1删除", checked: "0" },
                { id: 3, actionName: "二级 2-1修改", checked: "0" },
                { id: 4, actionName: "二级 2-1查询", checked: "0" }
              ]
            },
            {
              id: 6,
              checked: "0",
              menuName: "二级 2-2",
              actionList: [
                { id: 1, actionName: "二级 2-2增加", checked: "0" },
                { id: 2, actionName: "二级 2-2删除", checked: "0" },
                { id: 3, actionName: "二级 2-2修改", checked: "0" },
                { id: 4, actionName: "二级 2-2查询", checked: "0" }
              ]
            }
          ]
        },
        {
          id: 3,
          checked: "0",
          menuName: "一级 3",
          actionList: [
            { id: 1, actionName: "一级 3增加", checked: "0" },
            { id: 2, actionName: "一级 3删除", checked: "0" },
            { id: 3, actionName: "一级 3修改", checked: "0" },
            { id: 4, actionName: "一级 3查询", checked: "0" }
          ],
          children: [
            {
              id: 7,
              checked: "0",
              menuName: "二级 3-1",
              actionList: [
                { id: 1, actionName: "二级 3-1增加", checked: "0" },
                { id: 2, actionName: "二级 3-1删除", checked: "0" },
                { id: 3, actionName: "二级 3-1修改", checked: "0" },
                { id: 4, actionName: "二级 3-1查询", checked: "0" }
              ]
            },
            {
              id: 8,
              checked: "1",
              menuName: "二级 3-2",
              actionList: [
                { id: 1, actionName: "二级 3-2增加", checked: "0" },
                { id: 2, actionName: "二级 3-2删除", checked: "0" },
                { id: 3, actionName: "二级 3-2修改", checked: "0" },
                { id: 4, actionName: "二级 3-2查询", checked: "0" }
              ]
            }
          ]
        }
      ],
      defaultProps: {
        children: "children",
        label: "menuName"
      }
    };
  },
  mounted() {
    this.roleId = location.href.split("?id=")[1] || "";
    this.loadTreeData();
  },
  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    async loadTreeData() {
      let result = await this.$axios.post(
        "/role/findRoleMenuAction",
        this.buildFormData({ roleId: this.roleId || "" })
      );
      if (result.data.code == "1001") {
        this.permissionTreeData = result.data.datas.userMenuTreeList;
        this.authForm.authDesc = result.data.datas.roleDes;
        this.authForm.partName = result.data.datas.roleName;
        this.setDefaultChecked();
      }
    },

    /**
     * 保存权限
     */
    saveAuthor() {
      let _this = this;
      let url = this.roleId
        ? "/role/editRolePermission"
        : "/role/addRolePermission";
      _this.$refs["authForm"].validate(valid => {
        if (valid) {
          let returnValue = {
            roleName: _this.authForm.partName,
            roleDes: _this.authForm.authDesc,
            roleId: _this.roleId || "",
            userRoleMenuRelList: []
          };
          for (let menuAction of _this.permissionTreeData) {
            if (menuAction.checked == "1") {
              let userRoleMenuRel = {
                menuId: menuAction.id,
                userRoleActionRelList: menuAction.actionList.map(function(
                  item
                ) {
                  return {
                    actionId: item
                  };
                })
              };
              returnValue.userRoleMenuRelList.push(userRoleMenuRel);
              for (let subMenu of menuAction.children) {
                if (subMenu.checked == "1") {
                  userRoleMenuRel = {
                    menuId: subMenu.id,
                    userRoleActionRelList: subMenu.actionList
                      .filter(item => item.checked == "1")
                      .map(function(item) {
                        return {
                          actionId: item.actionId
                        };
                      })
                  };
                  returnValue.userRoleMenuRelList.push(userRoleMenuRel);
                }
              }
            }
          }
          _this.$axios
            .post(
              url,
              _this.buildFormData({
                jsonString: JSON.stringify(returnValue)
              })
            )
            .then(result => {
              if (result.data.code == "1001") {
                _this.$message({
                  message: result.data.msg,
                  type: "success"
                });
                _this.$router.push("/userCenter/userInfo");
              }
            });
        } else {
          return false;
        }
      });
    },
    searchAction(data, node, vueComponent) {
      // 获取当前节点
      let currentNode = this.$refs.author.getCurrentNode();
      // 获取当前节点的actionList
      this.actionList = currentNode.actionList || [];
      const menu = this.$refs.author.getNode(currentNode);
      if (!menu.checked) {
        this.actionList.map(item => (item.checked = "0"));
      }
      this.btnList = this.actionList
        .filter(item => item.checked == "1")
        .map(item => item.actionId);
    },

    /**
     * actionList的change事件
     * @param btnArr
     */
    changeBtn(btnArr) {
      let currentNode = this.$refs.author.getCurrentNode();
      let menu = this.$refs.author.getNode(currentNode);
      let store = this.$refs.author.store;
      let node = store.getNode(menu.data.id);
      if (btnArr.length <= 0) {
        node.checked = false;
        node.data.checked = "0";
        for (let items of this.permissionTreeData) {
          for (let item of items.children) {
            if (item.id == menu.data.id) {
              item.checked = "0";
            }
          }
        }
      } else {
        if (menu.parent != null) {
          menu.parent.checked = true;
          menu.parent.data.checked = "1";
        }
        this.$refs.author.setChecked(currentNode, true);
        currentNode.checked = "1";
        let actionList = currentNode.actionList;
        let btnList = this.btnList;
        for (let action of actionList) {
          btnList.includes(action.actionId)
            ? (action.checked = "1")
            : (action.checked = "0");
        }
      }
    },

    /**
     *  当节点被选中时的触发事件
     *  父节点选中，子节点全选
     */
    checkMenu(data, checked) {
      //  设置为当前选中的节点
      let store = this.$refs.author.store;
      let node = store.getNode(data.id);
      let actionList = data.actionList || [];

      store.setCurrentNode(node);
      store.currentNodeKey = data.id;
      node.checked ? (node.data.checked = "1") : (node.data.checked = "0");
      this.$refs.author.$emit("node-click", node.data, node, this.$refs.author);
      if (data.checked == "1") {
        for (let item of actionList) {
          this.btnList.includes(item.actionId)
            ? null
            : this.btnList.push(item.actionId);
        }
      }
      if (node.isLeaf && node.checked) {
        // 如果为子节点且为选中状态，将父节点checked置为1，且将下面所有的action  checked置为1
        //TODO  需要递归将父节点checkeed置为1
        node.parent.data.checked = "1";
        this.updateParentNodeChecked(node);
        for (let action of node.data.actionList) {
          action.checked = "1";
        }
      } else if (node.isLeaf && !node.checked) {
        node.parent.data.checked = "0";
        for (let action of node.data.actionList) {
          action.checked = "0";
        }
      } else if (!node.isLeaf && node.checked) {
        //如果为父节点且为选中状态
        //递归 将子节点及子节点的actionList.checked设为1
        this.changeActionChecked(node.data, "1");
      } else if (!node.isLeaf && !node.checked) {
        //如果为父节点且为非选中状态，
        //递归将子节点及子节点的actionList.checked设为0
        this.changeActionChecked(node.data, "0");
      }
      console.log(this.permissionTreeData);
      this.setDefaultChecked();
    },
    /**
     * 递归修改树节点及子节点的checked为"1"
     * 将上面涉及的节点的actionList也设为"1"
     * @param node 该节点所对应的对象
     * @param state "1"选中，"0"未选中
     */
    updateParentNodeChecked(node) {
      node.data.checked = "1";
      if (node.parent.data.hasOwnProperty("checked")) {
        this.updateParentNodeChecked(node.parent);
      }
    },
    changeActionChecked(node, state) {
      node.checked = state;
      if (node.actionList) {
        for (let item of node.actionList) {
          item.checked = state;
        }
      }
      if (node.children) {
        for (let child of node.children) {
          this.changeActionChecked(child, state);
        }
      }
    },

    /**
     * 通过后台传来的数据设置权限树的默认选中值
     * 可能需要递归
     * this.$refs.author.setCheckedKeys([3])
     * 每次调用前需要先 this.tempCheckedNode.clear()
     *
     */
    setDefaultChecked() {
      this.tempCheckedNode.clear();
      this.getDefaultChecked(this.permissionTreeData);
      //设置预设选中的节点为选中状态
      this.$refs.author.setCheckedKeys(Array.from(this.tempCheckedNode));
    },
    getDefaultChecked(treeData) {
      //递归查询选中的叶子节点,叶子节点选中父节点自选中或半选中状态
      for (let item of treeData) {
        if (item.hasOwnProperty("children") && item.children.length > 0) {
          this.getDefaultChecked(item.children);
        } else if (item.checked == "1") {
          this.tempCheckedNode.add(item.id);
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.wrap {
  padding: 15px;
  width: 100%;
  height: 100%;
  .authority {
    height: calc(100vh - 120px);
    margin-top: -16px;
    position: relative;
    background: #fff;
    .title {
      font-size: 14.7px;
      font-weight: bold;
      padding-left: 20px;
      line-height: 48px;
    }

    .contentBox {
      position: relative;
      margin: 0 auto;
      width: 50vw;
      min-width: 500px;
      height: calc(100vh - 190px);
      overflow-y: scroll;
      flex-wrap: wrap;
      .el-input,
      /deep/ .el-textarea {
        width: 300px;
      }
      .authPart {
        /deep/ .el-form-item__content {
          .treeNodeContainer {
            flex: 1;
          }
          .checkBoxContainer {
            flex: 2;
            padding-left: 24px;
            border-left: 1px solid #ccc;
          }
        }
        .el-button {
          width: 32%;
        }
      }
    }
  }
}
</style>

