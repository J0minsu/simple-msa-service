package com.sparta.msa_exam.auth.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    private String userId;
    private String username;
    private String password;
    private String role;
}

/**
 * 질문
 * 1. 사용자 권한 Member, Manager, Admin 등은 String 으로 관리하는가?
 *      Human error 를 방지하고자 Enum 을 사용해왔는데, MSA 구조에서 어떻게
 *      관리하는지 궁금하다.
 * 2. Layered architecture
 */