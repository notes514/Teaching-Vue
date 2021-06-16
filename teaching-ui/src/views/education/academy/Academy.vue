<template>
  <div>
    <el-form :inline="true">
      <el-form-item>
        <el-input
            v-model="searchForm.name"
            placeholder="名称"
            clearable
        >
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="getAcademyList">搜索</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="addHandle">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-popconfirm title="这是确定批量删除吗？" @confirm="delHandle()">
          <el-button type="danger" slot="reference" :disabled="delBtlState">批量删除</el-button>
        </el-popconfirm>
      </el-form-item>
    </el-form>
    <!-- 学院列表 -->
    <el-table
        ref="multipleTable"
        style="width: 100%;"
        :data="tableData"
        :row-style="{height: '34px'}"
        :header-cell-style="{textAlign: 'center', height: '34px'}"
        :cell-style="{textAlign: 'center', padding: '1px'}"
        tooltip-effect="dark"
        border
        stripe
        @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column
          prop="academyId"
          label="院系编号"
          width="140">
      </el-table-column>
      <el-table-column
          prop="academyName"
          label="院系名称"
          width="160">
      </el-table-column>
      <el-table-column
          prop="academyDean"
          label="院系院长"
          width="140">
      </el-table-column>
      <el-table-column
          prop="officePhone"
          label="办公室电话"
          width="160">
      </el-table-column>
      <el-table-column
          prop="createTime"
          label="创建时间"
          width="200">
      </el-table-column>
      <el-table-column
          prop="icon"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="editHandle(scope.row.academyId)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm title="确定要删除吗？" @confirm="delHandle(scope.row.academyId)">
              <el-button type="text" slot="reference">删除</el-button>
            </el-popconfirm>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[8, 16, 32, 64]"
        :current-page="current"
        :page-size="size"
        :total="total">
    </el-pagination>

    <!-- 新增对话框 -->
    <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="600px"
        :before-close="handleClose">
      <el-form :model="academyForm" :rules="academyFormRules" ref="academyForm" label-width="100px" class="demo-editForm">
        <el-form-item label="院系编号" prop="academyId" label-width="120px">
          <el-input v-model="academyForm.academyId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="院系名称" prop="academyName" label-width="120px">
          <el-input v-model="academyForm.academyName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="院系院长" prop="academyDean" label-width="120px">
          <el-input v-model="academyForm.academyDean" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="办公室电话" prop="officePhone" label-width="120px">
          <el-input v-model="academyForm.officePhone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('academyForm')">{{ this.flag ? '保存' : '创建' }}</el-button>
          <el-button @click="resetForm('academyForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Academy",
  data() {
    return {
      /** 搜索栏 */
      searchForm: {},
      /** 批量删除按钮默认显示状态（默认不显示） */
      delBtlState: true,
      /** 总页数 */
      total: 0,
      /** 每页显示条目 */
      size: 8,
      /** 当前页 */
      current: 1,
      /** 新增编辑弹出框显示状态（默认不显示） */
      dialogVisible: false,
      /** 学院列表数据 */
      tableData: [],
      academyForm: {
      },
      academyFormRules: {
        academyId: [
          {required: true, message: '请输入院系编号', trigger: 'blur'}
        ],
        academyName: [
          {required: true, message: '请输入院系名称', trigger: 'blur'}
        ],
        academyDean: [
          {required: true, message: '请输入院长姓名', trigger: 'blur'}
        ],
        officePhone: [
          {required: true, message: '请输入办公室电话', trigger: 'blur'}
        ]
      },
      /** 批量删除选中 */
      multipleSelection: [],
      /** 新增编辑状态区分 */
      flag: false
    }
  },

  created() {
    this.getAcademyList();
  },

  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },

    /**
     * 勾线学院列表信息
     */
    handleSelectionChange(val) {
      console.log("勾选")
      console.log(val)
      this.multipleSelection = val;
      this.delBtlState = val.length === 0;
    },

    /**
     * 获取总页数
     */
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.size = val;
      this.getAcademyList();
    },

    /**
     * 获取当前页信息
     */
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.current = val;
      this.getAcademyList();
    },

    /**
     * 重置对话框
     */
    resetForm(academyForm) {
      this.$refs[academyForm].resetFields();
      this.dialogVisible = false;
      this.academyForm = {};
    },

    /**
     * 关闭对话框
     */
    handleClose() {
      this.resetForm('academyForm');
    },

    /**
     * 获取所有学院信息
     */
    getAcademyList() {
      this.$axios.get("/academy/list", {
        params: {
          academyName: this.searchForm.name,
          current: this.current,
          size: this.size
        }
      }).then(res => {
        this.tableData = res.data.data.records;
        this.size = res.data.data.size;
        this.current = res.data.data.current;
        this.total = res.data.data.total;
      });
    },

    /**
     * 新增确认添加学院信息
     * @param academyForm formName
     */
    submitForm(academyForm) {
      this.$refs[academyForm].validate((valid) => {
        if (valid) {
          console.log("this.academyForm.academyId");
          console.log(this.academyForm.academyId);
          console.log((this.academyForm.academyId ? 'update' : 'save'));
          this.$axios.post('/academy/' + (this.flag ? 'update' : 'save'), this.academyForm)
          .then(() => {
            this.$message({
              showClose: true,
              message: '操作成功！',
              type: 'success',
              onClose:() => {
                this.getAcademyList()
              }
            });
            this.dialogVisible = false;
            this.resetForm(academyForm);
          });
          this.dialogVisible = false;
          console.log('添加成功！');
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },

    /**
     *
     */
    addHandle() {
      this.dialogVisible = true;
      this.flag = false;
    },

    /**
     * 编辑学院信息
     */
    editHandle(academyId) {
      this.$axios.get('/academy/info/' + academyId).then(res => {
        this.academyForm = res.data.data;
        this.dialogVisible = true;
        this.flag = true;
      });
    },

    /**
     * 批量删除
     * @param academyId 学院编号
     */
    delHandle(academyId) {
      let academyIds = [];
      if (academyId) {
        academyIds.push(academyId);
      } else {
        this.multipleSelection.forEach(row => {
          academyIds.push(row.academyId);
        });
      }
      console.log("academyIds");
      console.log(academyIds);
      this.$axios.post("/academy/delete", academyIds).then(res => {
        this.$message({
          showClose: true,
          message: res.data.msg,
          type: 'success',
          onClose:() => {
            this.getAcademyList()
          }
        });
      });
    }
  }
}
</script>

<style scoped>

.el-pagination {
  float: right;
  margin-top: 22px;
}

</style>