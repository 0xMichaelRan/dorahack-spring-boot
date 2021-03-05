package com.magiplatform.dorahack;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@Slf4j
@ServletComponentScan
@SpringBootApplication
@ComponentScan(basePackages = {"com.magiplatform.dorahack.**"})
@MapperScan("com.magiplatform.dorahack.mapper")
@EnableConfigurationProperties
public class DorahackApplication implements CommandLineRunner {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(DorahackApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("\n----------------------------------------------------------\n\t" +
                            "Application '{}' is running! Access URLs:\n\t" +
                            "Local: \t\thttp://127.0.0.1:{}\n\t" +
                            "External: \thttp://{}:{}{}/doc.html\n\t" +
                            "Profile(s): \t{}\n----------------------------------------------------------",
                    environment.getProperty("spring.application.name"),
                    environment.getProperty("server.port"),
                    InetAddress.getLocalHost().getHostAddress(),
                    environment.getProperty("server.port"),
                    environment.getProperty("server.servlet.context-path"),
                    Arrays.toString(environment.getActiveProfiles()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
