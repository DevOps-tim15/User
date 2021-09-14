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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import uns.ac.rs.userservice.domain.User;
import uns.ac.rs.userservice.dto.UserDTO;
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
	
	@Test
	@Transactional
	@Order(7)
	public void block_successfully() throws InvalidDataException {
		String isBlocking = "jova";
		User u = userService.findByUsername(isBlocking);
		String forBlocking = "mika";
		UserDTO user = userService.blockUser(u, forBlocking);
		assertEquals(forBlocking , user.getUsername());
		User userBlocking = userService.findByUsername(isBlocking);
		User userBlocked = userService.findByUsername(forBlocking);
		assertEquals(userBlocking.getBlockedUsers().get(0).getUsername(), forBlocking);
		assertEquals(userBlocked.getBlockedByUsers().get(0).getUsername(), isBlocking);
		assertEquals(userBlocked.getBlockedByUsers().get(0).getUsername(), isBlocking);
	}
	
	
	@Test(expected = InvalidDataException.class)
	@Transactional
	@Order(8)
	public void block_unsuccessfully() throws InvalidDataException {
		String isBlocking = "mika";
		User u = userService.findByUsername(isBlocking);
		String forBlocking = "marko";
		userService.blockUser(u, forBlocking);
	}
	
	
	@Test
	@Transactional
	@Order(9)
	public void mute_successfully() throws InvalidDataException {
		String isMuting = "mara";
		User u = userService.findByUsername(isMuting);
		String forMuting = "mika";
		UserDTO user = userService.muteUser(u, forMuting);
		assertEquals(forMuting , user.getUsername());
		User userMuting = userService.findByUsername(isMuting);
		User userMuted = userService.findByUsername(forMuting);
		assertEquals(userMuting.getMutedUsers().get(0), userMuted);
	}
	
	@Test(expected = InvalidDataException.class)
	@Transactional
	@Order(10)
	public void mute_unsuccessfully() throws InvalidDataException {
		String isMuting= "mika";
		User u = userService.findByUsername(isMuting);
		String forMuting = "marko";
		userService.muteUser(u, forMuting);
	}
	
	@Test
	@Transactional
	@Order(11)
	public void unmute_successfully() throws InvalidDataException {
		String isUnmuting = "mara";
		User userUnmuting = userService.findByUsername(isUnmuting);
		String forUnmuting = "mika";
		User userUnmuted = userService.findByUsername(forUnmuting);
		userService.muteUser(userUnmuting, forUnmuting);
		assertEquals(userUnmuting.getMutedUsers().get(0), userUnmuted);

		UserDTO user = userService.unmuteUser(userUnmuting, forUnmuting);
		assertEquals(forUnmuting , user.getUsername());
		userUnmuting = userService.findByUsername(isUnmuting);
		assertEquals(0, userUnmuting.getMutedUsers().size());
	}
	
	@Test(expected = InvalidDataException.class)
	@Transactional
	@Order(12)
	public void unmute_unsuccessfully() throws InvalidDataException {
		String isUnmuting= "mika";
		User u = userService.findByUsername(isUnmuting);
		String forMuting = "marko";
		userService.unmuteUser(u, forMuting);
	}
	
	
}
