<script setup>
import { usePrincipalStore } from '@/stores/principal'
const principalStore = usePrincipalStore()
import { ref } from 'vue'
const activeIndex = ref('1')
const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}

</script>

<template>
  <p><strong>Current route path:</strong> {{ $route.fullPath }}</p>
  <p><strong>Principal:</strong> {{ principalStore.principal }}</p>
  <nav>
     
    
  </nav>

  <el-menu
    :default-active="activeIndex"
    class="el-menu-demo"
    mode="horizontal"
    :ellipsis="false"
    @select="handleSelect"
  >
    <el-menu-item index="0"><RouterLink to="/user">User Page</RouterLink></el-menu-item>

    <el-menu-item index="1"><RouterLink to="/role">Role Page</RouterLink></el-menu-item>
  </el-menu>

  <main>
    <RouterView />
  </main>
</template>

<style scoped>
</style>
