# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a uni-app mobile application called "叮咚号卡" (DingDong Number Card) built with Vue.js 2. It's a cross-platform mobile app that supports WeChat Mini Program, H5, and native app deployment.

## Key Architecture

### Framework & Libraries
- **uni-app**: Cross-platform framework for mobile development
- **Vue.js 2**: Frontend framework (configured in manifest.json)
- **Vuex**: State management (store/)
- **uview-ui**: UI component library
- **crypto-js**: Encryption utilities
- **echarts**: Charts and data visualization

### Project Structure
- `pages/`: All application pages organized by feature (login, mine, order, product, etc.)
- `api/`: API modules organized by domain (finance, home, login, order, product, system)
- `store/`: Vuex store with modules pattern (currently has user module)
- `utils/`: Utility functions (auth, request, validation, upload, etc.)
- `static/`: Static assets including images and SCSS styles
- `components/`: Reusable Vue components
- `uni_modules/`: uni-app ecosystem modules
- `uview-ui/`: UI component library source

### Configuration Files
- `manifest.json`: Platform-specific configurations for WeChat Mini Program, H5, and native apps
- `pages.json`: Page routing, tab bar, and navigation configuration
- `config.js`: Application configuration including API base URL and app info
- `uni.scss`: Global SCSS variables and styles

### Key Features
- Multi-platform deployment (WeChat Mini Program, H5, Native)
- Custom navigation bars on all pages
- Tab bar navigation with 4 main sections: 首页(Home), 订单(Orders), 商品(Products), 我的(Mine)
- Authentication system with token-based auth
- Commission and product management system
- Order management with detailed views

## Development Commands

### HBuilderX IDE (Recommended)
- **H5 Development**: Run in browser, auto-opens on port 9090
- **WeChat Mini Program**: Use built-in WeChat Developer Tools integration
- **Native App**: Build and run on iOS/Android simulators

### uni-app CLI Alternative
```bash
# Install uni-app CLI globally
npm install -g @dcloudio/uni-cli

# H5 development
npm run dev:h5

# WeChat Mini Program development
npm run dev:mp-weixin

# Build for production
npm run build:h5
npm run build:mp-weixin
```

### WeChat Mini Program Development
```bash
# WeChat Developer Tools project configuration
# Import project using project.config.json
# AppID: wxccd7e2a0911b3397 (configured in manifest.json)
```

### Development Setup
- Development server configured for H5 on port 9090
- WeChat Mini Program appid: wxccd7e2a0911b3397
- Build output in `unpackage/` directory

### State Management
- Centralized Vuex store in `store/index.js`
- User module for authentication state
- Global getters for shared computed properties

### API Integration
- Base URL configurable in `config.js` (currently set to localhost:8080)
- Request utilities in `utils/request.js`
- API modules separated by business domain

### Styling
- Global SCSS styles in `static/scss/index.scss`
- uview-ui component library integration
- Custom navigation styling across all pages

## Platform-specific Development

### H5 Web Development
- Development server: `localhost:9090`
- Router mode: hash (configured in manifest.json)
- Authentication: Token-based with automatic login redirect

### WeChat Mini Program
- AppID: `wxccd7e2a0911b3397`
- Configuration: `project.config.json` and `project.private.config.json`
- Development: Use WeChat Developer Tools
- Permissions: Network requests, user info access

### Native App Development
- Platforms: Android and iOS
- Permissions configured in `manifest.json` for camera, network, storage
- Build process through HBuilderX cloud build or local build

## Common Development Tasks

### Adding New Pages
1. Create page component in `pages/` directory
2. Update `pages.json` with route configuration
3. Set `navigationStyle: "custom"` for consistent UI

### API Integration
- Base URL configured in `config.js`
- HTTP client in `utils/request.js` with automatic token handling
- Error handling and token refresh logic included

### State Management
- Add new modules to `store/modules/`
- Register in `store/index.js`
- Use getters in `store/getters.js` for shared computed properties

### Styling and Theming
- Global styles in `uni.scss`
- uview-ui component library integration
- SCSS variables available for consistent theming

## Important Notes
- All pages use custom navigation (`navigationStyle: "custom"`)
- The app requires authentication - unauthenticated H5 users are redirected to login
- This appears to be a business application for managing number cards/telecom products
- Multi-platform codebase: single source code deploys to H5, WeChat Mini Program, and native apps