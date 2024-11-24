package vn.lilturtle.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.lilturtle.jobhunter.domain.Company;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.service.CompanyService;
import vn.lilturtle.jobhunter.util.annotation.ApiMessage;
import vn.lilturtle.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
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

    @GetMapping("/companies/{id}")
    @ApiMessage("Fetch a company")
    public ResponseEntity<Company> fetchACompany(@PathVariable("id") Long id) throws IdInvalidException {
        Optional<Company> optionalCompany = this.companyService.findById(id);
        if (!optionalCompany.isPresent()) {
            throw new IdInvalidException(
                    "Company with id " + id + " does not exist"
            );
        }
        return ResponseEntity.ok(optionalCompany.get());

    }

    @GetMapping("/companies")
    @ApiMessage("Fetch companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter
            Specification<Company> spec,
            Pageable pageable
    ) {
        ResultPaginationDTO listCompanies = this.companyService.fetchAllCompanies(spec, pageable);
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
        return ResponseEntity.ok(null);
    }
}
