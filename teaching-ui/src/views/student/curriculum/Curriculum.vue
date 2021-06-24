<template>
  <div>
    <el-table
        :data="studentCurriculumData"
        :header-cell-style="{textAlign: 'center'}"
        :cell-style="{textAlign: 'center', height: '90px'}"
        border
        stripe
        style="width: 100%; margin-top: 20px">
      <el-table-column
          prop="section"
          label=""
          width="60">
      </el-table-column>
      <el-table-column
          prop="course[0][1]"
          label="星期一">
      </el-table-column>
      <el-table-column
          prop="course[0][2]"
          label="星期二">
      </el-table-column>
      <el-table-column
          prop="course[0][3]"
          label="星期三">
      </el-table-column>
      <el-table-column
          prop="course[0][4]"
          label="星期四">
      </el-table-column>
      <el-table-column
          prop="course[0][5]"
          label="星期五">
      </el-table-column>
      <el-table-column
          prop="course[0][6]"
          label="星期六">
      </el-table-column>
      <el-table-column
          prop="course[0][7]"
          label="星期日 ">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "Curriculum",
  data() {
    return {
      studentCurriculumData: []
    };
  },
  created() {
    this.getCurriculumInfo();
  },
  methods: {
    /**
     * 获取课程信息
     */
    getCurriculumInfo() {
      this.$axios.get("/student/course/getCurriculumInfo").then(res => {
        this.studentCurriculumData = res.data.data;
      });
    }
  }
};
</script>

<style scoped>

</style>