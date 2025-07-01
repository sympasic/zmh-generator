# 大学生就业大数据分析平台

一个基于Vue 3 + TypeScript + ECharts + Element Plus构建的现代化大学生就业数据分析平台。

## 🎯 项目简介

本平台致力于为教育机构、政府部门、研究人员和学生提供全面的大学生就业数据分析服务。通过直观的数据可视化和深度分析，帮助用户了解就业市场趋势，制定更好的教育和就业策略。

## ✨ 主要功能

### 📊 数据概览
- 实时就业率统计
- 毕业生总数统计
- 平均薪资水平
- 就业趋势图表
- 行业分布分析
- 技能需求雷达图

### 💰 薪资分析
- 专业薪资排名
- 薪资区间分布
- 地区薪资对比
- 薪资增长趋势
- 多维度筛选功能
- 数据导出功能

### 🏭 行业分析
- 行业就业分布
- 行业增长率对比
- 新兴行业趋势
- 行业详细信息
- 气泡图关系分析
- 图表/表格双视图

### 🌍 地区分析
- 地区就业率排名
- 平均薪资地区对比
- 岗位数量分布
- 地区综合评分
- 地区对比功能
- 城市等级分类

### 📈 报告中心
- 综合就业报告生成
- 趋势分析报告
- 多格式导出(PDF/Excel/Word)
- 历史报告管理
- 报告预览功能

### 🗄️ 数据管理
- 多数据源管理
- 数据质量监控
- 数据同步功能
- 同步日志记录
- 数据上传功能

## 🛠️ 技术栈

- **前端框架**: Vue 3 + TypeScript
- **构建工具**: Vite
- **UI组件库**: Element Plus
- **图表库**: ECharts + vue-echarts
- **状态管理**: Pinia
- **路由管理**: Vue Router
- **HTTP客户端**: Axios
- **日期处理**: Day.js
- **代码规范**: ESLint

## 🚀 快速开始

### 环境要求
- Node.js >= 16.0.0
- npm >= 8.0.0

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm run dev
```

### 构建生产版本
```bash
npm run build
```

### 预览生产构建
```bash
npm run preview
```

### 代码检查
```bash
npm run lint
```

## 📁 项目结构

```
college-employment-analysis/
├── public/                 # 静态资源
├── src/
│   ├── assets/            # 资源文件
│   ├── components/        # 公共组件
│   ├── router/           # 路由配置
│   ├── stores/           # Pinia状态管理
│   │   └── employment.ts # 就业数据store
│   ├── views/            # 页面组件
│   │   ├── Dashboard.vue         # 数据概览
│   │   ├── SalaryAnalysis.vue    # 薪资分析
│   │   ├── IndustryAnalysis.vue  # 行业分析
│   │   ├── RegionAnalysis.vue    # 地区分析
│   │   ├── DataManagement.vue    # 数据管理
│   │   ├── Help.vue              # 帮助中心
│   │   ├── NotFound.vue          # 404页面
│   │   └── reports/              # 报告模块
│   │       ├── EmploymentReport.vue # 就业报告
│   │       └── TrendReport.vue       # 趋势报告
│   ├── App.vue           # 根组件
│   └── main.ts          # 入口文件
├── package.json
└── README.md
```

## 🎨 界面预览

### 主要特色
- 🎯 现代化设计风格
- 📱 响应式布局
- 🎨 渐变色彩搭配
- 📊 丰富的图表类型
- 🔍 强大的筛选功能
- 📤 便捷的导出功能

### 图表类型
- 📈 折线图 - 趋势分析
- 📊 柱状图 - 数据对比
- 🥧 饼图 - 比例分析
- 🎯 雷达图 - 多维评估
- 💫 散点图 - 关系分析
- 📏 进度条 - 完成度展示

## 📊 数据模拟

项目包含丰富的模拟数据：
- 6年就业率历史数据
- 10个主要专业薪资数据
- 9个行业分布数据
- 10个城市地区数据
- 12项技能需求数据

## 🔧 自定义配置

### 修改数据源
在 `src/stores/employment.ts` 中修改数据：
```typescript
// 修改就业率数据
const employmentRates = ref<EmploymentRate[]>([...])

// 修改行业数据
const industryData = ref<IndustryData[]>([...])

// 修改薪资数据
const salaryData = ref<SalaryData[]>([...])
```

### 自定义主题
在 `src/App.vue` 中修改主题色：
```css
.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系我们

- 项目地址: [GitHub Repository](https://github.com/your-username/college-employment-analysis)
- 问题反馈: [Issues](https://github.com/your-username/college-employment-analysis/issues)
- 邮箱: support@employment-analysis.com

## 🙏 致谢

感谢以下开源项目：
- [Vue.js](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [ECharts](https://echarts.apache.org/)
- [Vite](https://vitejs.dev/)

---

⭐ 如果这个项目对你有帮助，请给它一个星标！
