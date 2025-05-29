package com.memo.user.service;

import com.memo.common.HashUtils;
import com.memo.user.entity.UserEntity;
import com.memo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

// 생성자 주입 Dependency Injection => Spring bean 주입
@RequiredArgsConstructor
@Service
public class UserBO {
    private final UserRepository userRepository;

//    public UserBO(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    // input: loginId
    // output: boolean true:중복
    public boolean isDuplicateLoginId(String loginId) {
        // select * from user where loginId = ?
        UserEntity user = userRepository.findByLoginId(loginId).orElse(null);
        return user != null;
    }

    // i: 4개 파라미터
    // o: UserEntity => boolean(성공 true)
    public boolean addUser(String loginId, String password,
                           String name, String email) {

        // 암호화 -> 복호화 X
        // hashing 단방향 => md5
        // aaaa -> qjkejrfdslfja
        // aaaa -> qjkejrfdslfja
        String hashedPassword = HashUtils.md5(password);

        UserEntity user = userRepository.save(UserEntity.builder()
                .loginId(loginId)
                .password(hashedPassword)
                .name(name)
                .email(email)
                .build());
        return user != null; // 회원가입 잘 되면 true
    }

    // input:loginId, password
    // output:UserEntity(단건) or null(로그인 실패)
    public UserEntity getUserEntityByLoginIdPassword(String loginId, String password) {
        // 비밀번호 해싱
        String hashedPassword = HashUtils.md5(password);

        // DB 조회
        return userRepository.findByLoginIdAndPassword(loginId, hashedPassword).orElse(null);
    }
}