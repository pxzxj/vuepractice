import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const usePrincipalStore = defineStore('principal', () => {
  const principal = ref('')

  function clear() {
    principal.value = ''
  }
  
  function set(principal) {
    this.principal = principal
  }

  async function load() {
    try {
      const response = await fetch('/api/user/principal')
      const user = await response.json()
      this.principal = user.username
    } catch (error) {
      
    }
  }

  return { principal, clear, load }
})
