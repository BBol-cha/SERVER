package project.BBolCha.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.BBolCha.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneWithAuthoritiesByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}