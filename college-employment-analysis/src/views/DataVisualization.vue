<template>
  <div class="data-visualization-screen">
    <!-- 顶部标题栏 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">{{ currentTime }}</div>
      </div>
      <div class="header-center">
        <h1 class="main-title">大学生就业大数据可视化平台</h1>
        <div class="subtitle">University Graduate Employment Big Data Visualization Platform</div>
      </div>
      <div class="header-right">
        <div class="update-info">数据更新时间: {{ updateTime }}</div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="screen-content">
      <!-- 左侧面板 -->
      <div class="left-panel">
        <!-- 关键指标 -->
        <div class="panel-section">
          <div class="section-title">
            <div class="title-icon"></div>
            <span>关键指标</span>
          </div>
          <div class="metrics-grid">
            <div class="metric-card" v-for="metric in keyMetrics" :key="metric.id">
              <div class="metric-icon" :class="metric.iconClass">
                <div class="metric-number">{{ metric.value }}</div>
              </div>
              <div class="metric-label">{{ metric.label }}</div>
              <div class="metric-change" :class="metric.trend">
                {{ metric.change }}
              </div>
            </div>
          </div>
        </div>

        <!-- 专业分布 -->
        <div class="panel-section">
          <div class="section-title">
            <div class="title-icon"></div>
            <span>专业分布</span>
          </div>
          <div class="chart-container">
            <v-chart class="chart" :option="majorDistributionOption" />
          </div>
        </div>

        <!-- 实时数据流 -->
        <div class="panel-section">
          <div class="section-title">
            <div class="title-icon"></div>
            <span>实时数据</span>
          </div>
          <div class="data-stream">
            <div class="stream-item" v-for="item in dataStream" :key="item.id">
              <div class="stream-time">{{ item.time }}</div>
              <div class="stream-content">{{ item.content }}</div>
              <div class="stream-value" :class="item.type">{{ item.value }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中央地图区域 -->
      <div class="center-panel">
        <div class="map-container">
          <div class="section-title">
            <div class="title-icon"></div>
            <span>全国就业分布图</span>
          </div>
          <v-chart class="map-chart" :option="chinaMapOption" />
        </div>

        <!-- 底部趋势图 -->
        <div class="trend-container">
          <div class="section-title">
            <div class="title-icon"></div>
            <span>就业趋势分析</span>
          </div>
          <v-chart class="trend-chart" :option="trendOption" />
        </div>
      </div>

      <!-- 右侧面板 -->
      <div class="right-panel">
        <!-- 薪资分析 -->
        <div class="panel-section">
          <div class="section-title">
            <div class="title-icon"></div>
            <span>薪资分析</span>
          </div>
          <div class="chart-container">
            <v-chart class="chart" :option="salaryAnalysisOption" />
          </div>
        </div>

        <!-- 行业排名 -->
        <div class="panel-section">
          <div class="section-title">
            <div class="title-icon"></div>
            <span>热门行业</span>
          </div>
          <div class="industry-ranking">
            <div class="ranking-item" v-for="(industry, index) in industryRanking" :key="industry.name">
              <div class="rank-number">{{ index + 1 }}</div>
              <div class="industry-name">{{ industry.name }}</div>
              <div class="industry-progress">
                <div class="progress-bar" :style="{ width: industry.percentage + '%' }"></div>
              </div>
              <div class="industry-value">{{ industry.percentage }}%</div>
            </div>
          </div>
        </div>

        <!-- 技能需求 -->
        <div class="panel-section">
          <div class="section-title">
            <div class="title-icon"></div>
            <span>技能需求</span>
          </div>
          <div class="skills-cloud">
            <div class="skill-tag" 
                 v-for="skill in skillsData" 
                 :key="skill.name"
                 :style="{ fontSize: skill.size + 'px', color: skill.color }">
              {{ skill.name }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部状态栏 -->
    <div class="screen-footer">
      <div class="footer-left">
        <span class="status-indicator online"></span>
        <span>系统运行正常</span>
      </div>
      <div class="footer-center">
        <span>数据来源: 教育部、人社部、各大招聘平台</span>
      </div>
      <div class="footer-right">
        <span>在线用户: {{ onlineUsers }}</span>
      </div>
    </div>

    <!-- 装饰性粒子效果 -->
    <div class="particle-container">
      <div class="particle" v-for="n in 20" :key="n" :style="getParticleStyle()"></div>
    </div>

    <!-- 浮动导航控制面板 -->
    <div class="floating-nav" :class="{ 'nav-expanded': isNavExpanded }">
      <div class="nav-toggle" @click="toggleNav">
        <el-icon class="nav-icon">
          <Menu v-if="!isNavExpanded" />
          <Close v-else />
        </el-icon>
      </div>
      
      <div class="nav-menu" v-show="isNavExpanded">
        <div class="nav-title">功能导航</div>
        <div class="nav-grid">
          <div class="nav-item" @click="navigateTo('/dashboard')">
            <el-icon><Monitor /></el-icon>
            <span>数据概览</span>
          </div>
          <div class="nav-item" @click="navigateTo('/salary-analysis')">
            <el-icon><Money /></el-icon>
            <span>薪资分析</span>
          </div>
          <div class="nav-item" @click="navigateTo('/industry-analysis')">
            <el-icon><TrendCharts /></el-icon>
            <span>行业分析</span>
          </div>
          <div class="nav-item" @click="navigateTo('/region-analysis')">
            <el-icon><Location /></el-icon>
            <span>地区分析</span>
          </div>
          <div class="nav-item" @click="navigateTo('/reports/employment')">
            <el-icon><Document /></el-icon>
            <span>就业报告</span>
          </div>
          <div class="nav-item" @click="navigateTo('/reports/trend')">
            <el-icon><DataLine /></el-icon>
            <span>趋势报告</span>
          </div>
          <div class="nav-item" @click="navigateTo('/data-management')">
            <el-icon><FolderOpened /></el-icon>
            <span>数据管理</span>
          </div>
          <div class="nav-item" @click="navigateTo('/help')">
            <el-icon><QuestionFilled /></el-icon>
            <span>帮助中心</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 快捷操作按钮组 -->
    <div class="quick-actions">
      <el-tooltip content="全屏显示" placement="left">
        <div class="action-btn" @click="toggleFullscreen">
          <el-icon><FullScreen /></el-icon>
        </div>
      </el-tooltip>
      <el-tooltip content="刷新数据" placement="left">
        <div class="action-btn" @click="refreshData">
          <el-icon><Refresh /></el-icon>
        </div>
      </el-tooltip>
      <el-tooltip content="导出截图" placement="left">
        <div class="action-btn" @click="exportScreenshot">
          <el-icon><Camera /></el-icon>
        </div>
      </el-tooltip>
      <el-tooltip content="系统设置" placement="left">
        <div class="action-btn" @click="openSettings">
          <el-icon><Setting /></el-icon>
        </div>
      </el-tooltip>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useEmploymentStore } from '@/stores/employment'
import { ElMessage } from 'element-plus'
import { 
  Menu, 
  Close, 
  Monitor, 
  Money, 
  TrendCharts, 
  Location, 
  Document, 
  DataLine, 
  FolderOpened, 
  QuestionFilled, 
  FullScreen, 
  Refresh, 
  Camera, 
  Setting 
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import chinaMapData from '@/assets/china.json'

const router = useRouter()
const employmentStore = useEmploymentStore()

// 注册地图数据
echarts.registerMap('china', chinaMapData as any)

// 导航控制
const isNavExpanded = ref(false)

// 时间相关
const currentTime = ref('')
const updateTime = ref('')
const onlineUsers = ref(1247)

// 关键指标数据
const keyMetrics = ref([
  { id: 1, label: '就业率', value: '90.5%', change: '+1.4%', trend: 'up', iconClass: 'metric-employment' },
  { id: 2, label: '毕业生', value: '1179万', change: '+2.3%', trend: 'up', iconClass: 'metric-graduates' },
  { id: 3, label: '平均薪资', value: '11.2K', change: '+8.2%', trend: 'up', iconClass: 'metric-salary' },
  { id: 4, label: '岗位数', value: '856万', change: '+5.7%', trend: 'up', iconClass: 'metric-jobs' }
])

// 实时数据流
const dataStream = ref([
  { id: 1, time: '14:32:15', content: '北京地区新增就业岗位', value: '+126', type: 'increase' },
  { id: 2, time: '14:31:42', content: '上海IT行业薪资上涨', value: '+3.2%', type: 'increase' },
  { id: 3, time: '14:31:18', content: '深圳新增招聘企业', value: '+45', type: 'increase' },
  { id: 4, time: '14:30:56', content: '广州就业率提升', value: '+0.8%', type: 'increase' },
  { id: 5, time: '14:30:33', content: '杭州平均薪资调整', value: '+4.1%', type: 'increase' }
])

// 行业排名数据
const industryRanking = ref([
  { name: 'IT/互联网', percentage: 28.5 },
  { name: '金融', percentage: 15.2 },
  { name: '教育', percentage: 12.8 },
  { name: '制造业', percentage: 11.6 },
  { name: '医疗健康', percentage: 9.4 },
  { name: '房地产', percentage: 7.3 },
  { name: '咨询服务', percentage: 6.8 }
])

// 技能数据
const skillsData = ref([
  { name: 'Python', size: 24, color: '#00d4ff' },
  { name: 'Java', size: 22, color: '#ff6b9d' },
  { name: '数据分析', size: 20, color: '#ffa726' },
  { name: 'JavaScript', size: 18, color: '#66bb6a' },
  { name: '机器学习', size: 16, color: '#ab47bc' },
  { name: 'React', size: 15, color: '#26c6da' },
  { name: 'Vue', size: 14, color: '#ef5350' },
  { name: 'SQL', size: 13, color: '#ffee58' },
  { name: '产品设计', size: 12, color: '#8d6e63' },
  { name: '项目管理', size: 11, color: '#78909c' }
])

// 专业分布饼图配置
const majorDistributionOption = ref({
  backgroundColor: 'transparent',
  series: [{
    type: 'pie',
    radius: ['40%', '70%'],
    center: ['50%', '50%'],
    data: [
      { value: 28.5, name: '计算机', itemStyle: { color: '#00d4ff' } },
      { value: 15.2, name: '金融', itemStyle: { color: '#ff6b9d' } },
      { value: 12.8, name: '管理', itemStyle: { color: '#ffa726' } },
      { value: 11.6, name: '工程', itemStyle: { color: '#66bb6a' } },
      { value: 9.4, name: '医学', itemStyle: { color: '#ab47bc' } },
      { value: 22.5, name: '其他', itemStyle: { color: '#26c6da' } }
    ],
    label: {
      color: '#fff',
      fontSize: 12
    },
    labelLine: {
      lineStyle: {
        color: '#fff'
      }
    }
  }]
})

// 中国地图配置
const chinaMapOption = ref({
  backgroundColor: 'transparent',
  geo: {
    map: 'china',
    roam: false,
    zoom: 1.2,
    center: [105, 36],
    itemStyle: {
      borderColor: '#00d4ff',
      borderWidth: 1,
      areaColor: 'rgba(0, 212, 255, 0.1)'
    },
    emphasis: {
      itemStyle: {
        areaColor: 'rgba(0, 212, 255, 0.3)'
      }
    }
  },
  series: [
    {
      type: 'scatter',
      coordinateSystem: 'geo',
      data: [
        { name: '北京', value: [116.46, 39.92, 94.2] },
        { name: '上海', value: [121.48, 31.22, 93.8] },
        { name: '深圳', value: [114.07, 22.62, 92.5] },
        { name: '广州', value: [113.23, 23.16, 91.3] },
        { name: '杭州', value: [120.19, 30.26, 90.8] },
        { name: '南京', value: [118.78, 32.04, 89.6] },
        { name: '成都', value: [104.06, 30.67, 88.7] },
        { name: '武汉', value: [114.31, 30.52, 87.9] }
      ],
      symbolSize: function (val: number[]) {
        return val[2] / 5
      },
      itemStyle: {
        color: '#ff6b9d',
        shadowBlur: 10,
        shadowColor: '#ff6b9d'
      }
    }
  ]
})

// 趋势图配置
const trendOption = ref({
  backgroundColor: 'transparent',
  grid: {
    left: '5%',
    right: '5%',
    bottom: '10%',
    top: '10%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: ['2019', '2020', '2021', '2022', '2023', '2024'],
    axisLine: {
      lineStyle: { color: '#00d4ff' }
    },
    axisLabel: {
      color: '#fff'
    }
  },
  yAxis: {
    type: 'value',
    axisLine: {
      lineStyle: { color: '#00d4ff' }
    },
    axisLabel: {
      color: '#fff'
    },
    splitLine: {
      lineStyle: {
        color: 'rgba(0, 212, 255, 0.1)'
      }
    }
  },
  series: [
    {
      data: [85.2, 82.1, 84.8, 87.3, 89.1, 90.5],
      type: 'line',
      smooth: true,
      lineStyle: {
        color: '#00d4ff',
        width: 3,
        shadowColor: '#00d4ff',
        shadowBlur: 10
      },
      itemStyle: {
        color: '#00d4ff',
        shadowColor: '#00d4ff',
        shadowBlur: 10
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(0, 212, 255, 0.3)' },
            { offset: 1, color: 'rgba(0, 212, 255, 0.05)' }
          ]
        }
      }
    }
  ]
})

