package ru.project.plusone.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
	private final AuthenticationService authenticationService;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public SecurityConfig(CustomAuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {

		this.authenticationSuccessHandler = authenticationSuccessHandler;
		this.authenticationService = authenticationService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/login", "/webjars/**, /registration").permitAll()
				.antMatchers("/mainBoard/**", "/event/**","/searchEvent/**","/addEvent/**","/editEvent/**","/editUser/**").authenticated()
				.and().formLogin()
				.loginPage("/login")
				.successHandler(authenticationSuccessHandler)
				.usernameParameter("username")
				.passwordParameter("password")
				.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
				.invalidateHttpSession(true)
				.permitAll()
				.and()
				.csrf().disable();
		http
				.sessionManagement()
				.maximumSessions(10000)
				.maxSessionsPreventsLogin(false)
				.expiredUrl("/login?logout");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder);
	}
}