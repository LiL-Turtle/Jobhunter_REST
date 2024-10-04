package vn.lilturtle.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.lilturtle.jobhunter.domain.Company;
import vn.lilturtle.jobhunter.service.CompanyService;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> postCreateCompany(@Valid @RequestBody Company reqCompany) {
        Company binCompany = this.companyService.handleSaveCompany(reqCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(binCompany);
    }
}
