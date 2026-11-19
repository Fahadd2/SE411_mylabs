package edu.psu.se411.lab17.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import edu.psu.se411.lab17.services.JwtService;
import edu.psu.se411.lab17.services.MyUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Runs once per request, before the username/password filters. If a valid
 * Bearer token is present, the request is marked as authenticated.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final MyUserDetailsService userDetailsService;

	public JwtFilter(JwtService jwtService, MyUserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")
				&& SecurityContextHolder.getContext().getAuthentication() == null) {
			String token = authHeader.substring(7); // skip "Bearer "
			try {
				String username = jwtService.extractUsername(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (jwtService.validateToken(token, userDetails)) {
					var authToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			} catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
				// bad, tampered or expired token: stay unauthenticated -> 401
			}
		}
		filterChain.doFilter(request, response); // always continue the chain
	}
}
