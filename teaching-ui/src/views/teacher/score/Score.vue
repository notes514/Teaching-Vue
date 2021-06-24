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
        <el-button @click="getTeacherScoreStudentCourseList">搜索</el-button>
      </el-form-item>
      <el-form-item>
        <el-popconfirm title="这是确定批量删除吗？" @confirm="teacherScoreDelHandle(null)">
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
          width="100">
      </el-table-column>
      <el-table-column
          prop="gender"
          label="性别"
          width="80">
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
          <el-tag size="small" v-else-if="scope.row.score === null" type="info">未更新</el-tag>
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
            <el-button type="text" @click="teacherScoreEditHandle(scope.row)">更新</el-button>
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm title="确定要删除吗？" @confirm="teacherScoreDelHandle(scope.row)">
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
      <el-form :model="teacherStudentScoreForm" :rules="teacherStudentScoreFormRules" ref="teacherStudentScoreForm"
               label-width="100px"
               class="demo-editForm">
        <el-form-item label="课程" prop="courseId" label-width="60px">
          <el-select v-model="teacherStudentScoreForm.courseId" placeholder="请选择" :disabled="selectDisabled">
            <el-option
                :key="teacherStudentScoreForm.courseId"
                :label="teacherStudentScoreForm.courseName"
                :value="teacherStudentScoreForm.courseId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="班级" prop="clbumId" label-width="60px">
          <el-select v-model="teacherStudentScoreForm.clbumId" placeholder="请选择" :disabled="selectDisabled">
            <el-option
                :key="teacherStudentScoreForm.clbumId"
                :label="teacherStudentScoreForm.clbumName"
                :value="teacherStudentScoreForm.clbumId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学号" prop="studentId" label-width="60px">
          <el-select v-model="teacherStudentScoreForm.studentId" placeholder="请选择" :disabled="selectDisabled">
            <el-option
                :key="teacherStudentScoreForm.studentId"
                :label="teacherStudentScoreForm.studentId"
                :value="teacherStudentScoreForm.studentId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="成绩" prop="score" label-width="60px">
          <el-input v-model="teacherStudentScoreForm.score" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('teacherStudentScoreForm')">保存</el-button>
          <el-button @click="resetForm('teacherStudentScoreForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {formatDate} from '@/util/LocalDateUtils';

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
      teacherStudentScoreForm: {
        courseId: '',
        teacherId: ''
      },
      teacherStudentScoreFormRules: {
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
      /** 下拉选择器禁用状态，默认为开启 */
      selectDisabled: true
    }
  },

  created() {
    this.getTeacherScoreStudentCourseList();
  },

  methods: {
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
      this.getTeacherScoreStudentCourseList();
    },

    /**
     * 获取当前页信息
     */
    handleCurrentChange(val) {
      this.current = val;
      this.getTeacherScoreStudentCourseList();
    },

    /**
     * 重置对话框
     */
    resetForm(teacherStudentScoreForm) {
      this.$refs[teacherStudentScoreForm].resetFields();
      this.teacherStudentScoreForm = {};
    },

    /**
     * 关闭对话框
     */
    handleClose() {
      this.resetForm('teacherStudentScoreForm');
      this.dialogVisible = false;
    },

    /**
     * 日期列表格式转化
     */
    dateFormatter(row, column, cellValue){
      let  dateTime = new Date(cellValue);
      let year = dateTime.getFullYear();
      let month = dateTime.getMonth() + 1;
      let day = dateTime.getDate();
      if (month < 10) {
        month = "0" + month;
      }
      if (day < 10) {
        day = "0" + day;
      }
      return year  + '-' + month + '-' + day;
    },

    /**
     * 获取所有课程信息
     */
    getTeacherScoreStudentCourseList() {
      this.$axios.get("/student/course/list", {
        params: {
          studentName: this.searchForm.name,
          current: this.current,
          size: this.size
        }
      }).then(res => {
        this.tableData = res.data.data.records;
        this.total = res.data.data.total;
        this.size = res.data.data.size;
        this.current = res.data.data.current;
      });
    },

    /**
     * 新增确认添加专业信息
     * @param teacherStudentScoreForm formName
     */
    submitForm(teacherStudentScoreForm) {
      this.$refs[teacherStudentScoreForm].validate((valid) => {
        if (valid) {
          // 判断teacherName是否为一个对象，是则进行转换
          if (this.teacherStudentScoreForm.teacherName instanceof Object) {
            this.teacherStudentScoreForm.teacherName = formatDate(this.teacherStudentScoreForm.teacherName);
          }

          let studentCourse = {
            studentId: this.teacherStudentScoreForm.studentId,
            courseId: this.teacherStudentScoreForm.courseId,
            score: this.teacherStudentScoreForm.score
          };

          this.$axios.post('/student/course/updateStudentCourseByScore', studentCourse)
              .then(res => {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: 'success',
                  onClose:() => {
                    this.getTeacherScoreStudentCourseList()
                  }
                });
                this.dialogVisible = false;
                this.resetForm(teacherStudentScoreForm);
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
     * 编辑学生成绩信息
     */
    teacherScoreEditHandle(studentCourse) {
      this.$axios.get('/student/course/info', {
        params: {
          courseId: studentCourse.courseId,
          studentId: studentCourse.studentId
        }
      }).then(res => {
        this.teacherStudentScoreForm = res.data.data;
        console.log("res.data.data");
        console.log(res.data.data);
        this.dialogVisible = true;
        this.selectDisabled = true;
      });
    },

    /**
     * 批量删除
     *
     * @param studentCourse 学号
     */
    teacherScoreDelHandle(studentCourse) {
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

      let formData = new FormData();
      formData.append("courseIds", courseIds);
      formData.append("studentIds", studentIds);

      this.$axios.post("/student/course/delete", formData).then(res => {
        this.$message({
          showClose: true,
          message: res.data.msg,
          type: 'success',
          onClose:() => {
            this.getTeacherScoreStudentCourseList()
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