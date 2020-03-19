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
    public void testEmployeeDaoOne() {
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
    }

    @Test
    public void testEmployeeDaoTwo() {
        createEmployee();
        createEmployee();
        List<Employee> employees = employeeDAO.findAll();
        Assert.assertEquals(2, employees.size());
        Employee employee = employees.get(0);
        employee.setFirstName("John");
        employee.setLastName("Cena");
        employee.setAddress("Slovakia");
        employeeDAO.update(employee);

        employee = employeeDAO.findOne(employee.getId()).get();
        Assert.assertEquals("John", employee.getFirstName());
        Assert.assertEquals("Cena", employee.getLastName());
        Assert.assertEquals("Slovakia", employee.getAddress());

        employeeDAO.delete(employee.getId());
        employees = employeeDAO.findAll();
        Assert.assertEquals(1, employees.size());
        Assert.assertNotEquals(employees.get(0).getId(), employee.getId());
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
