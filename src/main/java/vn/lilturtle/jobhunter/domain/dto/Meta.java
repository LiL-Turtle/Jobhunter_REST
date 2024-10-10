package vn.lilturtle.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {

    private int page;
    private int pageSize;
    private int pages; // Số trang
    private long total; // Tổng số phần tử
}
