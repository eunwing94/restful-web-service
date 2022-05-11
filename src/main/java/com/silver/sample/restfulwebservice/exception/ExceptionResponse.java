package com.silver.sample.restfulwebservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/*Spring AOP 공통적으로 ControllerBean 단에서 처리해야하는 로직*/

@Data
@AllArgsConstructor /*모든 field를 갖고있는 생성자*/
@NoArgsConstructor /*매개변수가 없는 default 생성자 ExceptionResponse()*/
public class ExceptionResponse {
    private Date timestamp;
    private String message;
    private String details;
}
