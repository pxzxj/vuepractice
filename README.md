# vuepractice

一个前后端分离的全栈练习项目：前端使用 Vue 3 + Vite + Element Plus，后端使用 Spring Boot + Spring Security + Spring Data JPA，实现了一个基于 Session 登录的简单后台管理示例（用户管理、角色查看）。

## 技术栈

| 层 | 技术 |
| --- | --- |
| 前端 | Vue 3、Vite、Vue Router、Pinia、Element Plus |
| 后端 | Spring Boot 2.7.18、Spring Security、Spring Data JPA（Hibernate） |
| 数据库 | H2（内存库，启动时通过 `schema.sql` / `data.sql` 初始化） |
| 构建 | Maven（后端）、npm / Vite（前端） |

## 目录结构

```
vuepractice/
├── backend/                 # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/
│       ├── java/io/github/pxzxj/
│       │   ├── BackendApplication.java        # 启动类
│       │   ├── config/SecurityConfig.java     # Spring Security 配置
│       │   └── user/
│       │       ├── controller/                # UserController、RoleController
│       │       ├── entity/                    # User、Role 实体
│       │       ├── repository/                # JPA Repository
│       │       └── service/                   # UserAuthenticationService
│       └── resources/
│           ├── application.yml
│           ├── schema.sql                     # 建表脚本
│           └── data.sql                       # 初始化数据
└── frontend/                # Vue 3 前端
    ├── package.json
    ├── vite.config.js       # 含 /api 反向代理配置
    └── src/
        ├── App.vue          # 根据登录状态切换登录页 / 主页
        ├── main.js
        ├── router/index.js
        ├── stores/          # Pinia store（principal）
        └── components/      # LoginPage、MainPage、User、Role
```

## 功能

- **登录 / 登出**：基于 Spring Security 的 Session 认证，未登录时只显示登录页。
- **用户管理**：分页列表、新增、编辑、删除；用户名重复校验；新增时密码经 BCrypt 加密。列表默认按 `id` 倒序。
- **角色查看**：分页展示角色列表。

## 前端 Vue 知识点

功能虽小，但覆盖了 Vue 3 组合式 API 的常见用法。下面按知识点列出对应的代码位置。

### 组件与单文件组件（SFC）

- 每个 `.vue` 文件由 `<script setup>` + `<template>` + `<style scoped>` 三块组成（`App.vue`、`components/*.vue`）。
- `<script setup>` 编译宏：顶层声明的变量 / 函数自动暴露给模板，无需 `return`。
- `<style scoped>`：样式只作用于当前组件。

### 响应式 API

| 能力 | 用法 | 位置 |
| --- | --- | --- |
| `ref` | 定义基本类型 / 数组状态；script 内用 `.value` 读写，模板内自动解包 | `username`、`tableData`、`currentPage`、`dialogFormVisible` 等 |
| `reactive` | 定义对象状态，属性可直接读写 | `User.vue` 的表单 `form` |
| `computed` | 派生状态 | `stores/counter.js` 的 `doubleCount`（示例，当前未被引用） |
| `onMounted` | 组件挂载后加载数据 | `User.vue`、`Role.vue` |

### 模板语法

- 插值 `{{ }}`：`{{ principalStore.principal }}`、`{{ $route.fullPath }}`。
- 双向绑定 `v-model`：`v-model="username"`、`v-model="dialogFormVisible"`；带参数形式 `v-model:current-page`、`v-model:page-size`（分页组件）。
- 条件渲染 `v-if` / `v-else`：`App.vue` 依据登录态切换登录页 / 主页；`LoginPage.vue` 按需显示报错信息。
- 属性绑定 `v-bind`（`:` 简写）：`:data`、`:total`、`:prop`、`:label-width`。
- 事件绑定 `v-on`（`@` 简写）与修饰符：`@click`、`@select`、`@size-change`、`@current-change`，以及 `@submit.prevent="login"`。
- 插槽：作用域插槽 `<template #default="scope">` 取出 `scope.$index` / `scope.row`；具名插槽 `<template #footer>`。

### 组件

