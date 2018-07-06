package be.qnh.bootlegs;

import be.qnh.bootlegs.domain.AppUser;
import be.qnh.bootlegs.repository.UserRepository;
import be.qnh.bootlegs.service.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class BootlegsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootlegsApplication.class, args);
    }


//  https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-command-line-runner

//    Using the ApplicationRunner or CommandLineRunner :
//    If you need to run some specific code once the SpringApplication has started, you can implement the ApplicationRunner or CommandLineRunner interfaces.
//    Both interfaces work in the same way and offer a single run method, which is called just before SpringApplication.run(…​) completes.
//
//    The CommandLineRunner interfaces provides access to application arguments as a simple string array, whereas the ApplicationRunner uses the ApplicationArguments
//    interface discussed earlier. The following example shows a CommandLineRunner with a run method:
//
//import org.springframework.boot.*;
//import org.springframework.stereotype.*;
//
//    @Bean
//    ApplicationRunner init(CarRepository repository) {
//        return args -> {
//            Stream.of("Ferrari", "Jaguar", "Porsche", "Lamborghini", "Bugatti",
//                      "AMC Gremlin", "Triumph Stag", "Ford Pinto", "Yugo GV").forEach(name -> {
//                Car car = new Car();
//                car.setName(name);
//                repository.save(car);
//            });
////        };
//    }
//    If several CommandLineRunner or ApplicationRunner beans are defined that must be called in a specific order, you can additionally implement the org.springframework
//    .core.Ordered interface or use the org.springframework.core.annotation.Order annotation.



    // adding initial users to database
    @Bean
    ApplicationRunner initDataBaseWithUsers(UserService userService) {
        return args -> {
            Stream.of(
                    new AppUser("Jopie", "wachtwoord", "jopie@gmail.com", "ROLE_ADMIN"),
                    new AppUser("Flippe", "geheim", "flippie@ergens.com", "ROLE_USER"),
                    new AppUser("Frans", "onbekend", "frans@yahoo.com", "ROLE_GUEST"),
                    new AppUser("TestAdminUser", "AdminWachtwoord", "testadmin@test.com", "ROLE_ADMIN"),
                    new AppUser("TestUser", "UserWachtwoord", "testuser@test.com", "ROLE_USER")

            ).forEach(userService::addOne);
        };
    }
}
