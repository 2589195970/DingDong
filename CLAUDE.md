# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a DingDong Number Card (叮咚号卡) system - a multi-platform telecom/virtual product management platform with both backend services and frontend applications.

## Architecture

### Repository Structure
- **代码相关/后端/number-card/**: RuoYi-based Spring Boot backend (v3.8.8)
- **代码相关/前端/前端代码/电脑版后台/**: Vue.js 2 admin dashboard
- **代码相关/前端/前端代码/手机版/**: uni-app mobile application
- **代码相关/前端/前端代码/H5/**: H5 web pages
- **代码相关/前端/前端代码/聚合页/**: Aggregation pages
- **叮咚对接文档/**: Integration documentation
- **叮咚推广文档/**: Marketing documentation
- **数据库备份/**: Database backups

### Technology Stack

#### Backend (Spring Boot)
- **Framework**: RuoYi v3.8.8, Spring Boot 2.5.15, Spring Security 5.7.12
- **Database**: MySQL with MyBatis Plus 3.5.7, Druid connection pool
- **Cache**: Redis
- **Authentication**: JWT tokens
- **Message Queue**: RocketMQ (in mc-order module)
- **API Documentation**: Swagger 3.0.0

#### Frontend Admin Dashboard (Vue.js 2)
- **Framework**: Vue.js 2.7.16, Element UI 2.15.14
- **Build**: Vue CLI 4.4.6
- **State Management**: Vuex 3.6.0
- **HTTP Client**: Axios 0.28.1

#### Mobile App (uni-app)
- **Framework**: uni-app with Vue.js 2
- **UI Library**: uview-ui 2.0.31
- **Charts**: ECharts 5.6.0
- **Platforms**: WeChat Mini Program, H5, Native Apps

## Common Development Commands

### Backend (Spring Boot)
```bash
# Navigate to backend directory
cd "代码相关/后端/number-card"

# Build and package (skip tests)
mvn clean package -Dmaven.test.skip=true

# Run application (port 8080)
java -jar ruoyi-admin/target/number-card.jar

# Windows batch scripts
bin/package.bat  # Build project
bin/run.bat      # Run application
bin/clean.bat    # Clean project
```

### Frontend Admin Dashboard
```bash
# Navigate to admin dashboard
cd "代码相关/前端/前端代码/电脑版后台"

# Install dependencies (fix permissions first if needed)
sudo chown -R 501:20 "/Users/zhao/.npm"
npm install

# Development server
npm run dev

# Production build
npm run build:prod

# Staging build
npm run build:stage

# Lint code
npm run lint
```

### Mobile App (uni-app)
```bash
# Navigate to mobile app
cd "代码相关/前端/前端代码/手机版"

# Development typically done through HBuilderX or uni-app CLI
# H5 development server runs on port 9090 (configured in manifest.json)
```

## Business Domain

### Core Functionality
- **Agent Management**: Agent accounts, hierarchies, and commission systems
- **Product Catalog**: Virtual number card products and configurations
- **Order Processing**: Order management with commission tracking
- **Withdrawal System**: Agent withdrawal applications and processing
- **Third-party Integrations**: Multiple upstream API providers (DouYin, GTH, WJK, etc.)

### Key Modules (Backend)
- **mc-console**: Core business console for number card management
- **mc-order**: Order processing with RocketMQ integration
- **ruoyi-system**: User/role/menu management
- **ruoyi-framework**: Security and configuration
- **ruoyi-common**: Utilities and third-party API integrations

## Important Notes

### Permission Issues
If encountering npm permission errors, run:
```bash
sudo chown -R 501:20 "/Users/zhao/.npm"
npm cache clean --force
```

### Configuration
- **Backend**: Configuration in `application.yml`, `application-dev.yml`, `application-release.yml`
- **Admin Dashboard**: Development server on port 8080
- **Mobile App**: H5 development on port 9090, WeChat Mini Program appid: wxccd7e2a0911b3397

### Existing Documentation
- Backend: Comprehensive CLAUDE.md exists at `代码相关/后端/number-card/CLAUDE.md`
- Mobile App: Detailed CLAUDE.md exists at `代码相关/前端/前端代码/手机版/CLAUDE.md`
- Project README: Standard RuoYi documentation at `代码相关/后端/number-card/README.md`

### Development Setup
1. Start backend: `cd "代码相关/后端/number-card" && mvn clean package && java -jar ruoyi-admin/target/number-card.jar`
2. Start admin dashboard: `cd "代码相关/前端/前端代码/电脑版后台" && npm run dev`
3. Mobile development: Use HBuilderX or uni-app CLI with `代码相关/前端/前端代码/手机版`

### Known Issues
- Vue 2 deprecation warnings (informational only)
- npm cache permission issues requiring sudo chown fix
- Node.js version compatibility (requires >=8.9)