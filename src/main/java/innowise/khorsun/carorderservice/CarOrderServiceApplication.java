package innowise.khorsun.carorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CarOrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarOrderServiceApplication.class, args);
    }

}
