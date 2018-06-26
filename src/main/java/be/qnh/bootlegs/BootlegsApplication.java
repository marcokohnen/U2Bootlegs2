package be.qnh.bootlegs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BootlegsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootlegsApplication.class, args);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("BCryptPasswordEncoding van 'testUser' =" + encoder.encode("testUser"));
    }
}
