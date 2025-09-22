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

### Development Setup
- No specific build commands found in package.json (uni-app typically uses HBuilderX or CLI)
- Development server configured for H5 on port 9090
- WeChat Mini Program appid: wxccd7e2a0911b3397

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

## Important Notes
- All pages use custom navigation (`navigationStyle: "custom"`)
- The app requires authentication - unauthenticated H5 users are redirected to login
- This appears to be a business application for managing number cards/telecom products