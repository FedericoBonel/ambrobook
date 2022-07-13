package com.fedebonel.recipemvc.config;

import com.fedebonel.recipemvc.controllers.UserController;
import com.fedebonel.recipemvc.model.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_URL = UserController.USER_URI + "/signin";
    public static final String AUTHENTICATION_FAILURE_URL = UserController.USER_URI + "/signin?error";
    public static final String LOGOUT_URL = UserController.USER_URI + "/signout";
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    // Authentication configuration
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    // Authorization configuration
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/",
                        "/recipe/**/show",
                        "/recipe/**/image/render",
                        "/resources/**",
                        "/static/**",
                        "/webjars/**",
                        "/images/**",
                        "/css/**").permitAll()
                .antMatchers("/user",
                        "/user/signup",
                        "/user/verify").anonymous()
                .antMatchers("/recipe/**/user/like").hasAnyRole(Roles.USER.toString(), Roles.ADMIN.toString())
                .antMatchers("/**").hasRole(Roles.ADMIN.toString())
                .and()
                .formLogin()
                    .loginPage(LOGIN_URL)
                    .loginProcessingUrl(LOGIN_URL)
                    .defaultSuccessUrl("/")
                    .failureUrl(AUTHENTICATION_FAILURE_URL).permitAll()
                .and()
                .logout()
                    .logoutUrl(LOGOUT_URL)
                    .logoutSuccessUrl("/")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true).permitAll()
                .and()
                .rememberMe();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
