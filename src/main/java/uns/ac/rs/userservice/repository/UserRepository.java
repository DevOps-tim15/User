package uns.ac.rs.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uns.ac.rs.userservice.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	User findByUsername(String username);

}
