package com.yyh.backendjava.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.ITemplate;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/kg_learing?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        String username = "root";
        String password = "123456";
        FastAutoGenerator.create(url,username,password)
                .globalConfig(builder -> {
                    builder.author("yyh")//作者
//                            .enableSwagger()
                            .fileOverride()//重新运行后会重载 关掉以防前面写的都没了
                            .outputDir(System.getProperty("user.dir")+"/src/main/java");//输出路径
                })
                .packageConfig(builder -> {
                    builder.parent("com.yyh")
                            .moduleName("backendjava")
                            .entity("entity")
                            .controller("controller")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("serviceImpl")
                            .xml("mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                                    System.getProperty("user.dir")+"/src/main/resources/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude("do_test");//数据表名字 改这里
//                            .addTablePrefix("t","");//过滤前缀
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
        System.out.println("代码生成完毕！...");
    }
}
