package com.pnu.dev.shpaltaif.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .ignoringAntMatchers("/telegram-bot-webhook-**")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/admin")
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/accounts/edit", "/accounts/update",
                        "/admin/posts/new", "/admin/posts/edit/**", "/admin/update/**")
                .hasRole("WRITER")
                .and()
                .authorizeRequests()
                .antMatchers("/admin", "/admin/users/update-password", "/admin/posts/**")
                .authenticated()
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole("ADMIN")
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll();
    }

    @Autowired
    @SneakyThrows
    public void configureGlobal(@Qualifier("userServiceImpl") UserDetailsService userDetailsService,
                                AuthenticationManagerBuilder auth,
                                BCryptPasswordEncoder bCryptPasswordEncoder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

}
