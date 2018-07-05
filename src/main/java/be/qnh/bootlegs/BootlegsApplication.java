package be.qnh.bootlegs;

import be.qnh.bootlegs.domain.AppUser;
import be.qnh.bootlegs.service.UserService;
import be.qnh.bootlegs.service.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BootlegsApplication {

    //creating initial users in database

//    public static void main(String[] args) {
//        ConfigurableApplicationContext applicationContext = SpringApplication.run(BootlegsApplication.class, args);
//        UserService userService = applicationContext.getBean("userServiceImpl", UserServiceImpl.class);
//
//        userService.addOne(new AppUser("Jopie", "wachtwoord", "jopie@gmail.com", "ROLE_ADMIN"));
//
//        userService.addOne(new AppUser("Flippe", "geheim", "flippie@ergens.com", "ROLE_USER"));
//
//        userService.addOne(new AppUser("Frans", "onbekend", "frans@yahoo.com", "ROLE_GUEST"));
//
//        userService.addOne(new AppUser("TestAdminUser", "AdminWachtwoord", "testadmin@test.com", "ROLE_ADMIN"));
//
//        userService.addOne(new AppUser("TestUser", "UserWachtwoord", "testuser@test.com", "ROLE_USER"));
//    }

    public static void main(String[] args) {
        SpringApplication.run(BootlegsApplication.class, args);
    }
}