// 薪资分析配置
const salaryAnalysisOption = ref({
  backgroundColor: 'transparent',
  grid: {
    left: '5%',
    right: '5%',
    bottom: '10%',
    top: '10%',
    containLabel: true
  },
  xAxis: {
    type: 'value',
    axisLine: {
      lineStyle: { color: '#00d4ff' }
    },
    axisLabel: {
      color: '#fff'
    },
    splitLine: {
      lineStyle: {
        color: 'rgba(0, 212, 255, 0.1)'
      }
    }
  },
  yAxis: {
    type: 'category',
    data: ['人工智能', '大数据', '云计算', '区块链', '物联网'],
    axisLine: {
      lineStyle: { color: '#00d4ff' }
    },
    axisLabel: {
      color: '#fff'
    }
  },
  series: [{
    data: [14200, 13500, 12800, 11600, 10800],
    type: 'bar',
    itemStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 1, y2: 0,
        colorStops: [
          { offset: 0, color: '#ff6b9d' },
          { offset: 1, color: '#ffa726' }
        ]
      }
    }
  }]
})

// 粒子效果
const getParticleStyle = () => {
  return {
    left: Math.random() * 100 + '%',
    top: Math.random() * 100 + '%',
    animationDelay: Math.random() * 2 + 's',
    animationDuration: (Math.random() * 3 + 2) + 's'
  }
}

