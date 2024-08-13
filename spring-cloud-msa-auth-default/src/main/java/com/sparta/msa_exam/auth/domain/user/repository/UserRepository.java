package com.sparta.msa_exam.auth.domain.user.repository;

import com.sparta.msa_exam.auth.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
