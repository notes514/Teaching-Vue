<template>
  <div>
    <el-form :inline="true">
      <el-form-item>
        <el-input
            v-model="searchForm.name"
            placeholder="名称"
            clearable>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getStudentList">搜索</el-button>
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
          prop="studentId"
          label="学号"
          width="140">
      </el-table-column>
      <el-table-column
          prop="studentName"
          label="姓名"
          width="80">
      </el-table-column>
      <el-table-column
          prop="gender"
          label="性别"
          width="60">
      </el-table-column>
      <el-table-column
          prop="birthday"
          label="出生日期"
          width="120"
          :formatter="dateFormatter">
      </el-table-column>
      <el-table-column
          prop="nationality"
          label="民族"
          width="60">
      </el-table-column>
      <el-table-column
          prop="studentPhone"
          label="学生电话"
          width="140">
      </el-table-column>
      <el-table-column
          prop="clbumId"
          label="班级编号"
          width="140">
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
          <el-button type="text" @click="editHandle(scope.row.studentId)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm title="确定要删除吗？" @confirm="delHandle(scope.row.studentId)">
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
      <el-form :model="studentForm" :rules="studentFormRules" ref="studentForm" label-width="100px" class="demo-editForm">
        <el-form-item label="学号" prop="studentId" label-width="120px">
          <el-input v-model="studentForm.studentId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学生姓名" prop="studentName" label-width="120px">
          <el-input v-model="studentForm.studentName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender" label-width="120px">
          <el-input v-model="studentForm.gender" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday" label-width="120px">
          <el-date-picker
              v-model="studentForm.birthday"
              type="date"
              format="yyyy-MM-dd"
              size="large"
              placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="民族" prop="nationality" label-width="120px">
          <el-input v-model="studentForm.nationality" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="班级编号" prop="clbumId" label-width="120px">
          <el-input v-model="studentForm.clbumId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学生电话" prop="studentPhone" label-width="120px">
          <el-input v-model="studentForm.studentPhone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('studentForm')">{{ this.flag ? '保存' : '创建' }}</el-button>
          <el-button @click="resetForm('studentForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {formatDate} from '@/util/LocalDateUtils'

export default {
  name: "Students",
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
      studentForm: {
      },
      studentFormRules: {
        studentId: [
          {required: true, message: '请输入学号', trigger: 'blur'}
        ],
        studentName: [
          {required: true, message: '请输入学生姓名', trigger: 'blur'}
        ],
        gender: [
          {required: true, message: '请输入性别', trigger: 'blur'}
        ],
        birthday: [
          {required: true, message: '请选择出生日期', trigger: 'blur'}
        ],
        nationality: [
          {required: true, message: '请输入民族', trigger: 'blur'}
        ],
        clbumId: [
          {required: true, message: '请输入班级编号', trigger: 'blur'}
        ],
        studentPhone: [
          {required: true, message: '请输入学生电话', trigger: 'blur'}
        ]
      },
      /** 批量删除选中 */
      multipleSelection: [],
      /** 新增编辑状态区分 */
      flag: false
    }
  },

  created() {
    this.getStudentList();
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
      this.getStudentList();
    },

    /**
     * 获取当前页信息
     */
    handleCurrentChange(val) {
      this.current = val;
      this.getStudentList();
    },

    /**
     * 重置对话框
     */
    resetForm(studentForm) {
      this.$refs[studentForm].resetFields();
      this.studentForm = {};
    },

    /**
     * 关闭对话框
     */
    handleClose() {
      this.resetForm('studentForm');
      this.dialogVisible = false;
    },

    /**
     * 日期列表格式转化
     */
    dateFormatter(row, column, cellValue){
      let  dateTime = new Date(cellValue);
      let year = dateTime.getFullYear() + '-';
      let month = dateTime.getMonth() + 1 + '-';
      let day = dateTime.getDate();
      return year + month + day;
    },

    /**
     * 获取所有专业信息
     */
    getStudentList() {
      this.$axios.get("/student/list", {
        params: {
          studentName: this.searchForm.name,
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
     * @param studentForm formName
     */
    submitForm(studentForm) {
      this.$refs[studentForm].validate((valid) => {
        if (valid) {
          // 判断birthday是否为一个对象，是则进行转换
          if (this.studentForm.birthday instanceof Object) {
            this.studentForm.birthday = formatDate(this.studentForm.birthday);
          }
          this.$axios.post('/student/' + (this.flag ? 'update' : 'save'), this.studentForm)
              .then(res => {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: 'success',
                  onClose:() => {
                    this.getStudentList()
                  }
                });
                this.dialogVisible = false;
                this.resetForm(studentForm);
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
    editHandle(studentId) {
      this.$axios.get('/student/info/' + studentId).then(res => {
        this.studentForm = res.data.data;
        this.dialogVisible = true;
        this.flag = true;
      });
    },

    /**
     * 批量删除
     * @param studentId 学号
     */
    delHandle(studentId) {
      let studentIds = [];
      if (studentId) {
        studentIds.push(studentId);
      } else {
        this.multipleSelection.forEach(row => {
          studentIds.push(row.studentId);
        });
      }
      this.$axios.post("/student/delete", studentIds).then(res => {
        this.$message({
          showClose: true,
          message: res.data.msg,
          type: 'success',
          onClose:() => {
            this.getStudentList()
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