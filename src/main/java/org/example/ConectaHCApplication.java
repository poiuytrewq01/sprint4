package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class ConectaHCApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConectaHCApplication.class, args);
        System.out.println("================================");
        System.out.println("ConectaHC - Sistema de Clínica");
        System.out.println("API RESTful iniciada com sucesso!");
        System.out.println("================================");
    }
}
