package eu.scscdev.dev.uniquepeople.assignment.dao;

import eu.scscdev.dev.uniquepeople.assignment.db.Employee;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeDAO {

    private final EntityManager em;

    public List<Employee> findAll() {
        return em.createQuery("from Employee e", Employee.class)
            .getResultList();
    }

    public Optional<Employee> findOne(Long id) {
        return em.createQuery("from Employee e where e.id = :id", Employee.class)
            .setParameter("id", id)
            .getResultStream()
            .findFirst();
    }

    /**
     * Find one Employee by both a first name and a last name.
     *
     * @param name two space-separated names in the following order: first name, last name
     */
    public Optional<Employee> findByName(String name) {
        // TODO: Verify the specification, what's the "name" supposed to mean? I'll assume it's a space-separated format
        var split = name.split(" ");
        if (split.length != 2) {
            throw new IllegalArgumentException(
                String.format("Invalid parameter name (%s), the Employee name is supposed to contain both first name and last name exactly once, space-separated", name)
            );
        }
        return em.createNamedQuery("Employee.findByName", Employee.class)
            .setParameter("firstName", split[0])
            .setParameter("lastName", split[1])
            .getResultStream()
            .findFirst();
    }

    /**
     * Delete one Employee.
     *
     * @param id ID of the Employee
     * @return Number of affected rows on the interval of 0 to 1 inclusively
     */
    public Long delete(Long id) {
        // The semantics of return value wasn't defined, assuming it's the update result
        return (long) em.createQuery("delete from Employee e where e.id = :id")
            .setParameter("id", id)
            .executeUpdate();
    }

    /**
     * Persist or update one Employee.
     *
     * @param employee Employee to be persisted or updated, based on already having assigned an ID.
     * @return Managed instance of the updated Employee.
     */
    public Employee update(Employee employee) {
        // The specification wasn't clear: should update also persist a new instance (if the id is null)? Assuming yes.
        return em.merge(employee);
    }
}
