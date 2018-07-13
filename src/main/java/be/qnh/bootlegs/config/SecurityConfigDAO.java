package be.qnh.bootlegs.config;

import be.qnh.bootlegs.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

// see : https://docs.spring.io/spring-security/site/docs/5.0.0.BUILD-SNAPSHOT/reference/htmlsingle/#jc

@Configuration
@EnableWebSecurity
// Modifying or overriding the default spring boot security.
public class SecurityConfigDAO extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfigDAO(UserServiceImpl userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests().antMatchers(HttpMethod.GET).access("hasRole('USER') or hasRole('ADMIN')")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST).hasRole("ADMIN")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT).hasRole("ADMIN")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                .and()
                .authorizeRequests().antMatchers("/login").permitAll()
                .and()
                .logout()
                .and()
                .httpBasic();
        //Cross-Site Request Forgery (CSRF) is a type of attack that occurs when a malicious web site, email,
        // blog, instant message, or program causes a user’s web browser to perform an unwanted action
        // on a trusted site for which the user is currently authenticated
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Autowired
    public void configureAuthenticationManager(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }
}
