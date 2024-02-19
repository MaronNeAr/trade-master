import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/views/MainPage.vue'),
    meta: { title: '主页' },
    children: [{
      path: '/',
      component: () => import('@/views/MarketPage.vue'),
      meta: { title: '行情' },
    },
    {
      path: '/market',
      component: () => import('@/views/MarketPage.vue'),
      meta: { title: '行情' },
    },
    {
      path: '/transaction',
      component: () => import('@/views/TransactionPage.vue'),
      meta: { title: '交易' }
    },
    {
      path: '/quant',
      component: () => import('@/views/QuantPage.vue'),
      meta: { title: 'QUANT' }
    },
    {
      path: '/account',
      component: () => import('@/views/AccountPage.vue'),
      meta: { title: '账户' }
    }]
  }
]

const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes
})

export default router
