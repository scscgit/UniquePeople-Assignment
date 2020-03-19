package eu.scscdev.dev.uniquepeople.assignment;

import eu.scscdev.dev.uniquepeople.assignment.dao.EmployeeDAO;
import eu.scscdev.dev.uniquepeople.assignment.db.Employee;
import eu.scscdev.dev.uniquepeople.assignment.repository.EmployeeRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EmployeeTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeDAO employeeDAO;

    @Test
    public void testEmployeeDao() {
        createEmployee();
        List<Employee> employees = employeeDAO.findAll();
        Assert.assertEquals(1, employees.size());
        Employee employee = employeeRepository.findAll().get(0);
        Assert.assertEquals(1L, employee.getId().longValue());

        employeeDAO.findOne(employee.getId());
        Assert.assertTrue(
            employeeDAO.findOne(employee.getId()).isPresent()
        );
        Assert.assertTrue(
            employeeDAO.findByName("FirstName LastName").isPresent()
        );

        employeeDAO.delete(employee.getId());
        Assert.assertFalse(
            employeeDAO.findOne(employee.getId()).isPresent()
        );
        Assert.assertFalse(
            employeeDAO.findByName("FirstName LastName").isPresent()
        );

        employeeRepository.saveAndFlush(Employee.builder()
            .firstName("FirstName")
            .lastName("LastName")
            .address("Somewhere")
            .company(null)
            .build()
        );
    }

    private void createEmployee() {
        employeeRepository.saveAndFlush(Employee.builder()
            .firstName("FirstName")
            .lastName("LastName")
            .address("Somewhere")
            .company(null)
            .build()
        );
    }

}
