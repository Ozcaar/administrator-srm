import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PanelView from '@/views/PanelView.vue'
import UsersPanel from '@/components/UsersPanel.vue'
import ComputersPanel from '@/components/ComputersPanel.vue'
import UserModal from '@/components/UserModal.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    },
    {
      path: "/panel",
      name: "panel",
      component: PanelView
    },
    {
      path: "/panel/users",
      name: "users",
      component: UsersPanel
    },
    {
      path: "/panel/computers",
      name: "computers",
      component: ComputersPanel
    },
    {
      path: "/modal",
      name: "modal",
      component: UserModal
    }
  ]
})

export default router
