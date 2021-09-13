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
import uns.ac.rs.userservice.util.InvalidDataException;

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
	}
	
	@Test
	@Transactional
	@Order(4)
	public void acceptFollowRequest_successfully() throws InvalidDataException {
		String username = "marko";
		String follower = "jova";
		List<String> res = userService.acceptRequest(username, follower);
		assertEquals(0, res.size());
	}
	
	@Test(expected = InvalidDataException.class)
	@Transactional
	@Order(5)
	public void acceptFollowRequest_public_profile() throws InvalidDataException {
		String username = "jova";
		String follower = "marko";
		userService.acceptRequest(username, follower);
	}
	
	@Test
	@Transactional
	@Order(6)
	public void declineFollowRequest_successfully() throws InvalidDataException {
		String username = "marko";
		String follower = "jova";
		List<String> res = userService.declineRequest(username, follower);
		assertEquals(0, res.size());
	}
	
	
}
