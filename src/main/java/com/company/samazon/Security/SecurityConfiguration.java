package com.company.samazon.Security;

import com.company.samazon.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SSUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception{
        return new SSUserDetailsService(userRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers("/", "/employerregistration", "/applicantregistration", "/css/**", "/img/**", "/fonts/**", "/js/**", "/sass/**", "/temp","/assets/**").permitAll()
                .antMatchers("/resume", "/coverletter", "/main").access("hasAnyAuthority('APPLICANT', 'EMPLOYER', 'ADMIN')")
                .antMatchers("/mod", "/references").access("hasAnyAuthority('APPLICANT', 'ADMIN')")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").successForwardUrl("/main").permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll().permitAll()
                .and()
                .httpBasic();


        http
                .csrf().disable();
        http
                .headers().frameOptions().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication().
                withUser("customer1").password("cpassword").authorities("CUSTOMER");

        auth.inMemoryAuthentication().
                withUser("admin1").password("apassword").authorities("ADMIN");

        auth.userDetailsService(userDetailsServiceBean());

    }


}