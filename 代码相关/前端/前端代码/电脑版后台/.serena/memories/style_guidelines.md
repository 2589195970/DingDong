# 代码/风格约定
- 前端遵循 `plugin:vue/recommended`：组件 PascalCase、两空格缩进、单引号、计算属性/方法按 Vue2 语法。
- JS 使用 ESLint + lint-staged，提交前自动修复；尽量保持 DRY、KISS、YAGNI 原则。
- 组件放置：业务页面在 `src/views/<module>`，共享组件在 `src/components`，路由/权限在 `src/permission.js`。
- API 请求集中在 `src/api/**` 下的 request 包装。
- 注释：仅在复杂逻辑前添加简洁说明，避免冗余描述。
- 常量/配置统一放置于 settings 或 common 常量文件，命名 camelCase。