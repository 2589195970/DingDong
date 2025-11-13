const constant = {
   avatar: 'vuex_avatar',
   name: 'vuex_name',
   roles: 'vuex_roles',
   permissions: 'vuex_permissions',
   agentAccount: 'vuex_agent_account',
   vipInfo: 'vuex_vip_info',
   REAL_NAME_PAGE: '/package-mine/mine/realname/index',

   // 实名认证状态常量 (与数据库定义保持一致)
   REAL_NAME_STATUS: {
     UNVERIFIED: 0,    // 未认证
     VERIFIED: 1,      // 已认证
     VERIFYING: 2,     // 认证中
     FAILED: 3         // 认证失败
   }
 }

 export default constant
