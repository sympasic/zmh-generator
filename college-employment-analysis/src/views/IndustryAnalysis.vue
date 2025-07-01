<template>
  <div class="industry-analysis">
    <el-row :gutter="20" class="mb-20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>行业分析筛选</span>
              <el-button-group>
                <el-button 
                  :type="viewMode === 'chart' ? 'primary' : 'default'" 
                  @click="viewMode = 'chart'"
                >
                  图表视图
                </el-button>
                <el-button 
                  :type="viewMode === 'table' ? 'primary' : 'default'" 
                  @click="viewMode = 'table'"
                >
                  表格视图
                </el-button>
              </el-button-group>
            </div>
          </template>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-select v-model="selectedIndustry" placeholder="选择行业" clearable @change="updateCharts">
                <el-option label="全部行业" value="" />
                <el-option 
                  v-for="industry in industries" 
                  :key="industry" 
                  :label="industry" 
                  :value="industry" 
                />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="sortBy" placeholder="排序方式" @change="updateCharts">
                <el-option label="按就业人数" value="value" />
                <el-option label="按增长率" value="growth" />
                <el-option label="按行业名称" value="name" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="growthFilter" placeholder="增长趋势" @change="updateCharts">
                <el-option label="全部趋势" value="" />
                <el-option label="增长行业" value="positive" />
                <el-option label="下降行业" value="negative" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="generateReport">
                <el-icon><Document /></el-icon>
                生成报告
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon industry-count">
              <el-icon><Grid /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ filteredIndustries.length }}</div>
              <div class="stat-label">行业数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon growth-rate">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ averageGrowth.toFixed(1) }}%</div>
              <div class="stat-label">平均增长率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon top-industry">
              <el-icon><Trophy /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ topIndustry?.name || '-' }}</div>
              <div class="stat-label">最大行业</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon fastest-growth">
              <el-icon><Lightning /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ fastestGrowthIndustry?.name || '-' }}</div>
              <div class="stat-label">增长最快</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div v-if="viewMode === 'chart'">
      <el-row :gutter="20" class="mb-20">
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>行业就业分布</span>
            </template>
            <v-chart 
              class="chart" 
              :option="industryDistributionOption"
              :loading="loading"
              autoresize
            />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>行业增长率对比</span>
            </template>
            <v-chart 
              class="chart" 
              :option="industryGrowthOption"
              :loading="loading"
              autoresize
            />
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="mb-20">
        <el-col :span="24">
          <el-card>
            <template #header>
              <span>行业就业人数与增长率气泡图</span>
            </template>
            <v-chart 
              class="chart-large" 
              :option="industryBubbleOption"
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
              <span>行业发展趋势</span>
            </template>
            <v-chart 
              class="chart" 
              :option="industryTrendOption"
              :loading="loading"
              autoresize
            />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>新兴行业对比</span>
            </template>
            <v-chart 
              class="chart" 
              :option="emergingIndustryOption"
              :loading="loading"
              autoresize
            />
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div v-if="viewMode === 'table'">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>行业详细数据</span>
                <el-input
                  v-model="searchText"
                  placeholder="搜索行业..."
                  style="width: 200px"
                  clearable
                >
                  <template #prefix>
                    <el-icon><Search /></el-icon>
                  </template>
                </el-input>
              </div>
            </template>
            <el-table :data="filteredTableData" stripe>
              <el-table-column prop="name" label="行业名称" sortable />
              <el-table-column prop="value" label="就业比例" sortable>
                <template #default="{ row }">
                  {{ row.value }}%
                </template>
              </el-table-column>
              <el-table-column prop="growth" label="增长率" sortable>
                <template #default="{ row }">
                  <el-tag :type="row.growth > 0 ? 'success' : 'danger'">
                    {{ row.growth > 0 ? '+' : '' }}{{ row.growth }}%
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="就业人数" sortable>
                <template #default="{ row }">
                  {{ calculateEmploymentCount(row.value).toLocaleString() }}
                </template>
              </el-table-column>
              <el-table-column label="发展前景">
                <template #default="{ row }">
                  <el-rate 
                    v-model="getProspectRating(row.growth)" 
                    disabled 
                    show-score 
                    text-color="#ff9900"
                  />
                </template>
              </el-table-column>
              <el-table-column label="操作">
                <template #default="{ row }">
                  <el-button size="small" @click="viewIndustryDetail(row)">
                    详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 行业详情对话框 -->
    <el-dialog v-model="showDetailDialog" title="行业详情" width="60%">
      <div v-if="selectedIndustryDetail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="行业名称">
            {{ selectedIndustryDetail.name }}
          </el-descriptions-item>
          <el-descriptions-item label="就业比例">
            {{ selectedIndustryDetail.value }}%
          </el-descriptions-item>
          <el-descriptions-item label="增长率">
            <el-tag :type="selectedIndustryDetail.growth > 0 ? 'success' : 'danger'">
              {{ selectedIndustryDetail.growth > 0 ? '+' : '' }}{{ selectedIndustryDetail.growth }}%
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="就业人数">
            {{ calculateEmploymentCount(selectedIndustryDetail.value).toLocaleString() }}
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="mt-20">
          <h4>行业特点</h4>
          <p>{{ getIndustryDescription(selectedIndustryDetail.name) }}</p>
        </div>
        
        <div class="mt-20">
          <h4>技能要求</h4>
          <el-tag 
            v-for="skill in getIndustrySkills(selectedIndustryDetail.name)" 
            :key="skill" 
            class="mr-10 mb-10"
          >
            {{ skill }}
          </el-tag>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useEmploymentStore } from '@/stores/employment'
