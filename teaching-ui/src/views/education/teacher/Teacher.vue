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
        <el-button @click="getTeacherList">搜索</el-button>
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
          prop="teacherId"
          label="教师编号"
          width="120">
      </el-table-column>
      <el-table-column
          prop="teacherName"
          label="教师姓名"
          width="120">
      </el-table-column>
      <el-table-column
          prop="gender"
          label="性别"
          width="100">
      </el-table-column>
      <el-table-column
          prop="teacherTitle"
          label="教师职称"
          width="120">
      </el-table-column>
      <el-table-column
          prop="academyId"
          label="所属院系编号"
          width="160">
      </el-table-column>
      <el-table-column
          prop="teacherPhone"
          label="教师电话"
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
          <el-button type="text" @click="editHandle(scope.row.teacherId)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm title="确定要删除吗？" @confirm="delHandle(scope.row.teacherId)">
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
      <el-form :model="teacherForm" :rules="teacherFormRules" ref="teacherForm" label-width="100px" class="demo-editForm">
        <el-form-item label="教师编号" prop="teacherId" label-width="120px">
          <el-input v-model="teacherForm.teacherId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="教师姓名" prop="teacherName" label-width="120px">
          <el-input v-model="teacherForm.teacherName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender" label-width="120px">
          <el-input v-model="teacherForm.gender" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="教师职称" prop="teacherTitle" label-width="120px">
          <el-input v-model="teacherForm.teacherTitle" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所属院系编号" prop="academyId" label-width="120px">
          <el-input v-model="teacherForm.academyId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="教师电话" prop="teacherPhone" label-width="120px">
          <el-input v-model="teacherForm.teacherPhone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('teacherForm')">{{ this.flag ? '保存' : '创建' }}</el-button>
          <el-button @click="resetForm('teacherForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Teacher",
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
      teacherForm: {
      },
      teacherFormRules: {
        teacherId: [
          {required: true, message: '请输入教师编号', trigger: 'blur'}
        ],
        teacherName: [
          {required: true, message: '请输入教师姓名', trigger: 'blur'}
        ],
        gender: [
          {required: true, message: '请输入性别', trigger: 'blur'}
        ],
        teacherTitle: [
          {required: true, message: '请输入教师职称', trigger: 'blur'}
        ],
        academyId: [
          {required: true, message: '请输入所属院系编号', trigger: 'blur'}
        ],
        teacherPhone: [
          {required: true, message: '请输入教师电话', trigger: 'blur'}
        ]
      },
      /** 批量删除选中 */
      multipleSelection: [],
      /** 新增编辑状态区分 */
      flag: false
    }
  },

  created() {
    this.getTeacherList();
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
      this.getTeacherList();
    },

    /**
     * 获取当前页信息
     */
    handleCurrentChange(val) {
      this.current = val;
      this.getTeacherList();
    },

    /**
     * 重置对话框
     */
    resetForm(teacherForm) {
      this.$refs[teacherForm].resetFields();
      this.teacherForm = {};
    },

    /**
     * 关闭对话框
     */
    handleClose() {
      this.resetForm('teacherForm');
      this.dialogVisible = false;
    },

    /**
     * 获取所有专业信息
     */
    getTeacherList() {
      this.$axios.get("/teacher/list", {
        params: {
          teacherName: this.searchForm.name,
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
     * @param teacherForm formName
     */
    submitForm(teacherForm) {
      this.$refs[teacherForm].validate((valid) => {
        if (valid) {
          this.$axios.post('/teacher/' + (this.flag ? 'update' : 'save'), this.teacherForm)
              .then(res => {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: 'success',
                  onClose:() => {
                    this.getTeacherList()
                  }
                });
                this.dialogVisible = false;
                this.resetForm(teacherForm);
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
    editHandle(teacherId) {
      this.$axios.get('/teacher/info/' + teacherId).then(res => {
        this.teacherForm = res.data.data;
        this.dialogVisible = true;
        this.flag = true;
      });
    },

    /**
     * 批量删除
     * @param teacherId 教师编号
     */
    delHandle(teacherId) {
      let teacherIds = [];
      if (teacherId) {
        teacherIds.push(teacherId);
      } else {
        this.multipleSelection.forEach(row => {
          teacherIds.push(row.teacherId);
        });
      }
      this.$axios.post("/teacher/delete", teacherIds).then(res => {
        this.$message({
          showClose: true,
          message: res.data.msg,
          type: 'success',
          onClose:() => {
            this.getTeacherList()
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