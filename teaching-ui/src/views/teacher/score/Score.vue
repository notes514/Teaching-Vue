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
        <el-button @click="getStudentCursorList">搜索</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="addHandle">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-popconfirm title="这是确定批量删除吗？" @confirm="delHandle(null)">
          <el-button type="danger" slot="reference" :disabled="delBtlState">批量删除</el-button>
        </el-popconfirm>
      </el-form-item>
    </el-form>
    <!-- 专业列表 -->
    <el-table
        ref="multipleTable"
        style="width: 100%;"
        :data="tableData"
        :row-style="{height: '24px'}"
        :header-cell-style="{textAlign: 'center', height: '24px'}"
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
          width="130">
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
          prop="clbumId"
          label="班级"
          width="110">
      </el-table-column>
      <el-table-column
          prop="courseName"
          label="课程名称"
          width="120">
      </el-table-column>
      <el-table-column
          prop="teacherName"
          label="科任教师"
          width="100">
      </el-table-column>
      <el-table-column
          prop="score"
          label="成绩"
          width="100">
      </el-table-column>
      <el-table-column
          prop="tag"
          label="等级 "
          width="100">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.score >= 90" type="success">优秀</el-tag>
          <el-tag size="small" v-else-if="scope.row.score >= 80" type="warning">良好</el-tag>
          <el-tag size="small" v-else-if="scope.row.score >= 60" type="info">合格</el-tag>
          <el-tag size="small" v-else type="danger">不合格</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="updateTime"
          label="更新时间"
          width="180">
      </el-table-column>
      <el-table-column
          prop="icon"
          label="操作">
        <template slot-scope="scope">
            <el-button type="text" @click="editHandle(scope.row)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm title="确定要删除吗？" @confirm="delHandle(scope.row)">
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
      <el-form :model="studentCourseForm" :rules="studentCourseFormRules" ref="studentCourseForm" label-width="100px"
               class="demo-editForm">
        <el-form-item label="课程" prop="teacherId" label-width="60px">
          <el-select v-model="studentCourseForm.courseId" placeholder="请选择" :disabled="selectDisabled">
            <el-option
                v-for="item in scoreCursorList"
                :key="item.courseId"
                :label="item.courseName"
                :value="item.courseId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="班级" prop="clbumId" label-width="60px">
          <el-select v-model="studentCourseForm.clbumId" placeholder="请选择" :disabled="selectDisabled"
                     @change="getStudentList(studentCourseForm.clbumId)">
            <el-option
                v-for="item in scoreClbumList"
                :key="item.clbumId"
                :label="item.clbumName"
                :value="item.clbumId"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学号" prop="studentId" label-width="60px">
          <el-select v-model="studentCourseForm.studentId" placeholder="请选择" :disabled="selectDisabled">
            <el-option
                v-for="item in scoreStudentList"
                :key="item.studentId"
                :label="item.studentId"
                :value="item.studentId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="成绩" prop="score" label-width="60px">
          <el-input v-model="studentCourseForm.score" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('studentCourseForm')">
            {{ this.flag ? '保存' : '创建' }}</el-button>
          <el-button @click="resetForm('studentCourseForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {formatDate} from '@/util/LocalDateUtils';
import qs from "qs";

