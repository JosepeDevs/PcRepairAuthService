package com.josepdevs.Domain.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.josepdevs.Domain.service.JwtTokenReaderService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

	private final JwtTokenReaderService jwtTokenReaderService;
	private final UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		// intecept all requests to filter as our needs, filterchain is other filters that are required after this one
		final String authorizationHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String username;
		
		if(authorizationHeader == null || ! authorizationHeader.startsWith("Bearer ")) {
			//if not authorized
			filterChain.doFilter(request, response);
			return; //this is included to stop execution after calling filterChain.
		}
		
		//"Bearer " are 7 digits, with this we get in a string the token value
		jwtToken = authorizationHeader.substring(7).replace (" ","");
		//we need a class to read from the JwtToken
		username = jwtTokenReaderService.extractUsername(jwtToken); 
		//if already authenticated skip all the generation process. If authentication is null, the user is not authenticated yet
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
			//get user details from database
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if( jwtTokenReaderService.isTokenUserDataValidAndNotExpired(jwtToken, userDetails) ){
				//by being able to create this token we have secured this request
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				//convert HttpServlet request class into WebAuthenticationDetails Spring class equivalent to request
				//therefore this line bellow set details with all the infomration from the request (using all info
				//even new things that HttpServlet did not have)
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//update authentication token
				SecurityContextHolder.getContext().setAuthentication(authToken);
			};
		}
		filterChain.doFilter(request, response);
	}

}