import { ElMessage } from 'element-plus'

const employmentStore = useEmploymentStore()
const loading = ref(false)
const viewMode = ref<'chart' | 'table'>('chart')
const selectedIndustry = ref('')
const sortBy = ref('value')
const growthFilter = ref('')
const searchText = ref('')
const showDetailDialog = ref(false)
const selectedIndustryDetail = ref<any>(null)

// 计算属性
const industries = computed(() => 
  employmentStore.industryData.map(item => item.name)
)

const filteredIndustries = computed(() => {
  let data = [...employmentStore.industryData]
  
  if (selectedIndustry.value) {
    data = data.filter(item => item.name === selectedIndustry.value)
  }
  
  if (growthFilter.value === 'positive') {
    data = data.filter(item => item.growth > 0)
  } else if (growthFilter.value === 'negative') {
    data = data.filter(item => item.growth < 0)
  }
  
  // 排序
  if (sortBy.value === 'value') {
    data.sort((a, b) => b.value - a.value)
  } else if (sortBy.value === 'growth') {
    data.sort((a, b) => b.growth - a.growth)
  } else if (sortBy.value === 'name') {
    data.sort((a, b) => a.name.localeCompare(b.name))
  }
  
  return data
})

const averageGrowth = computed(() => {
  const sum = filteredIndustries.value.reduce((acc, item) => acc + item.growth, 0)
  return filteredIndustries.value.length > 0 ? sum / filteredIndustries.value.length : 0
})

const topIndustry = computed(() => 
  filteredIndustries.value.reduce((max, item) => 
    item.value > (max?.value || 0) ? item : max, null)
)

const fastestGrowthIndustry = computed(() => 
  filteredIndustries.value.reduce((max, item) => 
    item.growth > (max?.growth || -Infinity) ? item : max, null)
)

const filteredTableData = computed(() => {
  let data = filteredIndustries.value
  if (searchText.value) {
    data = data.filter(item => 
      item.name.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }
  return data
})

// 行业分布饼图配置
const industryDistributionOption = computed(() => ({
  title: {
    text: '行业就业分布',
    left: 'center'
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
    data: filteredIndustries.value.map(item => ({
      name: item.name,
      value: item.value
    })),
    emphasis: {
      itemStyle: {
        shadowBlur: 10,
        shadowOffsetX: 0,
        shadowColor: 'rgba(0, 0, 0, 0.5)'
      }
    }
  }]
}))

// 行业增长率柱状图配置
const industryGrowthOption = computed(() => ({
  title: {
    text: '行业增长率对比',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    formatter: (params: any) => {
      const data = params[0]
      return `${data.name}<br/>增长率: ${data.value}%`
    }
  },
  xAxis: {
    type: 'category',
    data: filteredIndustries.value.map(item => item.name),
    axisLabel: {
      rotate: 45,
      interval: 0
    }
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: '{value}%'
    }
  },
  series: [{
    data: filteredIndustries.value.map(item => ({
      value: item.growth,
      itemStyle: {
        color: item.growth > 0 ? '#67C23A' : '#F56C6C'
      }
    })),
    type: 'bar'
  }]
}))

// 气泡图配置
const industryBubbleOption = computed(() => ({
  title: {
    text: '行业就业人数与增长率关系',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: (params: any) => {
      const industry = filteredIndustries.value[params.dataIndex]
      return `${industry.name}<br/>就业比例: ${industry.value}%<br/>增长率: ${industry.growth}%<br/>就业人数: ${calculateEmploymentCount(industry.value).toLocaleString()}`
    }
  },
  xAxis: {
    type: 'value',
    name: '就业比例 (%)',
    nameLocation: 'middle',
    nameGap: 30
  },
  yAxis: {
    type: 'value',
    name: '增长率 (%)',
    nameLocation: 'middle',
    nameGap: 50
  },
  series: [{
    symbolSize: (data: any) => Math.max(data[0] * 3, 10),
    data: filteredIndustries.value.map(item => [item.value, item.growth]),
    type: 'scatter',
    itemStyle: {
      color: (params: any) => {
        const growth = params.data[1]
        return growth > 0 ? '#67C23A' : '#F56C6C'
      },
      opacity: 0.7
    }
  }]
}))

