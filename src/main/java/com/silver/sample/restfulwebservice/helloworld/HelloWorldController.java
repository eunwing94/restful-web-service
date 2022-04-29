package com.silver.sample.restfulwebservice.helloworld;

import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    // @RequestMapping(method=RequestMethod.GET, path="/hello-world") 와 아래 GetMapping은 동일
   @GetMapping( "/hello-world")
    public String helloWorld(){
        String result = "Hello World!";

        System.out.println("Result = " + result);
        return result;
    }

    @GetMapping( path = "/hello-world-bean")
    public HelloWorldBean helloWorldBean(){
        HelloWorldBean result = new HelloWorldBean("Hello World Bean!");
        System.out.println("Result = " + result);

        return result;
    }

    @GetMapping( path = "/hello-world-bean/path-variable/{name}") //{name}과 아래 String name 변수명 동일해야함
    public HelloWorldBean helloWorldBean(@PathVariable(value="name") String name){ // 동일한 이름의 메서드와 오버로딩
        HelloWorldBean result = new HelloWorldBean(String.format("Hello World, I am %s", name));
        System.out.println("Result = " + result);

        return result;
    }
}
