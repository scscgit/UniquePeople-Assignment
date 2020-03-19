package eu.scscdev.dev.uniquepeople.assignment.repository;

import eu.scscdev.dev.uniquepeople.assignment.db.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
