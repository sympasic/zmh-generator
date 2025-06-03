package com.zmh.maker.template.model;

import lombok.Builder;
import lombok.Data;

/**
 * 文件过滤配置
 */
@Data
@Builder
public class FileFilterConfig {
    /**
     * 过滤范围
     */
    private String range;

    /**
     * 过滤规则
     */
    private String rule;

    /**
     * 过滤值
     */
    private String value;

    //{
    //  "files": [
    //    {
    //      "path": "文件（目录）路径",
    //      "filters": [
    //        {
    //          "range": "fileName",
    //          "rule": "regex",
    //          "value": ".*lala.*"
    //        },
    //        {
    //          "range": "fileContent",
    //          "rule": "contains",
    //          "value": "haha"
    //        }
    //      ]
    //    }
    //  ],
    //}

}
