package vn.lilturtle.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.lilturtle.jobhunter.domain.Company;
import vn.lilturtle.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleSaveCompany(Company company) {
        return this.companyRepository.save(company);
    }
}
