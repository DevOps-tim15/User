package uns.ac.rs.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	@GetMapping( value = "/verify-registered-user")
    public ResponseEntity<String> verifyRegisteredUser() {
        return new ResponseEntity<>("Hello registered user!", HttpStatus.OK);
    }
}
