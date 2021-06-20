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
        <el-button @click="getCourseList">搜索</el-button>
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
          prop="courseId"
          label="课程编号"
          width="120">
      </el-table-column>
      <el-table-column
          prop="courseName"
          label="课程名称"
          width="120">
      </el-table-column>
      <el-table-column
          prop="courseCredit"
          label="课程学分"
          width="120">
      </el-table-column>
      <el-table-column
          prop="courseHours"
          label="课程学时"
          width="120">
      </el-table-column>
      <el-table-column
          prop="startTime"
          label="开课时间"
          width="140"
          :formatter="dateFormatter">
      </el-table-column>
      <el-table-column
          prop="endTime"
          label="结课时间"
          width="140"
          :formatter="dateFormatter">
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
          <el-button type="text" @click="editHandle(scope.row.courseId)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm title="确定要删除吗？" @confirm="delHandle(scope.row.courseId)">
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
      <el-form :model="courseForm" :rules="courseFormRules" ref="courseForm" label-width="100px" class="demo-editForm">
        <el-form-item label="课程编号" prop="courseId" label-width="120px">
          <el-input v-model="courseForm.courseId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName" label-width="120px">
          <el-input v-model="courseForm.courseName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程学分" prop="courseCredit" label-width="120px">
          <el-input v-model="courseForm.courseCredit" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课程学时" prop="courseHours" label-width="120px">
          <el-input v-model="courseForm.courseHours" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="班级" prop="clbumId" label-width="120px">
          <el-select v-model="courseForm.clbumId" placeholder="请选择">
            <el-option
                v-for="item in courseClbums"
                :key="item.clbumId"
                :label="item.clbumName"
                :value="item.clbumId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程节数" prop="courseSection" label-width="120px">
          <el-select v-model="courseForm.courseSection" placeholder="请选择">
            <el-option
                v-for="item in courseSections"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程周天" prop="courseWhichDay" label-width="120px">
          <el-select v-model="courseForm.courseWhichDay" multiple placeholder="请选择">
            <el-option
                v-for="item in courseWhichDays"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开课时间" prop="startTime" label-width="120px">
          <el-date-picker
              v-model="courseForm.startTime"
              type="date"
              format="yyyy-MM-dd"
              size="large"
              placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结课时间" prop="endTime" label-width="120px">
          <el-date-picker
              v-model="courseForm.endTime"
              type="date"
              format="yyyy-MM-dd"
              size="large"
              placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('courseForm')">{{ this.flag ? '保存' : '创建' }}</el-button>
          <el-button @click="resetForm('courseForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {formatDate} from '@/util/LocalDateUtils'

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
      tableData: [],
      courseForm: {
        teacherId: ''
      },
      courseFormRules: {
        courseId: [
          {required: true, message: '请输入课程编号', trigger: 'blur'}
        ],
        courseName: [
          {required: true, message: '请输入学生课程名称', trigger: 'blur'}
        ],
        courseCredit: [
          {required: true, message: '请输入课程学分', trigger: 'blur'}
        ],
        courseHours: [
          {required: true, message: '请输入课程学时', trigger: 'blur'}
        ],
        clbumId: [
          {required: true, message: '请选择班级', trigger: 'blur'}
        ],
        courseSection: [
          {required: true, message: '请选择课程节数', trigger: 'blur'}
        ],
        courseWhichDay: [
          {required: true, message: '请选择课程周天', trigger: 'blur'}
        ],
        startTime: [
          {required: true, message: '请选择开课时间', trigger: 'blur'}
        ],
        endTime: [
          {required: true, message: '请选择结课时间', trigger: 'blur'}
        ]
      },
      /** 批量删除选中 */
      multipleSelection: [],
      /** 新增编辑状态区分 */
      flag: false,
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
      }]
    }
  },

  created() {
    this.getCourseList();
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
      console.log(val)
      this.multipleSelection = val;
      this.delBtlState = val.length === 0;
    },

    /**
     * 获取总页数
     */
    handleSizeChange(val) {
      this.size = val;
      this.getCourseList();
    },

    /**
     * 获取当前页信息
     */
    handleCurrentChange(val) {
      this.current = val;
      this.getCourseList();
    },

    /**
     * 重置对话框
     */
    resetForm(courseForm) {
      this.$refs[courseForm].resetFields();
      this.courseForm = {};
    },

    /**
     * 关闭对话框
     */
    handleClose() {
      this.resetForm('courseForm');
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
    getCourseList() {
      this.$axios.get("/course/list", {
        params: {
          courseName: this.searchForm.name,
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
     * @param courseForm formName
     */
    submitForm(courseForm) {
      this.$refs[courseForm].validate((valid) => {
        if (valid) {
          // 判断startTime是否为一个对象，是则进行转换
          if (this.courseForm.startTime instanceof Object) {
            this.courseForm.startTime = formatDate(this.courseForm.startTime);
            this.courseForm.endTime = formatDate(this.courseForm.endTime);
          }

          let courseWhichDay = this.courseForm.courseWhichDay;
          let courseWhichDayStr = '';

          for (let i = 0; i < courseWhichDay.length; i++) {
            courseWhichDayStr += (i + 1) === courseWhichDay.length ? courseWhichDay[i] : courseWhichDay[i] + ',';
          }

          this.courseForm.courseWhichDay = courseWhichDayStr;

          this.$axios.post('/course/' + (this.flag ? 'update' : 'save'), this.courseForm)
              .then(res => {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: 'success',
                  onClose:() => {
                    this.getCourseList()
                  }
                });
                this.dialogVisible = false;
                this.resetForm(courseForm);
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
      this.getClbumList();
      this.getTeacherByUsername();
    },

    /**
     * 获取教师信息
     */
    getTeacherByUsername() {
      this.$axios.get("/teacher/getTeacherByUsername").then(res => {
        this.courseForm.teacherId = res.data.data.teacherId;
      });
    },

    /**
     * 获取班级信息
     */
    getClbumList() {
      this.$axios.get("/clbum/getClbumAll", ).then(res => {
        this.courseClbums = res.data.data;
      });
    },

    /**
     * 编辑专业信息
     */
    editHandle(courseId) {
      this.$axios.get('/course/info/' + courseId).then(res => {
        this.courseForm = res.data.data;
        console.log("/course/info/");
        console.log(res.data.data);
        this.dialogVisible = true;
        this.flag = true;
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
            this.getCourseList()
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