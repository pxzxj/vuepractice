<script setup>
import { ref } from 'vue'
import { onMounted } from 'vue'

const tableData = ref([])
onMounted(async () => {
  const response = await fetch('/api/role/page')
  const responseData = await response.json()
  tableData.value = responseData.content
})
</script>

<template>
  <h1>Role Page</h1>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="id" label="Id" width="180" />
    <el-table-column prop="name" label="Name" width="180" />
    <el-table-column prop="description" label="Description" />
  </el-table>
</template>

<style scoped></style>
