package com.silver.sample.restfulwebservice.user;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    private UserDaoService service;

    // 의존성 주입
    public UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUser() {
        return service.findAll();
    }

    // GET method 사용, /users/23 이런식으로 사용 필요 (가변변수 {id})
    // 23 의 숫자도 서버 즉 컨트롤러로 전달될 때엔 문자열로 전달되니
    // 반드시 annotation @PathVariable [원하는 타입] [변수명] 으로 선언할 것
    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id) {
        return service.fineOne(id);

    }
    @PostMapping("/users")
    public void createUser(@RequestBody User user) {
        User savedUser = service.save(user);

        // 사용자에게 상태값 반환
        // Request로 들어온 URI에서 path의 가변변수에 사용자 ID 지정, 마지막에 URI로 변경
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
    }

}
