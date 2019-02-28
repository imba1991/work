import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const main = () =>
  import('@/views/index.vue'); //首页组件

//个人中心
const userCenter = () =>
  import('@/views/userCenter/index.vue'); //个人中心首页组件
const receiptConfigRecord = () =>
  import('@/views/userCenter/receiptConfigRecord/index.vue'); //收款配置记录组件
const alipayConfig = () =>
  import('@/views/userCenter/receiptConfig/alipayConfig.vue'); //支付宝收款配置组件
const weChatConfig = () =>
  import('@/views/userCenter/receiptConfig/weChatConfig.vue'); //微信收款配置组件
const userInfo = () =>
  import('@/views/userCenter/userInfo/index.vue'); //员工信息组件
const authority = () =>
  import('@/views/userCenter/userInfo/authority.vue'); //员工权限管理组件

const login = () =>
  import('@/views/sign/login.vue'); //登录组件
const register = () =>
  import('@/views/sign/register.vue'); //注册组件

const registerResult = () =>
  import('@/views/sign/registerResult.vue'); //注册结果页
//公用头部 各分中心的父节点都用这个组件比如下面的商品中心  
const header = () =>
  import('@/views/common/header.vue');
const blank = () =>
  import('@/views/common/blank.vue')

//商品中心
const goodsCenter = () =>
  import('@/views/goodsCenter/index.vue'); //商品中心
const goodsList = () =>
  import('@/views/goodsCenter/goodsList/index.vue'); //商品列表
const goodsDetail = () =>
  import('@/views/goodsCenter/goodsList/detail/goodsDetail.vue'); //商品详情
const resourceDetail = () =>
  import('@/views/goodsCenter/goodsList/detail/resourceDetail.vue'); //商品详情
const goodsBuy = () =>
  import('@/views/goodsCenter/goodsBuy'); //商品购买
const newInterface = () =>
  import('@/views/goodsCenter/interface'); //开通新接口
const supplySynGoods = () =>
  import('@/views/goodsCenter/supplySynGoods'); //供应接口同步商品
const distriSynGoods = () =>
  import('@/views/goodsCenter/distriSynGoods'); //分销接口同步商品

//分销中心
const distribute = () =>
  import('@/views/distributeCenter/index/index.vue'); //分销中心index页面
const licenseProduct = () =>
  import('@/views/distributeCenter/licenseProduct/index.vue'); //授权商品页面
const addDistributor = () =>
  import('@/views/distributeCenter/addDistributor/index.vue'); //添加分销商页面
const applyCooperation = () =>
  import('@/views/distributeCenter/applyCooperation/index.vue'); //申请合作页面 

// 通知中心
const notificationCenter = () => import('@/views/notificationCenter/index.vue'); //通知中心首页
const notificationDetail = () => import('@/views/notificationCenter/detail/index.vue'); //通知详情

