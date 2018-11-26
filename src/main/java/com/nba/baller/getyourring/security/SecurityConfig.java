package com.nba.baller.getyourring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.inMemoryAuthentication()
				.withUser("ziemo")
				.password(passwordEncoder().encode("1212"))
				.roles("ADMIN")
				.and()
				.withUser("ania")
				.password(passwordEncoder().encode("1212"))
				.roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/start")
					.permitAll()
				.antMatchers("/admin")
					.hasRole("ADMIN")
				.antMatchers("/game")
					.hasRole("USER")
				.antMatchers("/start")
					.anonymous()
				.anyRequest()
					.permitAll()
				.and()
				.formLogin()
//					.loginPage("/login")
				.and()
				.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/home")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
				.and()
				.rememberMe()
					.tokenValiditySeconds(1);
	}

	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
