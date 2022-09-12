<span style="color:#FF4800;font-size:50px;font-weight:900">MS商城</span>

<span style="color:#aaa">MS SHOP</span>

---

## 简介

该项目主要是完成网上购物系统的开发，该系统目前分为首页、用户中心、订单中心、店铺管理4个模块。
本项目为前后端分离的项目。后端主要采用的框架为springmvc，并使用spring boot简化项目开发。在用户权鉴方面使用springsecurity完成角色权限的登录认证。在数据访问层使用mybatis-plus作为持久层框架。数据库采用mysql。缓存使用redis并配合lua应对较高并发。使用消息队列rabbitMq完成一定程度的削峰以及业务解耦提高性能。前端使用vue快速搭建页面。

## 需要开启的服务

1. mysql8,可以为低版本，需要自己改写配置文件
2. redis
3. rabbitMq,需要用到延迟队列插件
