package com.example.CulinaryGuide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig  {


///login?logout
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.antMatchers( "/","/dish/**","/dishCategory/**","/ingredient/**","/ingredientCategory/**","/resources/static/Images/**","/registration","/login").permitAll().antMatchers("/resources/**").permitAll()
                        .antMatchers("/*.jpg").permitAll().antMatchers("/*.json").permitAll().antMatchers("/*.JPG").permitAll().antMatchers("/*.css").permitAll().antMatchers("/*.js").permitAll().
                        antMatchers("/*.png").permitAll().antMatchers("/*.map").permitAll().antMatchers("/*.scss").permitAll().antMatchers("/*.less").permitAll().
                        anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()

                )
                .logout();

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("pass")
//                        .roles("USER")
//                        .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    @Autowired
    private DataSource dataSource;
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws
            Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery("select username,password,enabled from userstable where username=?")
                .authoritiesByUsernameQuery("select username,authority from authorities where username=?");
    }


}