// 更新时间
const updateCurrentTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 更新数据流
const updateDataStream = () => {
  const templates = [
    '北京地区新增就业岗位',
    '上海IT行业薪资上涨',
    '深圳新增招聘企业',
    '广州就业率提升',
    '杭州平均薪资调整'
  ]
  
  const newItem = {
    id: Date.now(),
    time: new Date().toLocaleTimeString(),
    content: templates[Math.floor(Math.random() * templates.length)],
    value: '+' + (Math.random() * 100).toFixed(1) + (Math.random() > 0.5 ? '%' : ''),
    type: 'increase'
  }
  
  dataStream.value.unshift(newItem)
  if (dataStream.value.length > 5) {
    dataStream.value.pop()
  }
}

// 导航功能
const toggleNav = () => {
  isNavExpanded.value = !isNavExpanded.value
}

const navigateTo = (path: string) => {
  router.push(path)
  isNavExpanded.value = false
}

// 快捷操作功能
const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen().catch(() => {
      ElMessage.warning('无法进入全屏模式')
    })
  } else {
    document.exitFullscreen()
  }
}

const refreshData = () => {
  // 刷新所有数据
  updateDataStream()
  ElMessage.success('数据已刷新')
}

const exportScreenshot = () => {
  ElMessage.info('截图功能开发中...')
}

