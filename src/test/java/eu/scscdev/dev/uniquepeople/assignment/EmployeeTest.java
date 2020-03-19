package eu.scscdev.dev.uniquepeople.assignment;

import eu.scscdev.dev.uniquepeople.assignment.dao.EmployeeDAO;
import eu.scscdev.dev.uniquepeople.assignment.db.Company;
import eu.scscdev.dev.uniquepeople.assignment.db.Employee;
import eu.scscdev.dev.uniquepeople.assignment.repository.CompanyRepository;
import eu.scscdev.dev.uniquepeople.assignment.repository.EmployeeRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Consumer;

@SpringBootTest
public class EmployeeTest {

    @Inject
    private EmployeeRepository employeeRepository;
    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private EmployeeDAO employeeDAO;

    /**
     * Use @employeeRepository and @companyRepository to test the first half of methods of @employeeDAO.
     */
    @Test
    public void testEmployeeDaoOne() {
        createEmployee(employee -> employeeRepository.saveAndFlush(employee));
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

    /**
     * Use @employeeRepository and @companyRepository to test the second half of methods of @employeeDAO.
     */
    @Test
    public void testEmployeeDaoTwo() {
        // Create two employees
        createEmployee(employee -> employeeRepository.saveAndFlush(employee));
        // Also test the EmployeeDAO's update to work as save
        createEmployee(employee -> employeeDAO.update(employee));
        List<Employee> employees = employeeDAO.findAll();
        Assert.assertEquals(2, employees.size());

        // Verify the first employee before a modification
        Employee employee = employees.get(0);
        Assert.assertEquals("FirstName", employee.getFirstName());
        Assert.assertEquals("LastName", employee.getLastName());
        Assert.assertEquals("Somewhere", employee.getAddress());
        Assert.assertNotNull(employee.getCompany());

        // Modify the employee
        employee.setFirstName("John");
        employee.setLastName("Cena");
        employee.setAddress("Slovakia");
        employee.setCompany(null);
        Employee updatedEmployee = employeeDAO.update(employee);

        // Verify the employee after a modification
        employee = employeeDAO.findOne(employee.getId()).get();
        Assert.assertEquals("John", employee.getFirstName());
        Assert.assertEquals("Cena", employee.getLastName());
        Assert.assertEquals("Slovakia", employee.getAddress());
        Assert.assertNull(employee.getCompany());

        // Verify the returned reference of the update call:
        Assert.assertEquals(employee.getId(), updatedEmployee.getId());
        Assert.assertEquals("John", updatedEmployee.getFirstName());
        Assert.assertEquals("Cena", updatedEmployee.getLastName());
        Assert.assertEquals("Slovakia", updatedEmployee.getAddress());
        Assert.assertNull(updatedEmployee.getCompany());

        // Delete the employee
        employeeDAO.delete(employee.getId());
        employees = employeeDAO.findAll();
        Assert.assertEquals(1, employees.size());

        // Make sure the remaining employee doesn't share the same ID with the deleted one
        Assert.assertNotEquals(employees.get(0).getId(), employee.getId());
    }

    private void createEmployee(Consumer<Employee> function) {
        function.accept(Employee.builder()
            .firstName("FirstName")
            .lastName("LastName")
            .address("Somewhere")
            .company(companyRepository.saveAndFlush(Company.builder().build()))
            .build()
        );
    }
}
