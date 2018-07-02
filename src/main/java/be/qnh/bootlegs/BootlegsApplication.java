package be.qnh.bootlegs;

import be.qnh.bootlegs.domain.AppUser;
import be.qnh.bootlegs.repository.UserRepository;
import be.qnh.bootlegs.service.UserService;
import be.qnh.bootlegs.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BootlegsApplication {

//    public static void main(String[] args) {
//        ConfigurableApplicationContext applicationContext = SpringApplication.run(BootlegsApplication.class, args);
//        UserService userService = applicationContext.getBean("userServiceImpl", UserServiceImpl.class);
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        UserRepository userRepository = applicationContext.getBean("userRepository", UserRepository.class);
//
//        userService.addOne(new AppUser("Jopie", encoder.encode("wachtwoord"), "jopie@gmail.com", "ROLE_ADMIN"));
//
//        userService.addOne(new AppUser("Flippe", encoder.encode("geheim"), "flippie@ergens.com", "ROLE_USER"));
//
//        userService.addOne(new AppUser("Frans", encoder.encode("onbekend"), "frans@yahoo.com", "ROLE_GUEST"));
//
//    }

    public static void main(String[] args) {
        SpringApplication.run(BootlegsApplication.class, args);
    }
}
