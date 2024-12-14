	package com.josepedevs.application.config;
	
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.security.authentication.AuthenticationProvider;
	import org.springframework.security.config.annotation.web.builders.HttpSecurity;
	import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
	import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
	import org.springframework.security.config.http.SessionCreationPolicy;
	import org.springframework.security.web.SecurityFilterChain;
	import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
	
	import lombok.AllArgsConstructor;
	
	@Configuration
	@AllArgsConstructor
	@EnableWebSecurity
	public class FilterAndWhiteListConfig {
	
		private final JwtValidationFilter jwtAuthFilter;
		private final AuthenticationProvider authenticationProvider;
		
		@Bean
		/**
		 * specifies whitelisting and what methods/urls need to be protected
		 * @param http
		 * @return
		 * @throws Exception
		 */
		SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			   http
			   //Cross-Site Request Forgery (CSRF),ricks the victim into submitting a malicious request
			   //A CSRF token is a unique, randomly generated value associated with a user's session
			   //alternative authentication such as JSON Web Tokens (JWT), might not require CSRF protection because JWTs inherently carry state information 
			   //that can be used to verify used, roles and more.
			   //That is why this is not required and can be disabled
	           .csrf(AbstractHttpConfigurer::disable)
	           //all the urls that match the pattern are permitted, any any other will require authentication. 
	           //in that path controller we will only place endpoints that do nor require auth such as register or login
	           //We do not require a token for register nor for "requesting the login token" what right now i am calling "login" in reality is authenticate 
	           .authorizeHttpRequests(registry -> {
	        	   registry.requestMatchers("api/v1/noauth/**", "otra/url").permitAll(); //permitir tds los que tengan una ruta especifica
	        	   registry.requestMatchers("api/v1/admin/**").hasAuthority("ADMIN");//solo pueden consultar eso administradores
	        	   registry.anyRequest().authenticated();
	           })         
	           //The session should be stateless, DO NOT SAVE ANYTHING IN THE SESSION!
	           .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	           //
	           .authenticationProvider(authenticationProvider)
	           //add filter of our own, just before the chosen filter/class, this line wil make jwtAuthFilter be excecuted before UsernamePassword..Filter
	           //our filter checks userdetails are not null and correct and that the token is not expired
	           .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
			return http.build();
		}
		
		
	}
