package vn.lilturtle.jobhunter.domain.response;

import lombok.Getter;
import lombok.Setter;
import vn.lilturtle.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Setter
@Getter
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
}
