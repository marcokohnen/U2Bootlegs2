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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override // from UserService
    public AppUser addOne(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        AppUser newAppUser = userRepository.save(appUser);
        LOGGER.info("new AppUser [{}]", newAppUser);
        return newAppUser;
    }

    @Override // from UserService
    public AppUser findAppUserByEmail(String email) {
        AppUser foundUser = userRepository.findAppUserByEmail(email);
        LOGGER.info("found appUser : {}", foundUser);
        return foundUser;
    }

    @Override // from UserService
    public AppUser deleteOneById(Long id) {
        Optional<AppUser> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            userRepository.deleteById(id);
            LOGGER.info("deleted AppUser = [{}]", foundUser);
            return foundUser.get();
        } else {
            return null;
        }
    }

    @Override // from UserDetailsService
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser founduser = findAppUserByEmail(email);
        if (founduser == null) {
            throw new UsernameNotFoundException("Invalid Username");
        }
        UserDetails userDetails = new User(founduser.getEmail(), founduser.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(founduser.getRole())));
        LOGGER.info("loadUserByUsername new userDetails = " + userDetails);
        return userDetails;

    }
}
