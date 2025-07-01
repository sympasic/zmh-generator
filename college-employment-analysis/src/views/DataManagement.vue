<template>
  <div class="data-management">
    <el-row :gutter="20" class="mb-20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>数据管理</span>
              <el-button type="primary" @click="uploadData">
                <el-icon><Upload /></el-icon>
                上传数据
              </el-button>
            </div>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-icon data-total">
                    <el-icon><DataBoard /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">1,234,567</div>
                    <div class="stat-label">总数据量</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-icon data-updated">
                    <el-icon><Clock /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">今日</div>
                    <div class="stat-label">最后更新</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-icon data-sources">
                    <el-icon><Connection /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">12</div>
                    <div class="stat-label">数据源</div>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-icon data-quality">
                    <el-icon><CircleCheck /></el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">98.5%</div>
                    <div class="stat-label">数据质量</div>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>数据源管理</span>
          </template>
          
          <el-table :data="dataSources" stripe>
            <el-table-column prop="name" label="数据源名称" />
            <el-table-column prop="type" label="类型">
              <template #default="{ row }">
                <el-tag :type="getSourceTypeColor(row.type)">
                  {{ row.type }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="row.status === '正常' ? 'success' : 'danger'">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="lastUpdate" label="最后更新" />
            <el-table-column prop="recordCount" label="记录数" />
            <el-table-column label="操作">
              <template #default="{ row }">
                <el-button size="small" @click="syncData(row)">同步</el-button>
                <el-button size="small" type="warning" @click="editSource(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteSource(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>数据质量监控</span>
          </template>
          
          <div class="quality-metrics">
            <div class="metric-item">
              <span class="metric-label">完整性</span>
              <el-progress :percentage="95" color="#67C23A" />
            </div>
            <div class="metric-item">
              <span class="metric-label">准确性</span>
              <el-progress :percentage="98" color="#409EFF" />
            </div>
            <div class="metric-item">
              <span class="metric-label">一致性</span>
              <el-progress :percentage="92" color="#E6A23C" />
            </div>
            <div class="metric-item">
              <span class="metric-label">及时性</span>
              <el-progress :percentage="99" color="#67C23A" />
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>数据同步日志</span>
          </template>
          
          <el-timeline>
            <el-timeline-item
              v-for="log in syncLogs"
              :key="log.id"
              :timestamp="log.timestamp"
              :type="log.type"
            >
              {{ log.message }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 数据源列表
const dataSources = ref([
  {
    id: 1,
    name: '教育部就业数据',
    type: 'API',
    status: '正常',
    lastUpdate: '2024-01-15 10:30:00',
    recordCount: '856,432'
  },
  {
    id: 2,
    name: '招聘网站数据',
    type: '爬虫',
    status: '正常',
    lastUpdate: '2024-01-15 09:45:00',
    recordCount: '234,567'
  },
  {
    id: 3,
    name: '高校就业统计',
    type: '文件',
    status: '异常',
    lastUpdate: '2024-01-14 16:20:00',
    recordCount: '143,998'
  },
  {
    id: 4,
    name: '薪资调研数据',
    type: 'API',
    status: '正常',
    lastUpdate: '2024-01-15 11:15:00',
    recordCount: '98,765'
  }
])

// 同步日志
const syncLogs = ref([
  {
    id: 1,
    timestamp: '2024-01-15 11:30:00',
    type: 'success',
    message: '教育部就业数据同步成功，新增记录 1,234 条'
  },
  {
    id: 2,
    timestamp: '2024-01-15 10:45:00',
    type: 'warning',
    message: '招聘网站数据同步完成，发现 5 条重复记录已过滤'
  },
  {
    id: 3,
    timestamp: '2024-01-15 09:30:00',
    type: 'danger',
    message: '高校就业统计数据同步失败，连接超时'
  },
  {
    id: 4,
    timestamp: '2024-01-15 08:15:00',
    type: 'success',
    message: '薪资调研数据同步成功'
  }
])

// 获取数据源类型颜色
const getSourceTypeColor = (type: string) => {
  const colorMap: Record<string, string> = {
    'API': 'success',
    '爬虫': 'warning',
    '文件': 'info'
  }
  return colorMap[type] || 'default'
}

// 上传数据
const uploadData = () => {
  ElMessage.info('数据上传功能开发中...')
}

// 同步数据
const syncData = (row: any) => {
  ElMessage.success(`正在同步 ${row.name} 数据...`)
}

// 编辑数据源
const editSource = (row: any) => {
  ElMessage.info(`编辑 ${row.name} 功能开发中...`)
}

// 删除数据源
const deleteSource = (row: any) => {
  ElMessageBox.confirm(`确定要删除数据源 ${row.name} 吗？`, '确认删除', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
  })
}
</script>

<style scoped>
.data-management {
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

.stat-icon.data-total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.data-updated {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.data-sources {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.data-quality {
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

.quality-metrics {
  padding: 20px 0;
}

.metric-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.metric-label {
  width: 80px;
  margin-right: 20px;
  font-weight: 500;
}

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.el-card__header) {
  padding: 18px 20px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-timeline) {
  padding-left: 0;
}
</style>