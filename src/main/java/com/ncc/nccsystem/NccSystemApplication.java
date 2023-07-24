package com.ncc.nccsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ncc.nccsystem.mapper")
public class NccSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(NccSystemApplication.class, args);
    }

}
