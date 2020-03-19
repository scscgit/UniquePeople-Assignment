package eu.scscdev.dev.uniquepeople.assignment.dao;

import eu.scscdev.dev.uniquepeople.assignment.db.Employee;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDAO {

    @Inject
    private EntityManager em;

    @Transactional
    public List<Employee> findAll() {
        return em.createQuery("select e from Employee e", Employee.class)
            .getResultList();
    }

    @Transactional
    public Optional<Employee> findOne(Long id) {
        return em.createQuery("select e from Employee e where e.id = :id", Employee.class)
            .setParameter("id", id)
            .getResultStream()
            .findFirst();
    }

    // TODO: Verify the specification, what's the "name" supposed to mean? I'll assume it's a space-separated format
    @Transactional
    public Optional<Employee> findByName(String name) {
        String[] split = name.split(" ");
        if (split.length != 2) {
            throw new IllegalArgumentException(
                String.format("Invalid parameter name (%s), the Employee name is supposed to contain both first name and last name, space-separated", name)
            );
        }
        return em.createQuery("select e from Employee e where e.firstName = :firstName and e.lastName = :lastName", Employee.class)
            .setParameter("firstName", split[0])
            .setParameter("lastName", split[1])
            .getResultStream()
            .findFirst();
    }

    @Transactional
    public Long delete(Long id) {
        return (long) em.createQuery("delete from Employee e where e.id = :id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Transactional
    public Employee update(Employee e) {
        // TODO: all fields automatically
        em.createQuery("update Employee e set e.firstName = :firstName, e.lastName = :lastName, e.address = :address, e.company = :company")
            .setParameter("firstName", e.getFirstName())
            .setParameter("lastName", e.getLastName())
            .setParameter("address", e.getAddress())
            .setParameter("company", e.getCompany())
            .executeUpdate();
        // TODO: re-implement using other option
        return findOne(e.getId()).get();
    }
}
