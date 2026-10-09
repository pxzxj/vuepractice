<script setup>
import { ref } from 'vue'

const username = ref('');
const password = ref('');
const loginFailMessage = ref('');

async function login() {
 const response = await fetch('/api/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: new URLSearchParams({
      username: username.value,
      password: password.value
    })
  })
  const result = await response.text()
  if (result === 'success') {
    location.reload()
  } else {
    loginFailMessage.value = result
  }
}

</script>

<template>
  <h1>Login Page</h1>
  <form @submit.prevent="login">
    <label for="username">Username</label> <input type="text" v-model="username" placeholder="Username" /> 
    <label for="password">Password</label> <input type="password" v-model="password" placeholder="Password" />
    <button type="submit">Login</button>
    <p v-if="loginFailMessage" style="color: red;">{{ loginFailMessage }}</p>
  </form>
</template>

<style scoped></style>