const openSettings = () => {
  ElMessage.info('设置功能开发中...')
}

let timeInterval: NodeJS.Timeout
let dataInterval: NodeJS.Timeout

onMounted(() => {
  updateCurrentTime()
  updateTime.value = '2024-01-15 14:30:00'
  
  timeInterval = setInterval(updateCurrentTime, 1000)
  dataInterval = setInterval(updateDataStream, 3000)
})

onUnmounted(() => {
  if (timeInterval) clearInterval(timeInterval)
  if (dataInterval) clearInterval(dataInterval)
})
</script>

<style scoped>
.data-visualization-screen {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #0c1445 0%, #1e3c72 50%, #2a5298 100%);
  color: #ffffff;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  overflow: hidden;
  position: relative;
}

/* 顶部标题栏 */
.screen-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 30px;
  background: rgba(0, 0, 0, 0.3);
  border-bottom: 2px solid #00d4ff;
  box-shadow: 0 2px 10px rgba(0, 212, 255, 0.3);
}

.header-left, .header-right {
  flex: 1;
  font-size: 14px;
  color: #00d4ff;
}

.header-right {
  text-align: right;
}

.header-center {
  flex: 2;
  text-align: center;
}

.main-title {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
  background: linear-gradient(45deg, #00d4ff, #ff6b9d);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0 0 20px rgba(0, 212, 255, 0.5);
}

.subtitle {
  font-size: 14px;
  color: #a0c4ff;
  margin-top: 5px;
}

/* 主要内容区域 */
.screen-content {
  display: flex;
  height: calc(100vh - 140px);
  padding: 20px;
  gap: 20px;
}

.left-panel, .right-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.center-panel {
  flex: 2;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 面板区块 */
.panel-section {
  background: rgba(0, 0, 0, 0.4);
  border: 1px solid #00d4ff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 4px 15px rgba(0, 212, 255, 0.2);
  backdrop-filter: blur(10px);
}

.section-title {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: bold;
  color: #00d4ff;
}

.title-icon {
  width: 4px;
  height: 16px;
  background: linear-gradient(to bottom, #00d4ff, #ff6b9d);
  margin-right: 10px;
  border-radius: 2px;
}

/* 关键指标 */
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.metric-card {
  text-align: center;
  padding: 15px;
  background: rgba(0, 212, 255, 0.1);
  border-radius: 8px;
  border: 1px solid rgba(0, 212, 255, 0.3);
}

.metric-icon {
  width: 60px;
  height: 60px;
  margin: 0 auto 10px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(45deg, #00d4ff, #ff6b9d);
  box-shadow: 0 4px 15px rgba(0, 212, 255, 0.4);
}

.metric-number {
  font-size: 18px;
  font-weight: bold;
  color: #fff;
}

.metric-label {
  font-size: 12px;
  color: #a0c4ff;
  margin-bottom: 5px;
}

.metric-change {
  font-size: 12px;
  font-weight: bold;
}

.metric-change.up {
  color: #4caf50;
}

.metric-change.down {
  color: #f44336;
}

/* 图表容器 */
.chart-container {
  height: 200px;
}

.chart {
  width: 100%;
  height: 100%;
}

/* 地图容器 */
.map-container {
  flex: 2;
  background: rgba(0, 0, 0, 0.4);
  border: 1px solid #00d4ff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 4px 15px rgba(0, 212, 255, 0.2);
}

.map-chart {
  width: 100%;
  height: calc(100% - 40px);
}

/* 趋势容器 */
.trend-container {
  flex: 1;
  background: rgba(0, 0, 0, 0.4);
  border: 1px solid #00d4ff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 4px 15px rgba(0, 212, 255, 0.2);
}

.trend-chart {
  width: 100%;
  height: calc(100% - 40px);
}

/* 实时数据流 */
.data-stream {
  max-height: 200px;
  overflow-y: auto;
}

.stream-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid rgba(0, 212, 255, 0.1);
  font-size: 12px;
}

.stream-time {
  color: #666;
  width: 60px;
}

.stream-content {
  flex: 1;
  color: #a0c4ff;
  margin: 0 10px;
}

.stream-value {
  font-weight: bold;
  width: 50px;
  text-align: right;
}

.stream-value.increase {
  color: #4caf50;
}

/* 行业排名 */
.industry-ranking {
  space-y: 10px;
}

.ranking-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
  font-size: 12px;
}

.rank-number {
  width: 24px;
  height: 24px;
  background: linear-gradient(45deg, #ff6b9d, #ffa726);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-right: 10px;
}

.industry-name {
  width: 60px;
  color: #a0c4ff;
}

.industry-progress {
  flex: 1;
  height: 6px;
  background: rgba(0, 212, 255, 0.1);
  border-radius: 3px;
  margin: 0 10px;
  position: relative;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(to right, #00d4ff, #ff6b9d);
  border-radius: 3px;
  transition: width 0.3s ease;
}

.industry-value {
  width: 40px;
  text-align: right;
  color: #00d4ff;
  font-weight: bold;
}

/* 技能云 */
.skills-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
  align-items: center;
  height: 150px;
}

.skill-tag {
  padding: 4px 8px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid currentColor;
}

.skill-tag:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 15px currentColor;
}

/* 底部状态栏 */
.screen-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 30px;
  background: rgba(0, 0, 0, 0.3);
  border-top: 1px solid #00d4ff;
  font-size: 12px;
  color: #a0c4ff;
}

.status-indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 8px;
  animation: pulse 2s infinite;
}

