package com.linecode.auth.repository;

import java.util.Optional;

import com.linecode.auth.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    //@Query("SELECT u FROM user u WHERE u.user_name = :userName")
    Optional<User> findByUsername(String username);
    
}
