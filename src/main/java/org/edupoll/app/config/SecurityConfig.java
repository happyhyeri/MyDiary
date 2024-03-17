package org.edupoll.app.config;

import org.edupoll.app.common.CustomUserDetailsService;
import org.edupoll.app.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// 1. restricting access

		http.authorizeHttpRequests(t -> t.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
				.requestMatchers("/auth/register").permitAll().requestMatchers("/static/**").permitAll()
				.anyRequest().authenticated());

		http.anonymous(t -> t.disable()); // 익명인증 안받겠다.

		// 2 .custom login form setting
		http.formLogin(t -> t.loginPage("/auth/login").permitAll().usernameParameter("id"));

		// 3. custom logout configuration
		http.logout(t -> t.logoutUrl("/logout").logoutSuccessUrl("/auth/login").permitAll());
		
		return http.build();

	}

	@Bean
	public UserDetailsService jpaUsers(AccountRepository accountRepository) {

		return new CustomUserDetailsService(accountRepository);
	}

}
