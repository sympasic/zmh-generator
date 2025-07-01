<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  DataAnalysis, 
  Refresh, 
  Setting, 
  User, 
  ArrowDown, 
  Monitor, 
  TrendCharts, 
  Money, 
  Grid, 
  Location, 
  Document, 
  DataLine, 
  FolderOpened, 
  QuestionFilled,
  Monitor
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

// 判断是否为数据可视化大屏页面
const isDataVisualizationPage = computed(() => {
  return route.path === '/data-visualization'
})

// 面包屑导航
const currentBreadcrumb = computed(() => {
  const breadcrumbMap: Record<string, string> = {
    '/dashboard': '数据概览',
    '/salary-analysis': '薪资分析',
    '/industry-analysis': '行业分析',
    '/region-analysis': '地区分析',
    '/reports/employment': '就业报告',
    '/reports/trend': '趋势报告',
    '/data-management': '数据管理',
    '/data-visualization': '数据可视化大屏',
    '/help': '帮助中心'
  }
  return breadcrumbMap[route.path] || ''
})

// 返回数据可视化大屏
const goToDataVisualization = () => {
  router.push('/data-visualization')
}

// 刷新数据
const refreshData = () => {
  ElMessage.success('数据刷新成功')
}

// 显示设置
const showSettings = () => {
  ElMessage.info('设置功能开发中...')
}

// 用户菜单操作
const handleUserCommand = (command: string) => {
  if (command === 'profile') {
    ElMessage.info('个人信息功能开发中...')
  } else if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      ElMessage.success('已退出登录')
    })
  }
}

onMounted(() => {
  // 页面加载完成后的初始化操作
})
</script>

<template>
  <el-container class="app-container">
    <!-- 顶部导航栏 -->
    <el-header class="app-header" v-if="!isDataVisualizationPage">
      <div class="header-left">
        <div class="logo">
          <el-icon class="logo-icon"><DataAnalysis /></el-icon>
          <span class="logo-text">大学生就业大数据分析平台</span>
        </div>
      </div>
      <div class="header-right">
        <el-button text @click="goToDataVisualization">
          <el-icon><Monitor /></el-icon>
          返回大屏
        </el-button>
        <el-button text @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新数据
        </el-button>
        <el-button text @click="showSettings">
          <el-icon><Setting /></el-icon>
          设置
        </el-button>
        <el-dropdown @command="handleUserCommand">
          <span class="user-dropdown">
            <el-avatar :size="32" src="/avatar.jpg">
              <el-icon><User /></el-icon>
            </el-avatar>
            <span class="username">管理员</span>
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container v-if="!isDataVisualizationPage">
      <!-- 左侧菜单 -->
      <el-aside width="250px" class="app-aside">
        <el-menu
          :default-active="$route.path"
          class="sidebar-menu"
          router
          unique-opened
        >
          <el-menu-item index="/dashboard">
            <el-icon><Monitor /></el-icon>
            <span>数据概览</span>
          </el-menu-item>
          
          <el-sub-menu index="analysis">
            <template #title>
              <el-icon><TrendCharts /></el-icon>
              <span>深度分析</span>
            </template>
            <el-menu-item index="/salary-analysis">
              <el-icon><Money /></el-icon>
              <span>薪资分析</span>
            </el-menu-item>
            <el-menu-item index="/industry-analysis">
              <el-icon><Grid /></el-icon>
              <span>行业分析</span>
            </el-menu-item>
            <el-menu-item index="/region-analysis">
              <el-icon><Location /></el-icon>
              <span>地区分析</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="reports">
            <template #title>
              <el-icon><Document /></el-icon>
              <span>报告中心</span>
            </template>
            <el-menu-item index="/reports/employment">
              <el-icon><DataLine /></el-icon>
              <span>就业报告</span>
            </el-menu-item>
            <el-menu-item index="/reports/trend">
              <el-icon><TrendCharts /></el-icon>
              <span>趋势报告</span>
            </el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/data-management">
            <el-icon><FolderOpened /></el-icon>
            <span>数据管理</span>
          </el-menu-item>

                      <el-menu-item index="/data-visualization">
              <el-icon><Monitor /></el-icon>
              <span>数据大屏</span>
            </el-menu-item>
            <el-menu-item index="/help">
              <el-icon><QuestionFilled /></el-icon>
              <span>帮助中心</span>
            </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主要内容区域 -->
      <el-main class="app-main">
        <div class="breadcrumb-container">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="currentBreadcrumb">{{ currentBreadcrumb }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        
        <div class="main-content">
          <RouterView />
        </div>
      </el-main>
    </el-container>

    <!-- 页脚 -->
    <el-footer class="app-footer" v-if="!isDataVisualizationPage">
      <div class="footer-content">
        <span>© 2024 大学生就业大数据分析平台. All rights reserved.</span>
        <span class="footer-links">
          <a href="#" @click.prevent>隐私政策</a>
          <a href="#" @click.prevent>服务条款</a>
          <a href="#" @click.prevent>联系我们</a>
        </span>
      </div>
    </el-footer>

    <!-- 数据可视化大屏单独渲染 -->
    <RouterView v-if="isDataVisualizationPage" class="fullscreen-view" />
  </el-container>
</template>

<style scoped>
.app-container {
  height: 100vh;
}

.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
}

.logo-icon {
  font-size: 28px;
  margin-right: 10px;
  color: #FFD700;
}

.logo-text {
  color: white;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.username {
  color: white;
  font-size: 14px;
}

.app-aside {
  background-color: #f8f9fa;
  border-right: 1px solid #e9ecef;
}

.sidebar-menu {
  border-right: none;
  height: 100%;
}

.app-main {
  background-color: #f5f7fa;
  padding: 0;
}

.breadcrumb-container {
  background: white;
  padding: 15px 20px;
  border-bottom: 1px solid #e9ecef;
  margin-bottom: 0;
}

.main-content {
  min-height: calc(100vh - 120px - 60px - 50px); /* 减去header、footer、breadcrumb高度 */
}

.app-footer {
  background-color: #ffffff;
  border-top: 1px solid #e9ecef;
  height: 50px;
  line-height: 50px;
  padding: 0 20px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #666;
}

.footer-links {
  display: flex;
  gap: 20px;
}

.footer-links a {
  color: #666;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: #409EFF;
}

:deep(.el-menu-item) {
  height: 48px;
  line-height: 48px;
}

:deep(.el-sub-menu .el-menu-item) {
  height: 40px;
  line-height: 40px;
  padding-left: 50px !important;
}

:deep(.el-menu-item.is-active) {
  background-color: #ecf5ff;
  color: #409EFF;
}

:deep(.el-menu-item:hover) {
  background-color: #ecf5ff;
  color: #409EFF;
}

:deep(.el-button.is-text) {
  color: white;
}

:deep(.el-button.is-text:hover) {
  background-color: rgba(255, 255, 255, 0.1);
}

/* 全屏视图样式 */
.fullscreen-view {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 999;
}
</style>
