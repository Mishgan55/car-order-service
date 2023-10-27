package innowise.khorsun.carorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.ResourceBundle;

@SpringBootApplication
@EnableEurekaClient
public class CarOrderServiceApplication {
    @Bean
    ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("messages");
    }

    public static void main(String[] args) {
        SpringApplication.run(CarOrderServiceApplication.class, args);
    }

}
