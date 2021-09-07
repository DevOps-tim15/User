package uns.ac.rs.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import uns.ac.rs.userservice.domain.Authority;
import uns.ac.rs.userservice.domain.User;
import uns.ac.rs.userservice.repository.AuthorityRepository;
import uns.ac.rs.userservice.repository.UserRepository;
import uns.ac.rs.userservice.util.InvalidDataException;

@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
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
}
