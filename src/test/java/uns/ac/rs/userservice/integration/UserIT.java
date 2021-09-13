package uns.ac.rs.userservice.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uns.ac.rs.userservice.domain.User;
import uns.ac.rs.userservice.repository.UserRepository;
import uns.ac.rs.userservice.service.UserService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UserIT {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@Transactional
	@Order(1)
	public void follow_successfully() {
		String username = "jova";
		String follower = "marko";
		Boolean res = userService.follow(username, follower);
		assertEquals(true, res);
		User user = userRepository.findByUsername(username);
		assertEquals(1, user.getFollowing().size());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	@Transactional
	@Order(2)
	public void follow_user_does_not_exist() {
		String username = "abc";
		String follower = "marko";
		userService.follow(username, follower);
	}
	
	@Test
	@Transactional
	@Order(3)
	public void follow_request_successfully() {
		String username = "marko";
		String follower = "jova";
		Boolean res = userService.follow(username, follower);
		assertEquals(false, res);
		User user = userRepository.findByUsername(username);
		assertEquals(1, user.getFollowingRequests().size());
	}
}
