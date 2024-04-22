import { createRouter, createWebHistory } from 'vue-router'
// import HomeView from '../views/HomeView.vue'
// import PanelView from '@/views/PanelView.vue'
import NotFound from '@/views/NotFound.vue'

import UsersPanel from '@/components/UsersPanel.vue'
import ComputersPanel from '@/components/ComputersPanel.vue'
import PasswordsPanel from '@/components/PasswordsPanel.vue'
import MailsPanel from '@/components/MailsPanel.vue'
import AuthView from '@/views/AuthView.vue'
import AdminsPanel from '@/components/AdminsPanel.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/auth'
    },
    {
      path: '/auth',
      name: 'auth',
      component: AuthView
    },
    // {
    //   path: '/',
    //   name: 'home',
    //   component: HomeView
    // },
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AboutView.vue')
    // },
    {
      path: "/panel",
      redirect: '/panel/users'
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
    {
      path: "/panel/admins",
      name: "admins",
      component: AdminsPanel
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
