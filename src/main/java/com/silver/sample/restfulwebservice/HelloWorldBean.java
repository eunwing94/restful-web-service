package com.silver.sample.restfulwebservice;
//lombok


import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class HelloWorldBean {
    private String message;


    /* lombok을 쓰면 생성자도, 아래 Getter Setter 사용 필요없음 */
    /*

    public HelloWorldBean(String message){
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }
     */

}
