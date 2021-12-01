package com.webapplication.crossport.config;

import com.webapplication.crossport.config.filter.JWTFilter;
import com.webapplication.crossport.controllers.ArticleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * This class aims to configure Spring security config
 *
 * @author Herzig Melvyn
 */
@Configuration
@EnableWebSecurity
@ComponentScan("com.webapplication.crossport.config")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public CustomAuthenticationProvider authProvider() {
        CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        return authProvider;
    }

    /**
     * Defines our own web configuration
     *
     * @param http The httpSecurity to modify
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(
                        // WebController
                        "/",
                        "/home",
                        "/index.html",

                        // ArticleController
                        "/shop/**",
                        "/articles/**",

                        // CartController
                        "/cart/**",

                        //Images des produits
                        "/" + ArticleController.uploadDir + "/**",

                        // Ressources Statiques
                        "/css/**",
                        "/images/**",
                        "/js/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(
                        // CategoryController
                        "/categorie/**",

                        //ArticleController
                        "/manageArticles",
                        "/makeUnavailable**",
                        "/editArticle")
                .hasRole("ADMIN")
                .antMatchers(
                        // LoginController
                        "/login",
                        // RegisterController
                        "/register").anonymous()
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(authenticationSuccessHandler)
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler(logoutSuccessHandler)
                .logoutSuccessUrl("/home");

        http.addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Used by the default implementation of authenticationManager() to
     * attempt to obtain an AuthenticationManager.
     *
     * @param auth AuthenticationManagerBuilder to use
     * @throws Exception If something goes wrong
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider()).eraseCredentials(false);
    }
}