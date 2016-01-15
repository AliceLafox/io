package net.lafox.io;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("net.lafox.io.dao")
public class IoApplication {

    public static void main(String[] args) {
        SpringApplication.run(IoApplication.class, args);
    }
}
