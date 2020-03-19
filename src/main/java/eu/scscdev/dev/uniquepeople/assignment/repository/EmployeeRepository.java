package eu.scscdev.dev.uniquepeople.assignment.repository;

import eu.scscdev.dev.uniquepeople.assignment.db.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// An alternative implementation
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    // TODO name?

    Optional<Employee> findByFirstName(String firstName);

    Optional<Employee> findByLastName(String lastName);

    // TODO: delete doesn't return long here, is it a problem?
}
