package vn.lilturtle.jobhunter.domain.response.Resume;

import lombok.Getter;
import lombok.Setter;
import vn.lilturtle.jobhunter.util.constant.ResumeStateEnum;

import java.time.Instant;

@Getter
@Setter
public class ResUpdateResumeDTO {
    private ResumeStateEnum status;
    private Instant updatedAt;
    private String updatedBy;
}
