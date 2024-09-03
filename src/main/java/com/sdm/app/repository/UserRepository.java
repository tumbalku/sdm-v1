package com.sdm.app.repository;

import com.sdm.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
  Optional<User> findFirstByToken(String token);
  boolean existsByEmail(String email);
  boolean existsByPhone(String phone);
  boolean existsByNip(String nip);
  boolean existsByUsername(String username);
  Optional<User> findByUsername(String username);
}
