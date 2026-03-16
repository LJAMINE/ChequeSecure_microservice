package com.chequesecure.banquecentrale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BanqueCentraleServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BanqueCentraleServiceApplication.class, args);
    }
}
