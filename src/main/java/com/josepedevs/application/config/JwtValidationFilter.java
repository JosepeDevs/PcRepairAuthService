package com.josepedevs.application.config;

import java.io.IOException;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.application.service.JwtTokenDataExtractorService;
import com.josepedevs.application.service.JwtTokenValidations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

	private final JwtTokenDataExtractorService jwtTokenReaderService;
	private final JwtTokenValidations jwtValidations;
	private final UserDetailsService userDetailsService;
	private final AuthenticationDataRepository repository;
	private final Logger logger = LoggerFactory.getLogger(JwtValidationFilter.class);

	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		// intecept all requests to filter as our needs, filterchain is other filters that are required after this one
		final String authorizationHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String username;
		
		if(authorizationHeader == null || ! authorizationHeader.startsWith("Bearer ")) {
			//if not authorized
			logger.info("No token found, JwtValidationFilter skipped");
			filterChain.doFilter(request, response);
			return; //this is included to stop execution after calling filterChain.
		}
		
		//"Bearer " are 7 digits, with this we get in a string the token value
		jwtToken = authorizationHeader.substring(7).replace (" ","");
		//we need a class to read from the JwtToken
		username = jwtTokenReaderService.extractUsername(jwtToken); 
		boolean trueOrWillthrowException = repository.isTokenInvalidated(username);
		//if already authenticated skip all the generation process. If authentication is null, the user is not authenticated yet
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
			//get user details from database
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			logger.trace("test");
			if( jwtValidations.isUserTokenCompletelyValidated(jwtToken, userDetails) ){
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
		logger.info("JwtValidationFilter finished checking, continuing to next filter...");
		filterChain.doFilter(request, response);
	}

}
