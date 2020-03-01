package com.dragonsoft;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ronin
 * @version V1.0
 * @since 2019/11/15 9:24
 */
//@MapperScan("com.dragonsoft.yqjk.dao")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