.status-indicator.online {
  background: #4caf50;
}

/* 粒子效果 */
.particle-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.particle {
  position: absolute;
  width: 2px;
  height: 2px;
  background: #00d4ff;
  border-radius: 50%;
  animation: float linear infinite;
}

/* 动画效果 */
@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

@keyframes float {
  0% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100px) rotate(360deg);
    opacity: 0;
  }
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 4px;
}

::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.1);
}

::-webkit-scrollbar-thumb {
  background: #00d4ff;
  border-radius: 2px;
}

::-webkit-scrollbar-thumb:hover {
  background: #ff6b9d;
}

/* 浮动导航面板 */
.floating-nav {
  position: fixed;
  top: 50%;
  left: 20px;
  transform: translateY(-50%);
  z-index: 1000;
  transition: all 0.3s ease;
}

.nav-toggle {
  width: 50px;
  height: 50px;
  background: rgba(0, 0, 0, 0.8);
  border: 2px solid #00d4ff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 20px rgba(0, 212, 255, 0.4);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.nav-toggle:hover {
  background: rgba(0, 212, 255, 0.2);
  transform: scale(1.1);
}

.nav-icon {
  font-size: 20px;
  color: #00d4ff;
}

.nav-menu {
  position: absolute;
  left: 70px;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(0, 0, 0, 0.9);
  border: 1px solid #00d4ff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 8px 32px rgba(0, 212, 255, 0.3);
  backdrop-filter: blur(20px);
  min-width: 320px;
  animation: slideInLeft 0.3s ease;
}

