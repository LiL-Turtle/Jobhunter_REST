package vn.lilturtle.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.lilturtle.jobhunter.domain.Company;
import vn.lilturtle.jobhunter.domain.User;
import vn.lilturtle.jobhunter.domain.response.ResultPaginationDTO;
import vn.lilturtle.jobhunter.repository.CompanyRepository;
import vn.lilturtle.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
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

    public ResultPaginationDTO fetchAllCompanies(Specification<Company> spec, Pageable pageable) {

        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageCompany.getNumber() + 1);
        mt.setPageSize(pageCompany.getSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());

        return rs;
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

    public Optional<Company> findById(long id) {
        return this.companyRepository.findById(id);
    }

    public void deleteCompanyById(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company com = companyOptional.get();
            //fetch all user belong to this company
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }
        this.companyRepository.deleteById(id);
    }
}
