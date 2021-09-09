package uns.ac.rs.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uns.ac.rs.userservice.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	@GetMapping( value = "/verify-registered-user")
    public ResponseEntity<String> verifyRegisteredUser() {
        return new ResponseEntity<>("Hello registered user!", HttpStatus.OK);
    }
    
    @GetMapping( value = "/follow/{username}")
    public ResponseEntity<?> follow(@PathVariable String username) {
        return new ResponseEntity<>(userService.follow(username), HttpStatus.OK);
    }
}
