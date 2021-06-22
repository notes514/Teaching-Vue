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
        <el-button @click="getProfessionList">搜索</el-button>
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
          prop="professionId"
          label="专业编号"
          width="140">
      </el-table-column>
      <el-table-column
          prop="professionName"
          label="专业名称"
          width="200">
      </el-table-column>
      <el-table-column
          prop="academyId"
          label="所属院系编号"
          width="140">
      </el-table-column>
      <el-table-column
          prop="officePhone"
          label="办公室电话"
          width="160">
      </el-table-column>
      <el-table-column
          prop="createTime"
          label="创建时间"
          width="200">
      </el-table-column>
      <el-table-column
          prop="icon"
          label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="editHandle(scope.row.professionId)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <template>
            <el-popconfirm title="确定要删除吗？" @confirm="delHandle(scope.row.professionId)">
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
      <el-form :model="professionForm" :rules="professionFormRules" ref="professionForm" label-width="100px" class="demo-editForm">
        <el-form-item label="专业编号" prop="professionId" label-width="120px">
          <el-input v-model="professionForm.professionId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="专业名称" prop="professionName" label-width="120px">
          <el-input v-model="professionForm.professionName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所属院系编号" prop="academyId" label-width="120px">
          <el-input v-model="professionForm.academyId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="办公室电话" prop="officePhone" label-width="120px">
          <el-input v-model="professionForm.officePhone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('professionForm')">{{ this.flag ? '保存' : '创建' }}</el-button>
          <el-button @click="resetForm('professionForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {isPoneAvailable} from '@/util/LocalDateUtils'

export default {
  name: "Profession",
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
      professionForm: {
      },
      professionFormRules: {
        professionId: [
          {required: true, message: '请输入专业编号', trigger: 'blur'}
        ],
        professionName: [
          {required: true, message: '请输入专业名称', trigger: 'blur'}
        ],
        academyId: [
          {required: true, message: '请输入所属学院编号', trigger: 'blur'}
        ],
        officePhone: [
          {
            required: true,
            pattern: /^0?(13[0-9]|15[7-9]|153|156|18[7-9])[0-9]{8}$/,
            message: '手机号码格式不正确',
            trigger: 'blur',
          }
        ]
      },
      /** 批量删除选中 */
      multipleSelection: [],
      /** 新增编辑状态区分 */
      flag: false
    }
  },

  created() {
    this.getProfessionList();
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
      this.getProfessionList();
    },

    /**
     * 获取当前页信息
     */
    handleCurrentChange(val) {
      this.current = val;
      this.getProfessionList();
    },

    /**
     * 重置对话框
     */
    resetForm(professionForm) {
      this.$refs[professionForm].resetFields();
      this.professionForm = {};
    },

    /**
     * 关闭对话框
     */
    handleClose() {
      this.resetForm('professionForm');
      this.dialogVisible = false;
    },

    /**
     * 获取所有专业信息
     */
    getProfessionList() {
      this.$axios.get("/profession/list", {
        params: {
          professionName: this.searchForm.name,
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
     * @param professionForm formName
     */
    submitForm(professionForm) {
      this.$refs[professionForm].validate((valid) => {
        if (valid) {
          if (!isPoneAvailable(this.professionForm.officePhone)) {

          }

          this.$axios.post('/profession/' + (this.flag ? 'update' : 'save'), this.professionForm)
              .then(res => {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: 'success',
                  onClose:() => {
                    this.getProfessionList()
                  }
                });
                this.dialogVisible = false;
                this.resetForm(professionForm);
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
    editHandle(professionId) {
      this.$axios.get('/profession/info/' + professionId).then(res => {
        this.professionForm = res.data.data;
        this.dialogVisible = true;
        this.flag = true;
      });
    },

    /**
     * 批量删除
     * @param professionId 专业编号
     */
    delHandle(professionId) {
      let professionIds = [];
      if (professionId) {
        professionIds.push(professionId);
      } else {
        this.multipleSelection.forEach(row => {
          professionIds.push(row.professionId);
        });
      }
      this.$axios.post("/profession/delete", professionIds).then(res => {
        this.$message({
          showClose: true,
          message: res.data.msg,
          type: 'success',
          onClose:() => {
            this.getProfessionList()
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