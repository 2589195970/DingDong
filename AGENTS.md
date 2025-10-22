# Repository Guidelines

## Project Structure & Module Organization
- `代码相关/前端/前端代码/电脑版后台` hosts the Vue 2 admin console; keep page logic under `src/views`, shared widgets under `src/components`, and route guards in `src/permission.js`. Build artefacts land in `dist/`.
- `代码相关/前端/前端代码/{H5,手机版,聚合页}` house channel-specific bundles that reuse `src/components`; replicate admin conventions when wiring new entry files.
- `代码相关/后端/number-card` is the Spring Boot (RuoYi) workspace: `ruoyi-admin` exposes REST APIs, `ruoyi-system` holds domain services, `ruoyi-framework` provides security/config, while `sql/` and `docs/` track schema jobs. Use `mc-*` modules for domain features.
- `数据库备份` stores dated SQL snapshots—refresh local databases from the latest file before validating data migrations.

## Build, Test, and Development Commands
- Front-end: `npm install` then `npm run dev` under `.../电脑版后台` for hot reload, `npm run build:prod` for production bundles, and `npm run lint` to apply ESLint auto-fixes.
- Back-end: from `代码相关/后端/number-card`, run `mvn clean package -pl ruoyi-admin -am` to build all modules, `java -jar ruoyi-admin/target/ruoyi-admin.jar` to launch locally, and `mvn test` before submission to exercise module-level checks.

## Coding Style & Naming Conventions
- Vue code follows `plugin:vue/recommended`: two-space indentation, single quotes, PascalCase component names (`vue/name-property-casing`), and camelCase stores/utilities. Commit lint fixes before pushing to satisfy the pre-commit `lint-staged` hook.
- Java services align with current RuoYi packages: classes in UpperCamelCase, Spring stereotypes on beans, Lombok for DTOs, and REST paths versioned under `/api/...`. Prefer shared constants from `ruoyi-common`.

## Testing Guidelines
- Prefer backend unit or slice tests under `ruoyi-*/src/test/java` with Spring Boot test starters; cover new service logic and mapper queries. Document fixtures under `sql/` when tests rely on seed data.
- For front-end changes, add Jest or Cypress coverage when possible and at minimum keep `npm run lint` passing; attach screenshots when visual states change.

## Commit & Pull Request Guidelines
- Follow the existing Conventional Commits pattern (`feat(order): 描述`); map the scope to module folders (`system`, `photo`, `orders`) and keep subjects under 72 characters.
- Pull requests need a short summary, linked issue, UI evidence when relevant, database impact notes, and confirmation of lint/tests/build. Request both front-end and back-end reviewers when a change spans layers.

## Configuration & Security Tips
- Manage environment secrets via `application-{dev,release}.yml` overlays and reference them with `@Value`; never commit plaintext credentials.
- Align front-end API hosts with backend profiles by updating `src/settings.js` (admin) or channel-specific `config/index.js` to prevent cross-origin drift.