.nav-title {
  font-size: 16px;
  font-weight: bold;
  color: #00d4ff;
  margin-bottom: 15px;
  text-align: center;
  border-bottom: 1px solid rgba(0, 212, 255, 0.3);
  padding-bottom: 10px;
}

.nav-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px 10px;
  background: rgba(0, 212, 255, 0.1);
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 12px;
  color: #a0c4ff;
}

.nav-item:hover {
  background: rgba(0, 212, 255, 0.2);
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 212, 255, 0.4);
  color: #00d4ff;
}

.nav-item .el-icon {
  font-size: 24px;
  margin-bottom: 8px;
  color: #00d4ff;
}

/* 快捷操作按钮组 */
.quick-actions {
  position: fixed;
  top: 50%;
  right: 20px;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  gap: 15px;
  z-index: 1000;
}

.action-btn {
  width: 50px;
  height: 50px;
  background: rgba(0, 0, 0, 0.8);
  border: 2px solid #ff6b9d;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 20px rgba(255, 107, 157, 0.4);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.action-btn:hover {
  background: rgba(255, 107, 157, 0.2);
  transform: scale(1.1);
}

.action-btn .el-icon {
  font-size: 20px;
  color: #ff6b9d;
}

/* 动画效果 */
@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-20px) translateY(-50%);
  }
  to {
    opacity: 1;
    transform: translateX(0) translateY(-50%);
  }
}
</style>