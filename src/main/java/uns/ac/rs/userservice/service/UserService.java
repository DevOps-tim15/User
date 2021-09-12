package uns.ac.rs.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import uns.ac.rs.userservice.domain.Authority;
import uns.ac.rs.userservice.domain.User;
import uns.ac.rs.userservice.domain.UserType;
import uns.ac.rs.userservice.dto.UserDTO;
import uns.ac.rs.userservice.kafka.Producer;
import uns.ac.rs.userservice.kafka.domain.UserMessage;
import uns.ac.rs.userservice.kafka.domain.UsersFollowBlockMute;
import uns.ac.rs.userservice.mapper.UserMapper;
import uns.ac.rs.userservice.repository.AuthorityRepository;
import uns.ac.rs.userservice.repository.UserRepository;
import uns.ac.rs.userservice.util.InvalidDataException;

@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Autowired
	private Producer producer;
	
	public User saveRegisteredUser(User user) throws InvalidDataException {
		System.out.println("SAVE USERUSER SERVICE");
		System.out.println(user);
		
		User u = findByUsername(user.getUsername());
		if(u != null) {
			throw new InvalidDataException("Username already taken!"); 
		}
		
		if(findUserById(user.getId()).isPresent()) {
			throw new InvalidDataException("Wrong ID!"); 
		}
		
		u = findByEmail(user.getEmail());
		if(u != null) {
			throw new InvalidDataException("Email already taken!"); 
		}
		
		if(Stream.of(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword()).anyMatch(Objects::isNull)) {
			throw new InvalidDataException("Some data is missing");
		}
		
		if (user.getUsername().isEmpty() || user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getEmail().isEmpty()
				|| user.getPassword().isEmpty()) {
			throw new InvalidDataException("User information is incomplete!");
		}
		List<Authority> authorities = new ArrayList<Authority>();
		Authority a = findAuthority(1);
		authorities.add(a);
		user.setAuthorities(authorities);
		this.userRepository.save(user);
		return user;
	}
	
	public User updateUser(User ru, String oldUsername, UserType role) throws InvalidDataException{
		User user = findByUsername(oldUsername);
		User u = findByUsername(ru.getUsername());
		if(u != null && (!ru.getUsername().equals(user.getUsername()))) {
			throw new InvalidDataException("Username already taken!"); 
		}
		u = findByEmail(ru.getEmail());
		if(u != null && (!ru.getEmail().equals(user.getEmail()))) {
			throw new InvalidDataException("Email already taken!"); 
		}
		
		if(Stream.of(ru.getUsername(), ru.getFirstName(), ru.getLastName(), ru.getEmail(), ru.getPassword()).anyMatch(Objects::isNull)) {
			throw new InvalidDataException("Some data is missing");
		}
		
		if (ru.getUsername().isEmpty() || ru.getFirstName().isEmpty() || ru.getLastName().isEmpty() || ru.getEmail().isEmpty()
				|| ru.getPassword().isEmpty()) {
			throw new InvalidDataException("User information is incomplete!");
		}
		
		if (ru.getUsername().isEmpty() || ru.getFirstName().isEmpty() || ru.getLastName().isEmpty() || ru.getEmail().isEmpty()
				|| ru.getPassword().isEmpty()) {
			throw new InvalidDataException("User information is incomplete!");
		}

		user.setUsername(ru.getUsername());
		user.setFirstName(ru.getLastName());
		user.setLastName(ru.getLastName());
		user.setEmail(ru.getEmail());
		user.setPhone(ru.getPhone());
		user.setSex(ru.getSex());
		user.setWebsiteUrl(ru.getWebsiteUrl());
		user.setBiography(ru.getBiography());
		user.setBirthDate(ru.getBirthDate());
		user.setIsPrivate(ru.getIsPrivate());
		user.setCanBeTagged(ru.getCanBeTagged());
//		Authority a = this.authorityRepository.findAuthorityByUserType(role);
//		List<Authority> authorities = new ArrayList<>();
//		authorities.add(a);
//		user.setAuthorities(authorities);
		user = this.userRepository.save(user);
		SecurityContextHolder.clearContext();
		return user;
	}
	
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public Optional<User> findUserById(Long id) {
		return userRepository.findById(id);
	}
	
	public Authority findAuthority(Integer id) {
		return authorityRepository.findById(id).get();
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails u =  userRepository.findByUsername(username);
		if(u!= null)
			return u;
		else
			throw new UsernameNotFoundException(String.format("User with username '%s' not found", username));
	}
	
	public List<User> usersThatIFollow(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			return user.getFollowing();
		} else {
			throw new UsernameNotFoundException(String.format("User with username '%s' not found", username));
		}
	}
	
	public UsersFollowBlockMute usersThatIFollowBlockedAndMuted(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		UsersFollowBlockMute users = new UsersFollowBlockMute();
		if (user != null) {
			users.setFollowing(user.getFollowing());
			users.setBlock(user.getBlockedUsers());
			users.getBlock().addAll(user.getBlockedByUsers());
			return users;
		} else {
			throw new UsernameNotFoundException(String.format("User with username '%s' not found", username));
		}
	}

	public Boolean follow(String username) throws UsernameNotFoundException{
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User u = userRepository.findByUsername(user.getUsername());
		User friend = userRepository.findByUsername(username);
		if (friend == null) {
			throw new UsernameNotFoundException(String.format("User with username '%s' not found", username));
		}
		if (!friend.getIsPrivate()) {
			u.getFollowing().add(friend);
			userRepository.save(u);
			return true;
		} else {
			friend.getFollowingRequests().add(u);
			userRepository.save(friend);
			return false;
		}
		
	}

	public Long removeAccount(String username) throws InvalidDataException, JsonProcessingException{
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new InvalidDataException("Wrong username.");
		}
		userRepository.delete(user);
		UserMessage message = new UserMessage(user, "remove");
		producer.sendMessageToTopic("user-topic", message);
		return user.getId();
	}
	
	
	public UserDTO blockUser(User u, String username) throws InvalidDataException{
		System.out.println("********************"+ username);
		User userForBlocking = this.findByUsername(username);
		if (userForBlocking == null) {
			throw new InvalidDataException("Invalid user for blocking.");
		}
		
		User userWhoBlocks = this.findByUsername(u.getUsername());
		if (userWhoBlocks == null) {
			throw new InvalidDataException("Invalid user that blocks.");
		}
		userWhoBlocks.getBlockedUsers().add(userForBlocking);
		userForBlocking.getBlockedByUsers().add(userWhoBlocks);
		userRepository.save(userWhoBlocks);
		userRepository.save(userForBlocking);
		UserDTO dto = UserMapper.fromEntity(userForBlocking);
		return dto;
	}
}
