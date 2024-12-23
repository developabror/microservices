package uz.app.sellerapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import uz.app.sellerapp.repository.UserRepository;

@Configuration
public class MyConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeHttpRequests(authent->{
                    authent
                            .requestMatchers("/seller/**","/seller/swagger/**","/seller/swagger-ui/**","/v3/**")
                            .permitAll()
                            .requestMatchers("/seller/")
                            .anyRequest()
                            .authenticated();
                })
                .build();
//        http
//                .authorizeRequests()
//                .antMatchers("/api/seller/**").hasRole("SELLER")
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic()
//                .and()
//                .csrf().disable();
//        return http.build();
    }
    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository){
        UserDetailsService service =(username)->{
            return userRepository.findByUsername(username).orElseThrow();
        };
        return service;
    }
}
