package org.zerock.smumap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.smumap.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
