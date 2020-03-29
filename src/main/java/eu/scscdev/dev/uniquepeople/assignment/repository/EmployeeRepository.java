package eu.scscdev.dev.uniquepeople.assignment.repository;

import eu.scscdev.dev.uniquepeople.assignment.db.Employee;
import lombok.var;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    default Optional<Employee> findByName(String name) {
        var split = name.split(" ");
        if (split.length != 2) {
            throw new IllegalArgumentException(
                String.format("Invalid parameter name (%s), the Employee name is supposed to contain both first name and last name exactly once, space-separated", name)
            );
        }
        return findByFirstNameAndLastName(split[0], split[1]);
    }

    Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Employee> findByFirstName(String firstName);

    Optional<Employee> findByLastName(String lastName);

    // Like deleteById, but with an explicit return value of the number of affected rows
    Long removeById(Long id);
}
