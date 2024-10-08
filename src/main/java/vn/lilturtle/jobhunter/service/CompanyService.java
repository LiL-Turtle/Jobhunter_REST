package vn.lilturtle.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.lilturtle.jobhunter.domain.Company;
import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleSaveCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public Company fecthCompanyById(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            return companyOptional.get();
        }
        return null;
    }

    public List<Company> fetchAllCompanies() {
        return this.companyRepository.findAll();
    }

    public Company handleUpdateCompany(Company company) {
        Company currentCompany = this.fecthCompanyById(company.getId());

        if (currentCompany != null) {

            if (currentCompany.getName() != null && !currentCompany.getName().equals(company.getName())) {
                currentCompany.setName(company.getName());
            } else if (currentCompany.getDescription() != null && !currentCompany.getDescription().equals(company.getDescription())) {
                currentCompany.setDescription(company.getDescription());
            } else if (currentCompany.getAddress() != null && !currentCompany.getAddress().equals(company.getAddress())) {
                currentCompany.setAddress(company.getAddress());
            } else if (currentCompany.getLogo() != null && !currentCompany.getLogo().equals(company.getLogo())) {
                currentCompany.setLogo(company.getLogo());
            }
            currentCompany = this.companyRepository.save(currentCompany);
        }

        return currentCompany;
    }

    public void deleteCompanyById(long id) {
        this.companyRepository.deleteById(id);
    }
}