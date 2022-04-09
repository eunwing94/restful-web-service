package com.silver.sample.restfulwebservice;

import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.web.bind.annotation.GetMapping;
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
}
