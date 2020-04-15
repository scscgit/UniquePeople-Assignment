package eu.scscdev.dev.uniquepeople.assignment;

import eu.scscdev.dev.uniquepeople.assignment.dao.EmployeeDAO;
import eu.scscdev.dev.uniquepeople.assignment.db.Company;
import eu.scscdev.dev.uniquepeople.assignment.db.Employee;
import eu.scscdev.dev.uniquepeople.assignment.repository.CompanyRepository;
import eu.scscdev.dev.uniquepeople.assignment.repository.EmployeeRepository;
import lombok.var;
import org.junit.Assert;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
// Disabling Rollback due to TransactionTemplate's nested transactions, which are not possible to be rolled back
@Rollback(false)
public class EmployeeTest {

    @Inject
    private EmployeeRepository employeeRepository;
    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private EmployeeDAO employeeDAO;
    @Inject
    private TransactionTemplate transactionTemplate;

    /**
     * Use @employeeRepository and @companyRepository to test the first half of methods of @employeeDAO.
     */
    @Test
    @Order(1)
    public void testEmployeeDaoOne() {
        createEmployeeInNewTransaction(employeeRepository::save, this::createCompany);
        var employees = employeeDAO.findAll();
        Assert.assertEquals(1, employees.size());
        var employee = employeeRepository.findAll().get(0);
        Assert.assertEquals(employees.get(0).getId(), employee.getId());

        // Assert bi-directional association (needs to be explicitly set if not done via createEmployeeInNewTransaction)
        Assert.assertEquals(employee.getId(), employee.getCompany().getEmployees().get(0).getId());

        employeeDAO.findOne(employee.getId());
        Assert.assertTrue(
            employeeDAO.findOne(employee.getId()).isPresent()
        );
        Assert.assertTrue(
            employeeDAO.findByName("FirstName LastName").isPresent()
        );
        Assert.assertTrue(
            employeeRepository.findByName("FirstName LastName").isPresent()
        );

        employeeDAO.delete(employee.getId());
        Assert.assertFalse(
            employeeDAO.findOne(employee.getId()).isPresent()
        );
        Assert.assertFalse(
            employeeDAO.findByName("FirstName LastName").isPresent()
        );
        Assert.assertFalse(
            employeeRepository.findByName("FirstName LastName").isPresent()
        );
    }

    /**
     * Use @employeeRepository and @companyRepository to test the second half of methods of @employeeDAO.
     */
    @Test
    @Order(2)
    public void testEmployeeDaoTwo() {
        // Create two employees
        createEmployeeInNewTransaction(employee -> employeeRepository.save(employee), this::createCompany);
        // Also test the EmployeeDAO's update to work as save
        createEmployeeInNewTransaction(employee -> employeeDAO.update(employee), this::createCompany);
        var employees = employeeDAO.findAll();
        Assert.assertEquals(2, employees.size());

        // Verify the first employee before a modification
        var employee = employees.get(0);
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
        long deleted = employeeDAO.delete(employee.getId());
        Assert.assertEquals(1L, deleted);
        employees = employeeDAO.findAll();
        Assert.assertEquals(1, employees.size());
        // Try to delete it again, which should safely fail
        deleted = employeeDAO.delete(employee.getId());
        Assert.assertEquals(0L, deleted);

        // Make sure the remaining employee doesn't share the same ID with the deleted one
        Assert.assertNotEquals(employees.get(0).getId(), employee.getId());

        // Delete the other employee using EmployeeRepository
        employee = employees.get(0);
        deleted = employeeRepository.removeById(employee.getId());
        Assert.assertEquals(1L, deleted);
        employees = employeeRepository.findAll();
        Assert.assertEquals(0, employees.size());
        // Try to delete it again, which should safely fail
        deleted = employeeRepository.removeById(employee.getId());
        Assert.assertEquals(0L, deleted);
    }

    @Test
    @Order(3)
    public void duplicateFirstEmployeeUsingWith() {
        // Prepare one employee
        createEmployeeInNewTransaction(employee -> employeeRepository.save(employee), this::createCompany);
        Assert.assertEquals(1, employeeDAO.findAll().size());

        // Replicate it twice using Lombok's extension method of With, expecting the instance to stay immutable
        var oneWithoutId = employeeDAO.findAll().get(0).withId(null);
        employeeDAO.update(oneWithoutId);
        employeeDAO.update(oneWithoutId);
        Assert.assertEquals(3, employeeDAO.findAll().size());
    }

    /**
     * Creates and persists a new Employee with a Company, implicitly making the associations bi-directional.
     */
    public void createEmployeeInNewTransaction(Consumer<Employee> saveFunction, Supplier<Company> createCompany) {
        // We would require a self-autowired call in order to support @Transactional without TransactionTemplate
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionTemplate.executeWithoutResult(
            status -> saveFunction.accept(Employee.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .address("Somewhere")
                .company(createCompany.get())
                .build()
            )
        );
    }

    private Company createCompany() {
        return companyRepository.save(Company.builder().build());
    }
}
