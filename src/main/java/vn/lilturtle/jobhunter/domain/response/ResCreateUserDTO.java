package vn.lilturtle.jobhunter.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.lilturtle.jobhunter.domain.Company;
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
    private companyUser company;

    @Getter
    @Setter
    public static class companyUser {
        private long id;
        private String name;
    }
}
