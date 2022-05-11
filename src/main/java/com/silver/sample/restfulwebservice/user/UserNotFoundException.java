package com.silver.sample.restfulwebservice.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 아래와 같이 annotation 추가 후 NOT_FOUND HTTP Status를 포함시키면
// 500대 Err가 아닌 4XX대의 Client Err로 나오도록 처리할 수 있음
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message); // 부모클래스
    }
}
