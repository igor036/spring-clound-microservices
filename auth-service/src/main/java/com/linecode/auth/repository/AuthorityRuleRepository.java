package com.linecode.auth.repository;

import java.util.Optional;

import com.linecode.auth.entity.AuthorityRule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRuleRepository extends JpaRepository<AuthorityRule, Long> {

    Optional<AuthorityRule> findByDescription(String description);

}
