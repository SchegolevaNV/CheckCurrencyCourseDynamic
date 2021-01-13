package main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"main.services","main.controllers"})
public class Main {

    public static void main(String[] args)
    {
        SpringApplication.run(Main.class, args);
    }
}
