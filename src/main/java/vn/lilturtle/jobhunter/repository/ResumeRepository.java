package vn.lilturtle.jobhunter.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.lilturtle.jobhunter.domain.Resume;
import vn.lilturtle.jobhunter.domain.User;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {


}
