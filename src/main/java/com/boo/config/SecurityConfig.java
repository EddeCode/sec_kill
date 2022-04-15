package com.boo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author song
 * @Date 2022/4/10 17:14
 * @Description
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedPage("/403");
        http.logout().logoutUrl("/user/logout").logoutSuccessUrl("/login.html");
        http.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/index")
                .and()
                .authorizeRequests()

                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
//                //只有XX权限可以进入的
//                .antMatchers("/common")
//                .hasAnyAuthority("common","admin")
//                .antMatchers("/admin")
//                .hasAuthority("admin")
                //无需权限认证的
                .antMatchers("/user/login", "/login.html")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                //
                .csrf()
                .disable();
    }
}
