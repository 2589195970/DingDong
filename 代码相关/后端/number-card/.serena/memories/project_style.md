# Coding Style & Conventions

- **Backend (Java)**: UpperCamelCase classes, Spring stereotypes, Lombok DTOs, REST endpoints under `/api/...`, reuse constants from `ruoyi-common`. Ensure unit tests under `ruoyi-*/src/test/java` when adding service logic.
- **Frontend (Vue 2)**: ESLint `plugin:vue/recommended`, two-space indent, single quotes, PascalCase components (`vue/name-property-casing`), camelCase for stores/utilities. Apply `npm run lint` before commits.
- **General Principles**: YAGNI first, KISS for implementations, DRY for shared logic. Use Why-How-Done mindset with atomic task breakdown and continuous verification.
