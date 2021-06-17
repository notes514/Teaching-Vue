<template>
  <div>
    <el-table
        :data="tableData"
        :header-cell-style="{textAlign: 'center'}"
        :cell-style="{textAlign: 'center', height: '90px'}"
        border
        stripe
        style="width: 100%; margin-top: 20px">
      <el-table-column
          prop="section"
          label=""
          width="70">
      </el-table-column>
      <el-table-column
          prop="course[0][1]"
          label="星期一"
          width="160">
      </el-table-column>
      <el-table-column
          prop="course[0][2]"
          label="星期二"
          width="160">
      </el-table-column>
      <el-table-column
          prop="course[0][3]"
          label="星期三"
          width="160">
      </el-table-column>
      <el-table-column
          prop="course[0][4]"
          label="星期四"
          width="160">
      </el-table-column>
      <el-table-column
          prop="course[0][5]"
          label="星期五"
          width="160">
      </el-table-column>
      <el-table-column
          prop="course[0][6]"
          label="星期六"
          width="160">
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
      tableData: [{
        section: '1-2节',
        course: [
          ['', '毛概@14208', '毛概@14208', '', '', '选修', '', '']
        ]
      },{
        section: '3-4节',
        course: [
          ['', '大学英语(Ⅳ)@10203', '' , '模拟电子技术基础@16204', '', '模拟电子技术基础@16204', '', '']
        ]
      },{
        section: '5-6节',
        course: [
          ['', '', '信号与系统@11302', '', '信号与系统@11302', '电路、信号与系统实验', '', '']
        ]
      },{
        section: '7-8节',
        course: [
          ['', '形势与政策(Ⅳ)@15208', '', '电装实习@11301', '', '电装实习@11301', '', '',]
        ]
      },{
        section: '9-10节',
        course: [
          ['', '大学体育(Ⅳ)', '', '数据结构与算法分析', '', '数据结构与算法分析', '', '', ]
        ]
      }]
    };
  },
  methods: {
    arraySpanMethod({ row, column, rowIndex, columnIndex }) {
      if (rowIndex % 2 === 0) {
        if (columnIndex === 0) {
          return [1, 2];
        } else if (columnIndex === 1) {
          return [0, 0];
        }
      }
    },
  }
};
</script>

<style scoped>

</style>