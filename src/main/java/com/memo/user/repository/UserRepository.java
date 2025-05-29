package com.memo.user.repository;

import com.memo.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    // 한 행이거나 null로 예상될 경우, Optional 리턴 타입 (단 건)
    // 그 이상일 경우 List 타입
    public Optional<UserEntity> findByLoginId(String loginId);
    public Optional<UserEntity> findByLoginIdAndPassword(String loginId, String password);
}