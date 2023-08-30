package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/index.html").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                .antMatchers("/web/styles.css").permitAll()
                .antMatchers("/web/images/**").permitAll()
                .antMatchers("/index.js").permitAll()
                .antMatchers("/web/CascadiaCodePL.ttf").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients","/api/transactions").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers("/rest/**", "/admin/**", "/h2-console/**", "/api/clients","/api/clients/current/accounts").hasAnyAuthority("ADMIN")
                .antMatchers("/web/accounts.html","/web/cards.html","/web/account.html","/web/accounts.js","/web/account.js","/web/cards.js","/web/error.html").hasAnyAuthority("USER")
                .antMatchers("/api/clients/current","/api/clients/current/cards","/api/clients/current/accounts","/api/clients/current/account.html","/api/clients/current/accounts/{id}").hasAnyAuthority("USER")
                .anyRequest().denyAll();



        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");


        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        http.logout()
                .logoutUrl("/api/logout").deleteCookies("JSESSIONID");
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        http.logout().logoutSuccessHandler((req, res, auth) -> {
            res.sendRedirect("/index.html");
        });

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }


}

