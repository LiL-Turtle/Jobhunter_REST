package vn.lilturtle.jobhunter.domain.response;

import lombok.Getter;
import lombok.Setter;
import vn.lilturtle.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
}
