package com.gening.library.gemapper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author G
 * @version 1.0
 * @className GeMapperApplication
 * @description SpringBoot启动类
 * @date 2022/3/18 17:45
 */
@MapperScan("com.gening.library.gemapper.web.*.dao")
@SpringBootApplication
public class GeMapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeMapperApplication.class, args);
    }

}
