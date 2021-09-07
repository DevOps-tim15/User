package uns.ac.rs.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uns.ac.rs.userservice.domain.Authority;


public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

}
