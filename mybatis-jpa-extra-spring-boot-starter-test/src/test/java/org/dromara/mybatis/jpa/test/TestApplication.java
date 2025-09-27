package org.dromara.mybatis.jpa.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/24 9:48
 */

@SpringBootApplication
@MapperScan("org.dromara.mybatis.jpa.test.dao.persistence")
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
