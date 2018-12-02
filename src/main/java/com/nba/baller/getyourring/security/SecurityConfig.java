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
//		auth
//				.inMemoryAuthentication()
//				.withUser("ziemo")
//				.password(passwordEncoder().encode("1212"))
//				.roles("ADMIN")
//				.and()
//				.withUser("ania")
//				.password(passwordEncoder().encode("1212"))
//				.roles("USER");

		auth
				.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery("select username, password, enabled from owner where username=?")
				.passwordEncoder(passwordEncoder())
				.authoritiesByUsernameQuery("select owner_username, role from roles where owner_username=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/home")
					.permitAll()
				.antMatchers("/admin")
					.hasRole("ADMIN")
				.antMatchers("/game/**")
					.hasRole("USER")
//				.antMatchers("/home")
//					.anonymous()  --> don't let to open home page when being logged
				.anyRequest()
					.permitAll()
				.and()
				.formLogin()
					.loginPage("/login")
				.and()
				.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/home")
					.invalidateHttpSession(true)    //!!!
					.deleteCookies("JSESSIONID")
				.and()
				.rememberMe()
					.tokenValiditySeconds(1);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
