package vn.lilturtle.jobhunter.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private companyUser company;
    private roleUser role;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class companyUser {
        private long id;
        private String name;
    }

    @Setter
    @Getter
    public static class roleUser {
        private long id;
        private String name;
    }
}
