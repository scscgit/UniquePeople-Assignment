package eu.scscdev.dev.uniquepeople.assignment.repository;

import eu.scscdev.dev.uniquepeople.assignment.db.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Long> {
}
