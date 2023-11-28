
package com.json.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.json.sentinel"})
public class StartApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }
    
//    @Bean
//    public PrometheusSentinelRegistry prometheusSenRegistry(CollectorRegistry registry) {
//        return new PrometheusSentinelRegistry(registry);
//    }
}
