<template>
  <div class="dashboard">
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon employment">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ latestEmploymentRate?.rate }}%</div>
              <div class="stat-label">最新就业率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon graduates">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ formatNumber(latestEmploymentRate?.graduateCount) }}</div>
              <div class="stat-label">2024年毕业生</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon salary">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ formatNumber(averageSalary) }}</div>
              <div class="stat-label">平均薪资</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon total">
              <el-icon><DataAnalysis /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ formatNumber(totalGraduates) }}</div>
              <div class="stat-label">总毕业生数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>就业率趋势</span>
              <el-button type="primary" size="small" @click="refreshData">
                <el-icon><Refresh /></el-icon>
                刷新
              </el-button>
            </div>
          </template>
          <v-chart 
            class="chart" 
            :option="employmentTrendOption" 
            :loading="loading"
            autoresize
          />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>行业分布</span>
          </template>
          <v-chart 
            class="chart" 
            :option="industryDistributionOption"
            :loading="loading"
            autoresize
          />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>热门技能需求</span>
          </template>
          <v-chart 
            class="chart" 
            :option="skillDemandOption"
            :loading="loading"
            autoresize
          />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>地区就业率排名</span>
          </template>
          <v-chart 
            class="chart" 
            :option="regionRankingOption"
            :loading="loading"
            autoresize
          />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>专业薪资对比</span>
          </template>
          <v-chart 
            class="chart-large" 
            :option="salaryComparisonOption"
            :loading="loading"
            autoresize
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useEmploymentStore } from '@/stores/employment'
import { ElMessage } from 'element-plus'

const employmentStore = useEmploymentStore()
const loading = ref(false)

// 计算属性
const latestEmploymentRate = computed(() => employmentStore.latestEmploymentRate)
const totalGraduates = computed(() => employmentStore.totalGraduates)
const averageSalary = computed(() => employmentStore.averageSalary)

// 格式化数字
const formatNumber = (num?: number) => {
  if (!num) return '0'
  if (num >= 10000000) {
    return (num / 10000000).toFixed(1) + '千万'
  } else if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  }
  return num.toLocaleString()
}

// 就业率趋势图配置
const employmentTrendOption = computed(() => ({
  title: {
    text: '近年来就业率变化趋势',
    left: 'center',
    textStyle: {
      fontSize: 14
    }
  },
  tooltip: {
    trigger: 'axis',
    formatter: (params: any) => {
      const data = params[0]
      return `${data.name}年<br/>就业率: ${data.value}%`
    }
  },
  xAxis: {
    type: 'category',
    data: employmentStore.getEmploymentTrend().map(item => item.year)
  },
  yAxis: {
    type: 'value',
    min: 80,
    max: 95,
    axisLabel: {
      formatter: '{value}%'
    }
  },
  series: [{
    data: employmentStore.getEmploymentTrend().map(item => item.rate),
    type: 'line',
    smooth: true,
    symbol: 'circle',
    symbolSize: 8,
    lineStyle: {
      width: 3,
      color: '#409EFF'
    },
    itemStyle: {
      color: '#409EFF'
    },
    areaStyle: {
      color: {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 0,
        y2: 1,
        colorStops: [{
          offset: 0, color: 'rgba(64, 158, 255, 0.3)'
        }, {
          offset: 1, color: 'rgba(64, 158, 255, 0.1)'
        }]
      }
    }
  }]
}))

// 行业分布饼图配置
const industryDistributionOption = computed(() => ({
  title: {
    text: '就业行业分布',
    left: 'center',
    textStyle: {
      fontSize: 14
    }
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c}% ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    top: 'middle'
  },
  series: [{
    name: '行业分布',
    type: 'pie',
    radius: ['40%', '70%'],
    center: ['60%', '50%'],
    avoidLabelOverlap: false,
    label: {
      show: false,
      position: 'center'
    },
    emphasis: {
      label: {
        show: true,
        fontSize: '18',
        fontWeight: 'bold'
      }
    },
    labelLine: {
      show: false
    },
    data: employmentStore.industryData.map(item => ({
      name: item.name,
      value: item.value
    }))
  }]
}))

// 技能需求雷达图配置
const skillDemandOption = computed(() => {
  const skills = employmentStore.getTopSkills(8)
  return {
    title: {
      text: '技能需求雷达图',
      left: 'center',
      textStyle: {
        fontSize: 14
      }
    },
    tooltip: {},
    radar: {
      indicator: skills.map(skill => ({
        name: skill.skill,
        max: 100
      })),
      center: ['50%', '55%'],
      radius: '70%'
    },
    series: [{
      name: '技能需求度',
      type: 'radar',
      data: [{
        value: skills.map(skill => skill.demand),
        name: '需求度',
        areaStyle: {
          color: 'rgba(64, 158, 255, 0.3)'
        },
        lineStyle: {
          color: '#409EFF',
          width: 2
        },
        itemStyle: {
          color: '#409EFF'
        }
      }]
    }]
  }
})

// 地区排名柱状图配置
const regionRankingOption = computed(() => {
  const regions = employmentStore.getTopRegions(8)
  return {
    title: {
      text: '地区就业率排名',
      left: 'center',
      textStyle: {
        fontSize: 14
      }
    },
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        const data = params[0]
        return `${data.name}<br/>就业率: ${data.value}%`
      }
    },
    xAxis: {
      type: 'category',
      data: regions.map(item => item.region),
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      min: 80,
      axisLabel: {
        formatter: '{value}%'
      }
    },
    series: [{
      data: regions.map(item => item.employmentRate),
      type: 'bar',
      itemStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0, color: '#67C23A'
          }, {
            offset: 1, color: '#85CE61'
          }]
        }
      }
    }]
  }
})

// 薪资对比柱状图配置
const salaryComparisonOption = computed(() => {
  const majors = employmentStore.getTopSalaryMajors(10)
  return {
    title: {
      text: '专业平均薪资对比',
      left: 'center',
      textStyle: {
        fontSize: 16
      }
    },
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        const data = params[0]
        const major = majors.find(m => m.major === data.name)
        return `${data.name}<br/>平均薪资: ¥${data.value}<br/>样本数量: ${major?.count.toLocaleString()}`
      }
    },
    xAxis: {
      type: 'category',
      data: majors.map(item => item.major),
      axisLabel: {
        rotate: 45,
        interval: 0
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '¥{value}'
      }
    },
    series: [{
      data: majors.map(item => item.avgSalary),
      type: 'bar',
      itemStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0, color: '#E6A23C'
          }, {
            offset: 1, color: '#F56C6C'
          }]
        }
      }
    }]
  }
})

// 刷新数据
const refreshData = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    ElMessage.success('数据已刷新')
  }, 1000)
}

onMounted(() => {
  // 页面加载时的初始化逻辑
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.stat-icon.employment {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.graduates {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.salary {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.total {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart {
  height: 300px;
}

.chart-large {
  height: 400px;
}

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.el-card__header) {
  padding: 18px 20px;
  border-bottom: 1px solid #ebeef5;
}
</style>