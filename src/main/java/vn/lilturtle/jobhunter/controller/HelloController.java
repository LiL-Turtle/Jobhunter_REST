package vn.lilturtle.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.lilturtle.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class HelloController {

    @GetMapping("/")
    public String getHelloWorld() throws IdInvalidException {
//        if (true) {
//            throw new IdInvalidException("Có lỗi đại vương ơi");
//        }
        return "Hello World, Thiên đây";
    }
}