export default {
  name: "Score",
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
      studentCourseForm: {
        courseId: '',
        teacherId: ''
      },
      studentCourseFormRules: {
        courseId: [
          {required: true, message: '请选择课程', trigger: 'blur'}
        ],
        clbumId: [
          {required: true, message: '请选择班级', trigger: 'blur'}
        ],
        studentId: [
          {required: true, message: '请选择学号', trigger: 'blur'}
        ],
        score: [
          {required: true, message: '请输入成绩', trigger: 'blur'}
        ]
      },
      /** 批量删除选中 */
      multipleSelection: [],
      /** 新增编辑状态区分 */
      flag: false,
      /** 下拉选择器禁用状态，默认为开启 */
      selectDisabled: true,
      scoreCursorList: [],
      cursorValue: '',

      scoreClbumList: [],
      clbumValue: '',

      scoreStudentList: [],
      studentValue: ''
    }
  },

  created() {
    this.getStudentCursorList();
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
     * 勾线专业列表信息
     */
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.delBtlState = val.length === 0;
    },

    /**
     * 获取总页数
     */
    handleSizeChange(val) {
      this.size = val;
      this.getStudentCursorList();
    },

    /**
     * 获取当前页信息
     */
    handleCurrentChange(val) {
      this.current = val;
      this.getStudentCursorList();
    },

    /**
     * 重置对话框
     */
    resetForm(studentCourseForm) {
      this.$refs[studentCourseForm].resetFields();
      this.studentCourseForm = {};
    },

    /**
     * 关闭对话框
     */
    handleClose() {
      this.resetForm('studentCourseForm');
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
    getStudentCursorList() {
      this.$axios.get("/student/course/list", {
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
     * @param studentCourseForm formName
     */
    submitForm(studentCourseForm) {
      this.$refs[studentCourseForm].validate((valid) => {
        if (valid) {
          // 判断teacherName是否为一个对象，是则进行转换
          if (this.studentCourseForm.teacherName instanceof Object) {
            this.studentCourseForm.teacherName = formatDate(this.studentCourseForm.teacherName);
          }

          this.$axios.post('/student/course/' + (this.flag ? 'update' : 'save'), this.studentCourseForm)
              .then(res => {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: 'success',
                  onClose:() => {
                    this.getStudentCursorList()
                  }
                });
                this.dialogVisible = false;
                this.resetForm(studentCourseForm);
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
      this.selectDisabled = false;
      this.getCursorList();
      this.getClbumList();
    },

    /**
     * 获取课程信息
     */
    getCursorList() {
      this.$axios.get("/course/getCourseAll", ).then(res => {
        this.scoreCursorList = res.data.data;
      });
    },

    /**
     * 获取班级信息
     */
    getClbumList() {
      this.$axios.get("/clbum/getClbumAll", ).then(res => {
        this.scoreClbumList = res.data.data;
      });
    },

    /**
     * 获取学生信息
     */
    getStudentList(clbumId) {
      this.$axios.get("/student/getStudentAllByClbumId/" + clbumId).then(res => {
        this.scoreStudentList = res.data.data;
      });

      this.getTeacherByUsername();
    },

    /**
     * 获取教师信息
     */
    getTeacherByUsername() {
      this.$axios.get("/teacher/getTeacherByUsername").then(res => {
        this.studentCourseForm.teacherId = res.data.data.teacherId;
      });
    },

    /**
     * 编辑专业信息
     */
    editHandle(studentCourse) {
      this.$axios.get('/student/course/info', {
        params: {
          courseId: studentCourse.courseId,
          studentId: studentCourse.studentId,
          score: studentCourse.score
        }
      }).then(res => {
        this.studentCourseForm = res.data.data;
        this.dialogVisible = true;
        this.flag = true;
        this.selectDisabled = true;
      });
    },

    /**
     * 批量删除
     * @param studentCourse 学号
     */
    delHandle(studentCourse) {
      let courseIds = [];
      let studentIds = [];
      if (studentCourse) {
        courseIds.push(studentCourse.courseId);
      } else {
        this.multipleSelection.forEach(row => {
          courseIds.push(row.courseId);
        });
      }
      if (studentCourse) {
        studentIds.push(studentCourse.studentId);
      } else {
        this.multipleSelection.forEach(row => {
          studentIds.push(row.studentId);
        });
      }

      console.log("courseIds")
      console.log(courseIds)
      console.log("studentIds")
      console.log(studentIds)

      let formData = new FormData();
      formData.append("courseIds", courseIds);
      formData.append("studentIds", studentIds);

      this.$axios.post("/student/course/delete", formData).then(res => {
        this.$message({
          showClose: true,
          message: res.data.msg,
          type: 'success',
          onClose:() => {
            this.getStudentCursorList()
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