// 行业趋势图配置
const industryTrendOption = computed(() => {
  // 模拟历史趋势数据
  const years = [2019, 2020, 2021, 2022, 2023, 2024]
  const topIndustries = filteredIndustries.value.slice(0, 5)
  
  return {
    title: {
      text: '主要行业发展趋势',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: topIndustries.map(item => item.name),
      top: 'bottom'
    },
    xAxis: {
      type: 'category',
      data: years
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value}%'
      }
    },
    series: topIndustries.map((industry, index) => ({
      name: industry.name,
      type: 'line',
      smooth: true,
      data: years.map(() => 
        industry.value + (Math.random() - 0.5) * 5
      )
    }))
  }
})

// 新兴行业对比图配置
const emergingIndustryOption = computed(() => {
  const emergingIndustries = filteredIndustries.value
    .filter(item => item.growth > 10)
    .slice(0, 6)
  
  return {
    title: {
      text: '新兴行业增长对比',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        const data = params[0]
        return `${data.name}<br/>增长率: ${data.value}%`
      }
    },
    radar: {
      indicator: emergingIndustries.map(industry => ({
        name: industry.name,
        max: Math.max(...emergingIndustries.map(i => i.growth)) + 5
      })),
      center: ['50%', '55%'],
      radius: '70%'
    },
    series: [{
      name: '增长率',
      type: 'radar',
      data: [{
        value: emergingIndustries.map(industry => industry.growth),
        name: '增长率',
        areaStyle: {
          color: 'rgba(64, 158, 255, 0.3)'
        }
      }]
    }]
  }
})

// 计算就业人数
const calculateEmploymentCount = (percentage: number) => {
  const totalEmployed = employmentStore.latestEmploymentRate?.employedCount || 10000000
  return Math.round(totalEmployed * percentage / 100)
}

// 获取前景评级
const getProspectRating = (growth: number) => {
  if (growth > 10) return 5
  if (growth > 5) return 4
  if (growth > 0) return 3
  if (growth > -5) return 2
  return 1
}

// 查看行业详情
const viewIndustryDetail = (industry: any) => {
  selectedIndustryDetail.value = industry
  showDetailDialog.value = true
}

// 获取行业描述
const getIndustryDescription = (industryName: string) => {
  const descriptions: Record<string, string> = {
    'IT/互联网': '信息技术和互联网行业是当前最具活力的行业之一，涵盖软件开发、人工智能、大数据等领域。',
    '金融': '金融行业包括银行、证券、保险等传统金融机构，以及新兴的金融科技公司。',
    '教育': '教育行业涵盖基础教育、高等教育、职业教育和在线教育等多个细分领域。',
    '制造业': '制造业是实体经济的重要组成部分，正在向智能制造和高端制造转型升级。',
    '医疗健康': '医疗健康行业包括医疗服务、医药研发、医疗器械等，是关系民生的重要行业。'
  }
  return descriptions[industryName] || '该行业正在快速发展，为毕业生提供了丰富的就业机会。'
}

// 获取行业技能要求
const getIndustrySkills = (industryName: string) => {
  const skills: Record<string, string[]> = {
    'IT/互联网': ['编程语言', '数据分析', '云计算', '人工智能', '项目管理'],
    '金融': ['金融知识', '数据分析', '风险管理', '客户服务', '合规意识'],
    '教育': ['教学能力', '沟通技巧', '心理学', '课程设计', '教育技术'],
    '制造业': ['工程技术', '质量管理', '生产管理', '安全意识', '创新思维'],
    '医疗健康': ['医学知识', '临床技能', '沟通能力', '责任心', '持续学习']
  }
  return skills[industryName] || ['专业知识', '沟通能力', '团队合作', '学习能力', '责任心']
}

// 更新图表
const updateCharts = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 500)
}

// 生成报告
const generateReport = () => {
  ElMessage.success('行业分析报告生成中...')
}

onMounted(() => {
  // 页面加载时的初始化逻辑
})
</script>

<style scoped>
.industry-analysis {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.mr-10 {
  margin-right: 10px;
}

.mb-10 {
  margin-bottom: 10px;
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

.stat-icon.industry-count {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.growth-rate {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.top-industry {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.fastest-growth {
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

:deep(.el-select) {
  width: 100%;
}

:deep(.el-rate) {
  display: flex;
  align-items: center;
}
</style>