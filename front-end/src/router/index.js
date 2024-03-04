import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PanelView from '@/views/PanelView.vue'
import NotFound from '@/views/NotFound.vue'

import UsersPanel from '@/components/UsersPanel.vue'
import ComputersPanel from '@/components/ComputersPanel.vue'
import PasswordsPanel from '@/components/PasswordsPanel.vue'
import MailsPanel from '@/components/MailsPanel.vue'

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
      path: "/panel/passwords",
      name: "passwords",
      component: PasswordsPanel
    },
    {
      path: "/panel/mails",
      name: "mails",
      component: MailsPanel
    },
    // REDIRECT UNKNOWN URL
    {
      path: '/:pathMatch(.*)*',
      redirect: '/404'
    },
    // NOT FOUND 404
    {
      path: '/404',
      name: 'NotFound',
      component: NotFound
    }
  ]
})

export default router
