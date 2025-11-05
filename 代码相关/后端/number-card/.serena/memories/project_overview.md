# Number Card Project Overview

- **Purpose**: Telecom number card platform built on the RuoYi Spring Boot stack with multi-channel front-ends (admin console, H5, mobile) and supporting services for product/agent management.
- **Tech Stack**: Java (Spring Boot/RuoYi modules: ruoyi-admin, ruoyi-system, ruoyi-framework, domain-specific mc-* modules), Vue 2 front-ends per channel, SQL schemas under `sql/`.
- **Structure Highlights**:
  - `代码相关/后端/number-card`: backend workspace separated into RuoYi modules and `mc-*` domain modules.
  - `代码相关/前端/前端代码/电脑版后台`: Vue 2 admin console (`src/views` for pages, `src/components` for shared widgets, `src/permission.js` for guards).
  - Additional Vue bundles under `.../{H5,手机版,聚合页}` sharing components.
  - Database backups in `数据库备份/` for refreshing local environments.
- **Key Guidelines**: Follow RuoYi conventions, keep secrets in profile-specific YAML, align API hosts between front/back ends, and apply Conventional Commits with Chinese descriptions where applicable.
