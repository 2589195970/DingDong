# Task Completion Checklist

- Ensure Java modules build with `mvn clean package -pl ruoyi-admin -am` when backend is touched.
- Run `mvn test` for backend changes.
- For Vue changes, run `npm run lint` (and tests/screenshots if UI updates).
- Document database impacts when relevant and align API hosts/settings.
- Prepare Conventional Commit message (e.g., `feat(system): 描述`).
- Provide PR summary with linked issue, UI evidence if applicable, and confirm lint/tests/build status.
