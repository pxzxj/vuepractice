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
