package com.silver.sample.restfulwebservice.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class UserDaoService {
    // 사용자 정보 로직 (비지니스 로직)
    private static List<User> users = new ArrayList<>();

    private static int userCount = 3;

    // 고정 초깃값
    static {
        users.add(new User(1,"Kenneth", new Date()));
        users.add(new User(2,"Alice", new Date()));
        users.add(new User(3,"Elena", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User findOne(int id){    // 조건절을 이용한 조회
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

    public User deleteById(int id) {

        Iterator<User> iterator = users.iterator();
        while(iterator.hasNext()){
            User user = iterator.next();
            if(user.getId() == id){
                iterator.remove();
                return user;
            }
        }

        return null;
    }

    public User update(int id, User user){
        // 특정 User를 선언해줘야함
        User updateUser = findOne(id);
        System.out.println("==========DEBUG==========NAME : "+user.getName());
        System.out.println("==========DEBUG==========ID : "+user.getId());
        System.out.println("==========DEBUG==========ID(param) : "+id);

        updateUser.setName(user.getName());
        updateUser.setJoinDate(new Date());   // 현재 시점 저장


        return updateUser;
    }
}
