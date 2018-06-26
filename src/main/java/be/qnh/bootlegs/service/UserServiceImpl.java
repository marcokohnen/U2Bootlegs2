package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.AppUser;
import be.qnh.bootlegs.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUser findAppUserByNameAndEmail(String name, String email) {
        AppUser foundUser = userRepository.findAppUserByNameAndEmail(name, email);
        LOGGER.info("found appUser : {}", foundUser);
        return foundUser;
    }

    @Override
    public AppUser findAppUserByEmail(String email) {
        AppUser foundUser = userRepository.findAppUserByEmail(email);
        LOGGER.info("found appUser : {}", foundUser);
        return foundUser;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        AppUser founduser = findAppUserByEmail(userEmail);
        if (founduser == null) {
            throw new UsernameNotFoundException("Invalid email");
        }
        return new User(founduser.getName(), founduser.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(founduser.getRole())));
    }
}
