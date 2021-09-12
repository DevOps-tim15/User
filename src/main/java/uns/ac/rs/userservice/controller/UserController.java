package uns.ac.rs.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import uns.ac.rs.userservice.domain.User;
import uns.ac.rs.userservice.service.UserService;
import uns.ac.rs.userservice.util.InvalidDataException;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	@GetMapping( value = "/verify-registered-user")
    public ResponseEntity<String> verifyRegisteredUser() {
        return new ResponseEntity<>("Hello registered user!", HttpStatus.OK);
    }
    
	@PreAuthorize("hasRole('ROLE_REGISTERED_USER') || hasRole('ROLE_AGENT')")
    @GetMapping( value = "/follow/{username}")
    public ResponseEntity<?> follow(@PathVariable String username) {
        return new ResponseEntity<>(userService.follow(username), HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/remove/{username}")
	public ResponseEntity<?> getAllReportedPosts(@PathVariable String username) {
		try {
			return new ResponseEntity<>(userService.removeAccount(username), HttpStatus.OK);
		} catch (InvalidDataException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (JsonProcessingException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
    
    
	@PreAuthorize("hasRole('ROLE_REGISTERED_USER') || hasRole('ROLE_AGENT')")
	@PostMapping(value = "/block/{username}")
	public ResponseEntity<?> blockUser(String username) {
		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return new ResponseEntity<>(userService.blockUser(user, username), HttpStatus.OK);
		} catch (InvalidDataException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
}
