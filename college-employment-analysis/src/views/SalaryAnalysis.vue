<template>
  <div class="salary-analysis">
    <el-row :gutter="20" class="mb-20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>薪资分析筛选</span>
            </div>
          </template>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-select v-model="selectedMajor" placeholder="选择专业" clearable @change="updateCharts">
                <el-option label="全部专业" value="" />
                <el-option 
                  v-for="major in majors" 
                  :key="major" 
                  :label="major" 
                  :value="major" 
                />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="selectedRegion" placeholder="选择地区" clearable @change="updateCharts">
                <el-option label="全部地区" value="" />
                <el-option 
                  v-for="region in regions" 
                  :key="region" 
                  :label="region" 
                  :value="region" 
                />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="salaryRange" placeholder="薪资范围" @change="updateCharts">
                <el-option label="全部范围" value="" />
                <el-option label="5K以下" value="0-5000" />
                <el-option label="5K-10K" value="5000-10000" />
                <el-option label="10K-15K" value="10000-15000" />
                <el-option label="15K-20K" value="15000-20000" />
                <el-option label="20K以上" value="20000-999999" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="exportData">
                <el-icon><Download /></el-icon>
                导出数据
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon salary-avg">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ filteredAverageSalary.toLocaleString() }}</div>
              <div class="stat-label">平均薪资</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon salary-median">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ medianSalary.toLocaleString() }}</div>
              <div class="stat-label">中位数薪资</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon sample-count">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ totalSamples.toLocaleString() }}</div>
              <div class="stat-label">样本数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>专业薪资分布</span>
          </template>
          <v-chart 
            class="chart" 
            :option="majorSalaryOption"
            :loading="loading"
            autoresize
          />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>薪资区间分布</span>
          </template>
          <v-chart 
            class="chart" 
            :option="salaryDistributionOption"
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
            <span>薪资与就业数量散点图</span>
          </template>
          <v-chart 
            class="chart-large" 
            :option="salaryScatterOption"
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
            <span>地区平均薪资</span>
          </template>
          <v-chart 
            class="chart" 
            :option="regionSalaryOption"
            :loading="loading"
            autoresize
          />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>薪资增长趋势</span>
          </template>
          <v-chart 
            class="chart" 
            :option="salaryTrendOption"
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
            <div class="card-header">
              <span>详细数据表格</span>
              <el-input
                v-model="searchText"
                placeholder="搜索专业..."
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
            <el-table-column prop="major" label="专业" sortable />
            <el-table-column prop="avgSalary" label="平均薪资" sortable>
              <template #default="{ row }">
                ¥{{ row.avgSalary.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="minSalary" label="最低薪资" sortable>
              <template #default="{ row }">
                ¥{{ row.minSalary.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="maxSalary" label="最高薪资" sortable>
              <template #default="{ row }">
                ¥{{ row.maxSalary.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="count" label="样本数量" sortable>
              <template #default="{ row }">
                {{ row.count.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column label="薪资范围">
              <template #default="{ row }">
                <el-tag>{{ row.maxSalary - row.minSalary }}K</el-tag>
              </template>
            </el-table-column>
          </el-table>
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
const selectedMajor = ref('')
const selectedRegion = ref('')
const salaryRange = ref('')
const searchText = ref('')

// 计算属性
const majors = computed(() => 
  employmentStore.salaryData.map(item => item.major)
)

const regions = computed(() => 
  employmentStore.regionData.map(item => item.region)
)

const filteredData = computed(() => {
  let data = [...employmentStore.salaryData]
  
  if (selectedMajor.value) {
    data = data.filter(item => item.major === selectedMajor.value)
  }
  
  if (salaryRange.value) {
    const [min, max] = salaryRange.value.split('-').map(Number)
    data = data.filter(item => item.avgSalary >= min && item.avgSalary <= max)
  }
  
  return data
})

const filteredAverageSalary = computed(() => {
  if (filteredData.value.length === 0) return 0
  const total = filteredData.value.reduce((sum, item) => sum + item.avgSalary * item.count, 0)
  const totalCount = filteredData.value.reduce((sum, item) => sum + item.count, 0)
  return Math.round(total / totalCount)
})

const medianSalary = computed(() => {
  if (filteredData.value.length === 0) return 0
  const sorted = [...filteredData.value].sort((a, b) => a.avgSalary - b.avgSalary)
  const mid = Math.floor(sorted.length / 2)
  return sorted.length % 2 === 0 
    ? Math.round((sorted[mid - 1].avgSalary + sorted[mid].avgSalary) / 2)
    : sorted[mid].avgSalary
})

const totalSamples = computed(() => 
  filteredData.value.reduce((sum, item) => sum + item.count, 0)
)

const filteredTableData = computed(() => {
  let data = filteredData.value
  if (searchText.value) {
    data = data.filter(item => 
      item.major.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }
  return data
})

// 专业薪资分布图配置
const majorSalaryOption = computed(() => ({
  title: {
    text: '专业平均薪资排名',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    formatter: (params: any) => {
      const data = params[0]
      return `${data.name}<br/>平均薪资: ¥${data.value.toLocaleString()}`
    }
  },
  xAxis: {
    type: 'category',
    data: filteredData.value
      .sort((a, b) => b.avgSalary - a.avgSalary)
      .slice(0, 8)
      .map(item => item.major),
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
    data: filteredData.value
      .sort((a, b) => b.avgSalary - a.avgSalary)
      .slice(0, 8)
      .map(item => item.avgSalary),
    type: 'bar',
    itemStyle: {
      color: {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 0,
        y2: 1,
        colorStops: [{
          offset: 0, color: '#409EFF'
        }, {
          offset: 1, color: '#79BBFF'
        }]
      }
    }
  }]
}))

// 薪资区间分布图配置
const salaryDistributionOption = computed(() => {
  const ranges = [
    { name: '5K以下', min: 0, max: 5000 },
    { name: '5K-8K', min: 5000, max: 8000 },
    { name: '8K-12K', min: 8000, max: 12000 },
    { name: '12K-18K', min: 12000, max: 18000 },
    { name: '18K-25K', min: 18000, max: 25000 },
    { name: '25K以上', min: 25000, max: 999999 }
  ]
  
  const distribution = ranges.map(range => {
    const count = filteredData.value
      .filter(item => item.avgSalary >= range.min && item.avgSalary < range.max)
      .reduce((sum, item) => sum + item.count, 0)
    return { name: range.name, value: count }
  })

  return {
    title: {
      text: '薪资区间人数分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c}人 ({d}%)'
    },
    series: [{
      name: '薪资分布',
      type: 'pie',
      radius: '70%',
      data: distribution,
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  }
})

// 薪资散点图配置
const salaryScatterOption = computed(() => ({
  title: {
    text: '专业薪资与就业数量关系',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: (params: any) => {
      const data = filteredData.value[params.dataIndex]
      return `${data.major}<br/>平均薪资: ¥${data.avgSalary.toLocaleString()}<br/>就业人数: ${data.count.toLocaleString()}`
    }
  },
  xAxis: {
    type: 'value',
    name: '平均薪资 (元)',
    nameLocation: 'middle',
    nameGap: 30,
    axisLabel: {
      formatter: '¥{value}'
    }
  },
  yAxis: {
    type: 'value',
    name: '就业人数',
    nameLocation: 'middle',
    nameGap: 50,
    axisLabel: {
      formatter: '{value}'
    }
  },
  series: [{
    symbolSize: (data: any) => Math.sqrt(data[1]) / 50,
    data: filteredData.value.map(item => [item.avgSalary, item.count]),
    type: 'scatter',
    itemStyle: {
      color: '#409EFF',
      opacity: 0.7
    }
  }]
}))

// 地区薪资图配置
const regionSalaryOption = computed(() => ({
  title: {
    text: '各地区平均薪资',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    formatter: (params: any) => {
      const data = params[0]
      return `${data.name}<br/>平均薪资: ¥${data.value.toLocaleString()}`
    }
  },
  xAxis: {
    type: 'category',
    data: employmentStore.regionData
      .sort((a, b) => b.avgSalary - a.avgSalary)
      .slice(0, 8)
      .map(item => item.region),
    axisLabel: {
      rotate: 45
    }
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: '¥{value}'
    }
  },
  series: [{
    data: employmentStore.regionData
      .sort((a, b) => b.avgSalary - a.avgSalary)
      .slice(0, 8)
      .map(item => item.avgSalary),
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
          offset: 1, color: '#95D475'
        }]
      }
    }
  }]
}))

// 薪资增长趋势图配置
const salaryTrendOption = computed(() => {
  // 模拟薪资增长数据
  const trendData = [
    { year: 2019, salary: 8500 },
    { year: 2020, salary: 8800 },
    { year: 2021, salary: 9200 },
    { year: 2022, salary: 9800 },
    { year: 2023, salary: 10500 },
    { year: 2024, salary: 11200 }
  ]

  return {
    title: {
      text: '平均薪资增长趋势',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        const data = params[0]
        return `${data.name}年<br/>平均薪资: ¥${data.value.toLocaleString()}`
      }
    },
    xAxis: {
      type: 'category',
      data: trendData.map(item => item.year)
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '¥{value}'
      }
    },
    series: [{
      data: trendData.map(item => item.salary),
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      lineStyle: {
        width: 3,
        color: '#E6A23C'
      },
      itemStyle: {
        color: '#E6A23C'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0, color: 'rgba(230, 162, 60, 0.3)'
          }, {
            offset: 1, color: 'rgba(230, 162, 60, 0.1)'
          }]
        }
      }
    }]
  }
})

// 更新图表
const updateCharts = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 500)
}

// 导出数据
const exportData = () => {
  ElMessage.success('数据导出功能开发中...')
}

onMounted(() => {
  // 页面加载时的初始化逻辑
})
</script>

<style scoped>
.salary-analysis {
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

.stat-icon.salary-avg {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.salary-median {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.sample-count {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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
</style>