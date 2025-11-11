# 风格与规范
- 代码基于 Vue 2 + uni-app：`<template>/<script>/<style scoped>` 结构，样式多使用 SCSS，组件引入 uView。
- JS/TS：使用 ES modules + async/await；常量集中在 `utils/constant.js`；Vuex 使用 `store/modules/user.js`，mutation 常量直接写字符串。
- 导航统一通过 `this.$tab.navigateTo|redirectTo|reLaunch`；全局守卫逻辑集中在 `permission.js`。
- 状态与缓存：缓存工具 `utils/storage.js` 只会持久化在白名单内的键，确保新增键写入前加到白名单。
- 国际化暂无；文案直接写在页面脚本中，提示统一经 `this.$modal` 与 `uni.showToast/uni.showModal`。
- 依赖 uView 2.x，保持与其组件命名和 slot 约定一致。