# 任务完成前检查
- 代码改动需在 HBuilderX/uni-app CLI 上验证目标平台能正常编译、界面无异常跳转。
- 关键交互（登录、实名认证、提现入口等）需要手动走一遍，观察 `permission.js` 守卫行为。
- 若引入新的缓存键，确认 `utils/storage.js` 白名单更新，并在真机/浏览器刷新后仍可恢复状态。
- 没有固定测试脚本；若添加单元测试可使用 `@vue/test-utils` 或 uni-app 提供的 UT 方案，运行 `npm test`（需自行配置）。
- 交付前检查 Git 工作树，确保只包含本次修改；必要时运行 `npm install` 以锁定依赖。