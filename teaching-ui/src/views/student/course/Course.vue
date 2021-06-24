<template>
  <div>
    <el-form :inline="true">
      <el-form-item>
        <el-input
            v-model="searchForm.name"
            placeholder="请输入课程名称"
            clearable>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getCurrentElectiveInfo">搜索</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addHandle">选课</el-button>
      </el-form-item>
    </el-form>
    <!-- 专业列表 -->
    <el-table
        ref="multipleTable"
        style="width: 100%;"
        :data="studentCourseData"
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
          prop="courseId"
          label="课程编号">
      </el-table-column>
      <el-table-column
          prop="courseName"
          label="课程名称">
      </el-table-column>
      <el-table-column
          prop="courseHours"
          label="课程学时">
      </el-table-column>
      <el-table-column
          prop="courseCredit"
          label="课程学分">
      </el-table-column>
      <el-table-column
          prop="courseCategory"
          label="课程类别"
          width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.courseCategory === 0">必修</span>
          <span v-else>选修</span>
        </template>
      </el-table-column>
      <el-table-column
          prop="startTime"
          label="开课时间"
          :formatter="dateFormatter">
      </el-table-column>
      <el-table-column
          prop="endTime"
          label="结课时间"
          :formatter="dateFormatter">
      </el-table-column>
      <el-table-column
          prop="createTime"
          label="创建时间"
          width="200px">
      </el-table-column>
      <el-table-column
          prop="icon"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="editHandle(scope.row.courseId)">编辑</el-button>
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
        title="请选择课程"
        :visible.sync="dialogVisible"
        width="700px"
        :before-close="handleClose">
      <el-table
          :data="tableElectiveCourse"
          :header-cell-style="{textAlign: 'center'}"
          :cell-style="{textAlign: 'center'}"
          height="300"
          style="width: 100%">
        <el-table-column
            prop="courseId"
            label="课程编号">
        </el-table-column>
        <el-table-column
            prop="courseName"
            label="课程名称">
        </el-table-column>
        <el-table-column
            prop="courseHours"
            label="课程学时">
        </el-table-column>
        <el-table-column
            prop="courseCredit"
            label="课程学分">
        </el-table-column>
        <el-table-column
            prop="courseCategory"
            label="课程类别">
          <template slot-scope="scope">
            <span v-if="scope.row.courseCategory === 0">必修课</span>
            <span v-else>选修课</span>
          </template>
        </el-table-column>
        <el-table-column label="选课" width="200px">
          <template slot-scope="scope">
            <el-switch
                style="display: block"
                @change="test(scope.row.courseId, scope.row.selectedState)"
                on-value="1"
                off-value="0"
                v-model="scope.row.selectedState"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="已选"
                inactive-text="未选">
            </el-switch>
          </template>
        </el-table-column>
      </el-table>
      <el-form :model="studentCourseForm" ref="studentCourseForm" class="demo-editForm">
        <el-form-item>
          <el-button type="primary" @click="submitForm('studentCourseForm')">{{ this.studentCourseFlag ? '保存' : '确定' }}</el-button>
          <el-button @click="resetForm('studentCourseForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "Course",
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
      studentCourseData: [{
        selectedState: false
      }],
      tableElectiveCourse: [{
        selectedState: false
      }],
      studentCourseForm: {
        teacherId: ''
      },
      /** 批量删除选中 */
      multipleSelection: [],
      /** 新增编辑状态区分 */
      studentCourseFlag: false,
      /** 班级集合 */
      courseClbums: [],
      /** 课程节数集合 */
      courseSections: [{
        value: '1-2节',
        label: '1-2节'
      }, {
        value: '3-4节',
        label: '3-4节'
      }, {
        value: '5-6节',
        label: '5-6节'
      }, {
        value: '7-8节',
        label: '7-8节'
      }, {
        value: '9-10节',
        label: '9-10节'
      }],
      /** 课程周天集合 */
      courseWhichDays: [{
        value: '1',
        label: '星期一'
      }, {
        value: '2',
        label: '星期二'
      }, {
        value: '3',
        label: '星期三'
      }, {
        value: '4',
        label: '星期四'
      }, {
        value: '5',
        label: '星期五'
      }, {
        value: '6',
        label: '星期六'
      }, {
        value: '7',
        label: '星期日'
      }],

      courses: []
    }
  },

  created() {
    this.getCurrentElectiveInfo();
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
      this.getCurrentElectiveInfo();
    },

    /**
     * 获取当前页信息
     */
    handleCurrentChange(val) {
      this.current = val;
      this.getCurrentElectiveInfo();
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

    test(courseId, selectedState) {
      if (selectedState) {
        this.courses.push(courseId);
      } else {
        this.courses.splice(this.courses.indexOf(courseId), 1);
      }
    },

    /**
     * 获取所有课程信息
     */
    getCurrentElectiveInfo() {
      this.$axios.get("/student/course/getCurrentElectiveInfo", {
        params: {
          courseName: this.searchForm.name,
          current: this.current,
          size: this.size
        }
      }).then(res => {
        this.studentCourseData = res.data.data.records;
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
          if (this.courses.length <= 0) {
            this.$message({
              showClose: true,
              message: "请先添加课程",
              type: 'error',
            });
            return ;
          }

          this.$axios.post('/course/' + (this.studentCourseFlag ? 'update' : 'save'), this.studentCourseForm)
              .then(res => {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: 'success',
                  onClose:() => {
                    this.getCurrentElectiveInfo()
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
      this.studentCourseFlag = false;
      this.getCurrentStudentNoCoursesInfo();
    },

    /**
     * 获取课程表中课程类型为选修课的多条记录
     */
    getCurrentStudentNoCoursesInfo() {
      this.$axios.get("/student/course/getCurrentStudentNoCoursesInfo", ).then(res => {
        this.tableElectiveCourse = res.data.data.records;
        this.size = res.data.data.size;
        this.current = res.data.data.current;
        this.total = res.data.data.total;
      });
    },

    /**
     * 编辑专业信息
     */
    editHandle(courseId) {
      this.$axios.get('/course/info/' + courseId).then(res => {
        this.studentCourseForm = res.data.data;
        this.dialogVisible = true;
        this.studentCourseFlag = true;
      });
    },

    /**
     * 批量删除
     * @param courseId 课程编号
     */
    delHandle(courseId) {
      let courseIds = [];
      if (courseId) {
        courseIds.push(courseId);
      } else {
        this.multipleSelection.forEach(row => {
          courseIds.push(row.courseId);
        });
      }
      this.$axios.post("/course/delete", courseIds).then(res => {
        this.$message({
          showClose: true,
          message: res.data.msg,
          type: 'success',
          onClose:() => {
            this.getCurrentElectiveInfo()
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