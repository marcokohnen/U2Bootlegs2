package be.qnh.bootlegs.config;

import be.qnh.bootlegs.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class SecurityConfigDAO extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userService;

    @Autowired
    public SecurityConfigDAO(UserServiceImpl userService) {
        this.userService = userService;
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
                .httpBasic();

        //Cross-Site Request Forgery (CSRF) is a type of attack that occurs when a malicious web site, email,
        // blog, instant message, or program causes a user’s web browser to perform an unwanted action
        // on a trusted site for which the user is currently authenticated
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }
}