- 组件引入与嵌套：`App.vue` 使用 `LoginPage` / `MainPage`，`MainPage.vue` 用 `<RouterView />` 承载子页面。
- 全局组件库：`main.js` 中 `app.use(ElementPlus)` 注册 Element Plus，模板里直接使用 `el-table`、`el-dialog`、`el-pagination` 等组件，脚本里使用 `ElMessage` / `ElMessageBox`。

### 路由（Vue Router）

- `createRouter` + `createWebHistory` 创建 HTML5 history 模式路由，`base` 取自 `import.meta.env.BASE_URL`。
- 路由表：`/user` → `User.vue`，`/role` → `Role.vue`。
- 声明式导航 `<RouterLink to="/user">`，路由出口 `<RouterView />`。
- 模板全局属性 `$route`：`$route.fullPath` 显示当前完整路径。

### 状态管理（Pinia）

- Setup Store 写法：`defineStore('principal', () => { ... })`，内部用 `ref` 声明 state、用函数作为 action（`stores/principal.js`、`stores/counter.js`）。
- 在组件之外使用：`main.js` 中先 `app.use(createPinia())`，再 `usePrincipalStore()` 并在挂载前 `await principalStore.load()` 预加载登录态。
- store 实例是响应式的，模板中可直接读取 state 属性（无需 `.value`）：`principalStore.principal`。
- 注意 `setup store` 与选项式 store 的区别：前者读写 state 应使用 `principal.value`，`this.principal` 是选项式 store 的写法。

### Vite / 工程化

- 路径别名 `@` → `src`（`vite.config.js`），组件中写作 `import { usePrincipalStore } from '@/stores/principal'`。
- `import.meta.env.BASE_URL` 读取 Vite 注入的环境变量。
- `main.js` 使用顶层 `await`，在挂载前完成登录态加载。

> 尚未涉及、可作为后续练习：父子组件 `props` / `emit`、`watch` / `watchEffect`、`provide` / `inject`、路由懒加载与导航守卫、组件内 `computed` 的实际使用。

## 环境要求

- JDK 8 及以上
- Maven 3.6+
- Node.js `^22.18.0 || >=24.12.0`
- npm

## 快速开始

### 1. 启动后端

```sh
cd backend
mvn spring-boot:run
```

- 服务默认运行在 `http://localhost:8080`。
- 数据库为内存 H2，每次启动都会重新执行 `schema.sql` 与 `data.sql`，**数据不会持久化**。
- 内置账号：`admin` / `admin`。

### 2. 启动前端

```sh
cd frontend
npm install
npm run dev
```

- 开发服务器启动后访问控制台输出的地址（Vite 默认 `http://localhost:5173`）。
- `vite.config.js` 已将 `/api` 代理到 `http://localhost:8080`，因此本地开发无需额外配置跨域。

### 3. 构建前端产物

```sh
cd frontend
npm run build      # 产物输出到 frontend/dist
npm run preview    # 本地预览构建结果
```

## 接口一览

所有 `/api/**` 接口均需要登录（`/api/login` 除外）。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/login` | 表单登录，参数 `username`、`password`；成功返回文本 `success`，失败返回错误信息 |
| GET | `/api/user/principal` | 获取当前登录用户信息 |
| GET | `/api/user/page` | 用户分页查询，支持 `page`、`size`、`sort`，可选 `username` 模糊查询 |
| POST | `/api/user/save` | 新增 / 更新用户（表单参数，`id` 为空表示新增），成功返回 `success` |
| POST | `/api/user/delete` | 按 `id` 删除用户 |
| GET | `/api/role/page` | 角色分页查询 |

> 说明：受 Spring Security 保护，登录后浏览器会持有 JSESSIONID，前端通过 Cookie 自动携带会话。

## 说明

- `application.yml` 中 `spring.jpa.hibernate.ddl-auto` 设为 `none`，表结构完全由 `schema.sql` 管理；`spring.sql.init.mode=always` 保证每次启动都执行初始化脚本。
- `User` 实体实现了 Spring Security 的 `UserDetails` 接口，`UserAuthenticationService` 实现 `UserDetailsService` 完成用户加载。
- 目前项目未编写自动化测试。

---

> 备注：本 README.md 由 Qwen 生成。