// 协议组件
const agreementCenter = () => import('@/views/agreementCenter/index.vue'); //协议组件  
//交易中心
const dealCenter = () => import('@/views/dealCenter/tradeLog/index.vue'); //卖出/买进交易
const router = new Router({
  routes: [{
      path: '/register',
      name: 'register',
      component: register,
    },
    {
      path: '/registerResult',
      name: 'registerResult',
      component: registerResult,
    }, {
      path: '/login',
      name: 'login',
      component: login,
    }, {
      path: '/',
      component: header,
      redirect: '/main',
      children: [{
        path: "/main",
        component: main,
        meta: {
          name: "首页"
        }
      }],
    },
    {
      path: "/userCenter",
      redirect: "/userCenter/index",
      component: header,
      meta: {
        name: "个人中心"
      },
      children: [{
          path: "index",
          component: userCenter,
        }, {
          path: 'receiptConfigRecord',
          component: receiptConfigRecord,
          meta: {
            name: "收款配置记录"
          }
        },
        {
          path: 'alipayConfig',
          component: alipayConfig,
          meta: {
            name: "支付宝收款配置"
          }
        },
        {
          path: 'weChatConfig',
          component: weChatConfig,
          meta: {
            name: "微信收款配置"
          }
        },
        {
          path: 'userInfo',
          component: blank,
          redirect: "userInfo/index",
          meta: {
            name: "员工信息"
          },
          children: [{
            path: 'index',
            component: userInfo,
          }, {
            path: 'authority',
            component: authority,
            meta: {
              name: "权限管理"
            }
          }]
        },
      ]
    },
    {
      path: "/goodsCenter",
      redirect: "/goodsCenter/index",
      component: header,
      meta: {
        name: "商品中心"
      },
      children: [{
        path: "index",
        component: goodsCenter,
      }, {
        path: "goodsList",
        redirect: "goodsList/index",
        component: blank,
        meta: {
          name: "商品列表"
        },
        children: [{
            path: "index",
            component: goodsList,
          },
          {
            path: "detail",
            redirect: "detail/goodsDetail",
            component: blank,
            meta: {
              name: "商品详情"
            },
            children: [{
              path: 'goodsDetail',
              name: 'goodsDetail',
              component: goodsDetail,

            }, {
              path: 'resourceDetail',
              name: 'resourceDetail',
              component: resourceDetail,
              meta: {
                name: "资源详情"
              }
            }],


          }
        ]
      }, {
        path: "goodsBuy",
        component: goodsBuy,
        meta: {
          name: "购买"
        }
      }, {
        path: "newInterface",
        component: newInterface,
        meta: {
          name: "开通新接口"
        }
      }, {
        path: "supplySynGoods",
        component: supplySynGoods,
        meta: {
          name: "供应接口同步商品"
        }
      }, {
        path: "distriSynGoods",
        component: distriSynGoods,
        meta: {
          name: "分销接口同步商品"
        }
      }]
    },
    {
      path: "/distributionCenter",
      redirect: "/distributionCenter/index",
      component: header,
      meta: {
        name: "分销中心"
      },
      children: [{
          path: "index",
          component: distribute,
          meta: {
            name: "商户列表"
          }
        },
        {
          path: "licenseProduct",
          component: licenseProduct,
          meta: {
            name: " 授权商品"
          }
        },
        {
          path: "addDistributor",
          component: addDistributor,
          meta: {
            name: " 添加分销商"
          }
        },
        {
          path: "applyCooperation",
          component: applyCooperation,
          meta: {
            name: " 申请合作"
          }

        }
      ]
    },
    {
      path: "/dealCenter",
      redirect: "/dealCenter/sold",
      component: header,
      meta: {
        name: "交易中心"
      },
      children: [{
        path: "buyIn",
        component: dealCenter,
        meta: {
          type:"buyIn",
          name: "买进交易"
        }
      }, {
        path: "sold",
        component: dealCenter,
        meta: {
          type:"sold",
          name: "卖出交易"
        }
      }]
    },
    {
      path: "/notificationCenter",
      redirect: "/notificationCenter/index",
      component: header,
      meta: {
        name: "通知中心"
      },
      children: [{
        path: "index",
        component: notificationCenter,
      }, {
        path: "notificationDetail",
        component: notificationDetail,
        meta: {
          name: "通知详情"
        }
      }]
    },
    {
      path: "/agreementCenter",
      component: agreementCenter,
    }
  ],
  scrollBehavior(to, from, savedPosition) {
    return {
      x: 0,
      y: 0
    }
  }

})

router.beforeEach(async (to, from, next) => {
  // if (to.path === '/login' || to.path === '/register' || to.path === '/registerResult') return next();
  // else {
  //   if (!sessionStorage.getItem("ticket"))
  //     return next("/login")
  // }


  // let EXPIRE = localStorage.getItem('EXPIRE');

  //   if(!EXPIRE||Date.now() >= EXPIRE){

  //     return next('/login')
  // }
  // let pages = sessionStorage.getItem("buttonPermissions");
  // if (pages[to.path] !== undefined) {
  //   return next();
  // } else {
  //   next()//暂时不拦截
  //   //TODO 跳转到指定页面 例：您暂无权限访问该页面
  // }
  sessionStorage.setItem('EXPIRE', new Date().getTime() + 900000) //刷新登陆有效期15分钟
  return next();
});
export default router;
