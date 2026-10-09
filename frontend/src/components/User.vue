<script setup>
import { ref, reactive } from 'vue'
import { onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'


const tableData = ref([])

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const fetchList = async () => {
  const response = await fetch(`/api/user/page?page=${currentPage.value - 1}&size=${pageSize.value}&sort=id,desc`)
  const responseData = await response.json()
  tableData.value = responseData.content
  total.value = responseData.totalElements
}

onMounted(fetchList)

const dialogFormVisible = ref(false)
const formLabelWidth = '140px'
const form = reactive({
  id: null,
  username: '',
  password: '',
  nickname: '',
  'role.id': 1
})

const handleAdd = () => {
  form.id = null
  form.username = ''
  form.password = ''
  form.nickname = ''
  dialogFormVisible.value = true
}

const handleEdit = (index, row) => {
  console.log(index, row)
  form.id = row.id
  form.username = row.username
  form.password = row.password
  form.nickname = row.nickname
  dialogFormVisible.value = true
}
const handleDelete = (_index, row) => {
    ElMessageBox.confirm(`确定删除「${row.username}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      fetch(`/api/user/delete`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `id=${row.id}`
      })
        .then(() => {
          ElMessage.success('删除成功')
          // 删除成功后重新获取数据
          fetchList()
        })
      
    })
    .catch(() => {
      // 用户取消，什么都不做
    })
}

 async function handleSave() {
   const response = await fetch('/api/user/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: new URLSearchParams(Object.fromEntries(
  Object.entries(form).filter(([_, value]) => value !== null)
  )).toString()
  })
  const responseData = await response.text()
  if (responseData === 'success') {
    ElMessage.success('保存成功')
    dialogFormVisible.value = false
    fetchList()
  } else {
    ElMessage.error(responseData)
  }
}
</script>

<template>
  <h1>User Page</h1> 
          <el-button @click="handleAdd" type="primary" style="margin-bottom: 20px">
          Add
        </el-button>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="id" label="Id" width="180" />
    <el-table-column prop="username" label="Username" width="180" />
    <el-table-column prop="password" label="Password" />
    <el-table-column prop="nickname" label="Nickname" />
    <el-table-column prop="role.name" label="RoleName" /> 
    <el-table-column label="Operations">
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
          Edit
        </el-button>
        <el-button
          size="small"
          type="danger"
          @click="handleDelete(scope.$index, scope.row)"
        >
          Delete
        </el-button>
      </template>
    </el-table-column>   
  </el-table>
  <el-pagination
    v-model:current-page="currentPage"
    v-model:page-size="pageSize"
    :page-sizes="[3, 5, 10]"
    :total="total"
    @size-change="fetchList"
    @current-change="fetchList"
    layout="total, sizes, prev, pager, next, jumper"
  />

  <el-dialog v-model="dialogFormVisible" title="Add/Edit User" width="500">
    <el-form :model="form">
      <el-form-item label="Username" :label-width="formLabelWidth">
        <el-input v-model="form.username" autocomplete="off" />
      </el-form-item>
      <el-form-item label="Password" :label-width="formLabelWidth">
        <el-input v-model="form.password" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="Nickname" :label-width="formLabelWidth">
        <el-input v-model="form.nickname" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSave">
          Confirm
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped></style>
