import { createRouter, createWebHistory } from 'vue-router'
import User from '@/components/User.vue'
import Role from '@/components/Role.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/user', component: User},
    {path: '/role', component: Role}
  ]
})

export default router
