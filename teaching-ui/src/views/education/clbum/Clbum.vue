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
        <el-button @click="getClbumList">搜索</el-button>
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
    <!-- 专业列表 -->
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
          prop="clbumId"
          label="班级编号"
          width="140">
      </el-table-column>
      <el-table-column
          prop="clbumName"
          label="班级名称"
          width="160">
      </el-table-column>
      <el-table-column
          prop="professionId"
          label="所属专业编号"
          width="140">
      </el-table-column>
      <el-table-column
          prop="clbumCounselor"
          label="班级辅导员"
          width="160">
      </el-table-column>
      <el-table-column
          prop="officePhone"
          label="办公室电话"
          width="160">
      </el-table-column>
      <el-table-column
          prop="createTime"
          label="创建时间"
          width="180">
      </el-table-column>
      <el-table-column
          prop="icon"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="editHandle(scope.row.clbumId)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm title="确定要删除吗？" @confirm="delHandle(scope.row.clbumId)">
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
      <el-form :model="clbumForm" :rules="clbumFormRules" ref="clbumForm" label-width="100px" class="demo-editForm">
        <el-form-item label="班级编号" prop="clbumId" label-width="120px">
          <el-input v-model="clbumForm.clbumId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="班级名称" prop="clbumName" label-width="120px">
          <el-input v-model="clbumForm.clbumName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所属专业编号" prop="professionId" label-width="120px">
          <el-input v-model="clbumForm.professionId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="班级辅导员" prop="clbumCounselor" label-width="120px">
          <el-input v-model="clbumForm.clbumCounselor" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="办公室电话" prop="officePhone" label-width="120px">
          <el-input v-model="clbumForm.officePhone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('clbumForm')">{{ this.flag ? '保存' : '创建' }}</el-button>
          <el-button @click="resetForm('clbumForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Clbum",
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
      /** 专业列表数据 */
      tableData: [],
      clbumForm: {
      },
      clbumFormRules: {
        clbumId: [
          {required: true, message: '请输入班级编号', trigger: 'blur'}
        ],
        clbumName: [
          {required: true, message: '请输入班级名称', trigger: 'blur'}
        ],
        professionId: [
          {required: true, message: '请输入所属专业编号', trigger: 'blur'}
        ],
        clbumCounselor: [
          {required: true, message: '请输入班级辅导员', trigger: 'blur'}
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
    this.getClbumList();
  },

  methods: {
    /**
     * 勾线专业列表信息
     */
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val;
      this.delBtlState = val.length === 0;
    },

    /**
     * 获取总页数
     */
    handleSizeChange(val) {
      this.size = val;
      this.getClbumList();
    },

    /**
     * 获取当前页信息
     */
    handleCurrentChange(val) {
      this.current = val;
      this.getClbumList();
    },

    /**
     * 重置对话框
     */
    resetForm(clbumForm) {
      this.$refs[clbumForm].resetFields();
      this.clbumForm = {};
    },

    /**
     * 关闭对话框
     */
    handleClose() {
      this.resetForm('clbumForm');
      this.dialogVisible = false;
    },

    /**
     * 获取所有专业信息
     */
    getClbumList() {
      this.$axios.get("/clbum/list", {
        params: {
          clbumName: this.searchForm.name,
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
     * 新增确认添加专业信息
     * @param clbumForm formName
     */
    submitForm(clbumForm) {
      this.$refs[clbumForm].validate((valid) => {
        if (valid) {
          this.$axios.post('/clbum/' + (this.flag ? 'update' : 'save'), this.clbumForm)
              .then(res => {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: 'success',
                  onClose:() => {
                    this.getClbumList()
                  }
                });
                this.dialogVisible = false;
                this.resetForm(clbumForm);
              });
          // 关闭弹出框
          this.dialogVisible = false;
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },

    /**
     * 新增专业信息
     */
    addHandle() {
      this.dialogVisible = true;
      this.flag = false;
    },

    /**
     * 编辑专业信息
     */
    editHandle(clbumId) {
      this.$axios.get('/clbum/info/' + clbumId).then(res => {
        this.clbumForm = res.data.data;
        this.dialogVisible = true;
        this.flag = true;
      });
    },

    /**
     * 批量删除
     * @param clbumId 班级编号
     */
    delHandle(clbumId) {
      let clbumIds = [];
      if (clbumId) {
        clbumIds.push(clbumId);
      } else {
        this.multipleSelection.forEach(row => {
          clbumIds.push(row.clbumId);
        });
      }
      this.$axios.post("/clbum/delete", clbumIds).then(res => {
        this.$message({
          showClose: true,
          message: res.data.msg,
          type: 'success',
          onClose:() => {
            this.getClbumList()
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