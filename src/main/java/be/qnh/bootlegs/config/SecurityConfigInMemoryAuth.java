//package be.qnh.bootlegs.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableGlobalMethodSecurity
//@EnableWebSecurity
//public class SecurityConfigInMemoryAuth extends WebSecurityConfigurerAdapter {
//
//    private PasswordEncoder passwordEncoder;
//
//    private Map encoders = new HashMap<>();
//
//    private void configEncoders() {
//        encoders.put("bcrypt", new BCryptPasswordEncoder());
//        encoders.put("noop", NoOpPasswordEncoder.getInstance());
//        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
//        encoders.put("sha64", new StandardPasswordEncoder());
//    }
//
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                //.authorizeRequests().antMatchers("/api/tour/find**", "/api/concert/find**").permitAll()
//                .authorizeRequests().antMatchers(HttpMethod.GET).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll()
//                .and()
//                .logout().permitAll()
//                .and()
//                .httpBasic();
//        //Cross-Site Request Forgery (CSRF) is a type of attack that occurs when a malicious web site, email,
//        // blog, instant message, or program causes a user’s web browser to perform an unwanted action
//        // on a trusted site for which the user is currently authenticated
//        httpSecurity.csrf().disable();
//        httpSecurity.headers().frameOptions().disable();//Disables X-Frame-Options in Spring Security for access to H2 database console
//        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }
//
//    //vanaf spring-security-5.0.0 wordt er geen default PasswordEncoder voorzien maar moeten we dat zelf doen met DelegatingPasswordEncoder
//    //zie : https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released en
//    //https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/password/DelegatingPasswordEncoder.html
//    @Autowired
//    public void configureAuthenticationManager(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        configEncoders();
//        passwordEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);
//        String passwordUser = passwordEncoder.encode("user");
//        String passwordAdmin = passwordEncoder.encode("admin");
//        System.out.println("passwordUser = " + passwordUser);
//        System.out.println("passwordAdmin = " + passwordAdmin);
//        authenticationManagerBuilder
//                .inMemoryAuthentication()
//                .passwordEncoder(passwordEncoder)
//                .withUser("admin").password(passwordAdmin).roles("ADMIN")
//                .and()
//                .withUser("user").password(passwordUser).roles("USER");
//    }
//}
