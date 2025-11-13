# 任务完成清单
- 前端：确保 `npm run lint` 通过，必要时 `npm run build:prod` 验证打包。
- 若涉及 UI/交互，准备截图或说明；若涉及接口，对照后端接口文档确认参数。
- 跨前后端改动需分别通知对应 reviewer，并确认接口环境配置（`src/settings.js` 等）。
- 保持工作区干净，避免回滚用户已有改动。提交遵循 Conventional Commits（如 `feat(products): ...`）。
- 若依赖数据库或配置，注明影响并同步 docs/sql。