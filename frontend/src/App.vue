<script setup>
import { usePrincipalStore } from './stores/principal.js'
import LoginPage from './components/LoginPage.vue'
import MainPage from './components/MainPage.vue'

const principalStore = usePrincipalStore()

</script>

<template>
  <LoginPage v-if="!principalStore.principal" />
  <MainPage v-else />
</template>

<style scoped></style>
