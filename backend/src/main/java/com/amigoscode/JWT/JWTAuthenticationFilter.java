package com.amigoscode.JWT;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.amigoscode.customer.CustomerUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter{

	private final JWTUtil jwtUtil;
	private UserDetailsService userDetailsService;
	
	public JWTAuthenticationFilter(JWTUtil jwtUtil, CustomerUserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
}
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		
			String authHeader = request.getHeader("Authorization");
			
			if(authHeader==null || !authHeader.startsWith("Bearer "))
			{
				filterChain.doFilter(request, response); //execute next filter
				return; // need to break out
			}
			
			String jwtToken = authHeader.substring(7);
			
			String subject=jwtUtil.getSubject(jwtToken);
			System.out.println("claims: "+jwtUtil.getClaims(jwtToken));
			
			if(subject!=null || SecurityContextHolder.getContext().getAuthentication() == null)
			{
				//user details
				UserDetails userDetails=userDetailsService.loadUserByUsername(subject); //find email & then check if its valid 
				if(jwtUtil.isTokenValid(jwtToken,userDetails.getUsername() ))
				{
					//create a token
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
							(	userDetails,
								null,
								userDetails.getAuthorities()
									);
					//create details of token
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken); //add user to context which allows them to be authenticated
				}
			}
			
			filterChain.doFilter(request, response);
	}

}
