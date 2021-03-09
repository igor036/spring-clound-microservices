package com.linecode.auth.repository;

import com.linecode.auth.entity.AuthorityRule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRuleRepository extends JpaRepository<AuthorityRule, Long> {

}
