package com.silver.sample.restfulwebservice.user;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data /* 모든 필드를 대상으로 접근자와 설정자가 자동으로 생성 */
@AllArgsConstructor /* 모든 field에 대해 parameter를 갖는 생성자 */
public class User {
    private Integer id;
    private String name;
    private Date joinDate;
}
