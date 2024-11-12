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
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private long id;
    private String email;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private Instant createdAt;
    private companyUser company;


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class companyUser {
        private long id;
        private String name;
    }


}

