package com.zmh.model;

import lombok.Data;

/**
 * 静态模板配置
 */
@Data
public class MainTemplateConfig {

    //1.在代码开头增加作者 @Author 注释(增加代码)
    //2.修改程序输出的信息提示(替换代码)
    //3.将循环读取输入改为单次读取(可选代码)

        /**
         * 作者注释
         */
        private String author;

        /**
         * 输出信息
         */
        private String outputText;
        /**
         * 是否生成循环
         */
        private boolean loop;
}
