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
        <el-button @click="getStudentCourseByStudentId">搜索</el-button>
      </el-form-item>
    </el-form>
    <!-- 专业列表 -->
    <el-table
        ref="multipleTable"
        style="width: 100%;"
        :data="tableData"
        :row-style="{height: '36px'}"
        :header-cell-style="{textAlign: 'center', height: '36px', padding: '8px'}"
        :cell-style="{textAlign: 'center', padding: '1px'}"
        tooltip-effect="dark"
        border
        stripe>
      <el-table-column
          prop="studentId"
          label="学号">
      </el-table-column>
      <el-table-column
          prop="studentName"
          label="姓名">
      </el-table-column>
      <el-table-column
          prop="courseName"
          label="课程名称">
      </el-table-column>
      <el-table-column
          prop="score"
          label="成绩">
      </el-table-column>
      <el-table-column
          prop="tag"
          label="等级 ">
        <template slot-scope="scope">
          <el-tag size="small" v-if="scope.row.score >= 90" type="success">优秀</el-tag>
          <el-tag size="small" v-else-if="scope.row.score >= 80" type="warning">良好</el-tag>
          <el-tag size="small" v-else-if="scope.row.score >= 60" type="info">合格</el-tag>
          <el-tag size="small" v-else type="danger">不合格</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="updateTime"
          label="更新时间">
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
  </div>
</template>

<script>

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
      /** 专业列表数据 */
      tableData: []
    }
  },

  created() {
    this.getStudentCourseByStudentId();
  },

  methods: {

    /**
     * 获取总页数
     */
    handleSizeChange(val) {
      this.size = val;
      this.getStudentCourseByStudentId();
    },

    /**
     * 获取当前页信息
     */
    handleCurrentChange(val) {
      this.current = val;
      this.getStudentCourseByStudentId();
    },

    /**
     * 获取学生课程成绩信息
     */
    getStudentCourseByStudentId() {
      this.$axios.get("/student/course/getCurrentStudentCourseByScore", {
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