package uns.ac.rs.userservice.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import uns.ac.rs.userservice.kafka.Producer;
import uns.ac.rs.userservice.service.UserService;

//Filter koji ce presretati svaki zahtev klijenta ka serveru
//Sem nad putanjama navedenim u WebSecurityConfig.configure(WebSecurity web)
public class TokenAuthenticationFilter extends OncePerRequestFilter {
	
	private Producer producer;
	
	private TokenUtils tokenUtils;
	
	private UserService userService;

	public TokenAuthenticationFilter(TokenUtils tokenHelper, Producer producer, UserService userService) {
		this.tokenUtils = tokenHelper;
		this.producer = producer;
		this.userService = userService;
	}
	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String authToken = tokenUtils.getToken(request);

		if (authToken != null) {

			try {
				String username = producer.checkUser(authToken);
				
				if(username != null) {
					UserDetails userDetails = userService.loadUserByUsername(username);
					TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
					authentication.setToken(authToken);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
		chain.doFilter(request, response);
	}

}