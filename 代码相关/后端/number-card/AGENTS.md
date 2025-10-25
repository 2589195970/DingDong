# Repository Guidelines

## 项目结构与模块组织
后端源码集中在 `代码相关/后端/number-card`：`ruoyi-admin` 提供 REST API，`ruoyi-system` 承载业务服务，`ruoyi-framework` 负责安全与配置；按领域将功能拆入既有 `mc-*` 模块。桌面管理前端位于 `代码相关/前端/前端代码/电脑版后台`，页面放在 `src/views`，通用组件放在 `src/components`，路由守卫由 `src/permission.js` 管理。渠道端 (`H5`、`手机版`、`聚合页`) 共用组件库但保留独立入口。数据库快照存于 `数据库备份`，在编写脚本或验证迁移前先恢复最新数据。

## 构建、测试与开发命令
在 `前端/前端代码/电脑版后台` 执行：
```bash
npm install
npm run dev        # 启动热更新开发环境
npm run build:prod # 打包生产资源
npm run lint       # 运行 ESLint 并修复
```
后端从 `代码相关/后端/number-card` 启动：
```bash
mvn clean package -pl ruoyi-admin -am
java -jar ruoyi-admin/target/ruoyi-admin.jar
mvn test
```

## 编码风格与命名约定
前端遵循 `plugin:vue/recommended`，保持两空格缩进、单引号、PascalCase 组件名，工具与 store 使用 camelCase；提交前务必执行 `npm run lint`。后端沿用 RuoYi 规范：类名 UpperCamelCase，Spring Bean 明确注解，DTO 使用 Lombok，REST 路径统一挂载在 `/api/...`，优先复用 `ruoyi-common` 常量与工具类。

## 测试指引
后端新增逻辑需在 `ruoyi-*/src/test/java` 增补 Spring Boot 单元或切片测试，涉及数据准备时将 SQL 脚本同步到 `sql/`。前端改动至少保证 `npm run lint` 通过，功能变化优先补充 Jest/Cypress 用例，并为视觉改动截取截图以便评审。

## 提交与合并请求规范
遵循 Conventional Commits（示例：`feat(system): 新增订单接口`），scope 取目录名，主题控制在 72 字符内。PR 需包含简要说明、关联 Issue、必要的 UI 截图与数据库影响说明，同时确认 lint、测试与构建结果；当前后端均受影响时，请邀请双端评审。

## 安全与配置提示
配置密钥统一维护在 `application-{dev,release}.yml` 并通过 `@Value` 引用，严禁提交明文凭据。同步前端与后端环境时，更新 `src/settings.js` 或对应渠道 `config/index.js` 中的 API Host。执行数据库或架构变更前，先基于最新 `数据库备份` 快照完成验证。
