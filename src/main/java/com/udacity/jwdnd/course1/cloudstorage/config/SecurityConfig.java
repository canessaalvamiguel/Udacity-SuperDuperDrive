package com.udacity.jwdnd.course1.cloudstorage.config;

import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationService authenticationService;

    public SecurityConfig(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationService){
        authenticationService.authenticationProvider(this.authenticationService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.authorizeRequests()
                .antMatchers("/login","/signup", "/css/**", "/js/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated();

        httpSecurity.formLogin()
                .loginPage("/login")
                .permitAll();

        /*Only to allow access to h2 console*/
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
        /*----------------------------------*/

        httpSecurity.formLogin()
                .defaultSuccessUrl("/home", true);

        httpSecurity.logout()
                .logoutSuccessUrl("/login?logout");

    }
}
