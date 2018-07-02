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

    @Override // from UserService
    public AppUser addOne(AppUser appUser) {
        AppUser newAppUser = userRepository.save(appUser);
        LOGGER.info("New AppUser [{}]", newAppUser);
        return newAppUser;
    }

    @Override // from UserService
    public AppUser findAppUserByName(String name) {
        AppUser foundUser = userRepository.findAppUserByName(name);
        LOGGER.info("findAppUserByName(String name) parameter name = " + name);
        return foundUser;
    }

    @Override // from UserService
    public AppUser findAppUserByNameAndEmail(String name, String email) {
        LOGGER.info("findAppUserByNameAndEmail parameter name + email = " + name + "-" + email);
        AppUser foundUser = userRepository.findAppUserByNameAndEmail(name, email);
        LOGGER.info("found appUser : {}", foundUser);
        return foundUser;
    }

    @Override // from UserService
    public AppUser findAppUserByEmail(String email) {
        LOGGER.info("findAppUserByEmail parameter email = " + email);
        AppUser foundUser = userRepository.findAppUserByEmail(email);
        LOGGER.info("found appUser : {}", foundUser);
        return foundUser;
    }

    @Override // from UserDetailsService
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        AppUser founduser = findAppUserByName(name);
        if (founduser == null) {
            throw new UsernameNotFoundException("Invalid Username");
        }
        UserDetails userDetails = new User(founduser.getName(), founduser.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(founduser.getRole())));
        LOGGER.info("loadUserByUsername new userDetails = " + userDetails);
        return userDetails;

    }
}
