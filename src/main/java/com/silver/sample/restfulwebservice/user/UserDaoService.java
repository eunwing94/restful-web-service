package com.silver.sample.restfulwebservice.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class UserDaoService {
    // 사용자 정보 로직 (비지니스 로직)
    private static List<User> users = new ArrayList<>();

    private static int userCount = 3;

    static {
        users.add(new User(1,"Kenneth", new Date()));
        users.add(new User(2,"Alice", new Date()));
        users.add(new User(3,"Elena", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User fineOne(int id){    // 조건절을 이용한 조회
        for(User user : users) {
            if(user.getId()==id){
                return user;
            }
        }
        return null;
    }

    public User save(User user){
        if(user.getId()==null){
            user.setId(++userCount);
        }
        users.add(user);
        return user;
    }
}
