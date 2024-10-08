package vn.lilturtle.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.lilturtle.jobhunter.domain.Company;
import vn.lilturtle.jobhunter.service.CompanyService;

import java.util.List;

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

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> listCompanies = this.companyService.fetchAllCompanies();
        return ResponseEntity.ok(listCompanies);
    }


    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company updateCompany) {
        updateCompany = this.companyService.handleUpdateCompany(updateCompany);
        return ResponseEntity.ok(updateCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable int id) {
        this.companyService.deleteCompanyById(id);
        return ResponseEntity.noContent().build();
    }
}
