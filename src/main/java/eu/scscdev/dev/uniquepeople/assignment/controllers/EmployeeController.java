package eu.scscdev.dev.uniquepeople.assignment.controllers;

import eu.scscdev.dev.uniquepeople.assignment.dao.EmployeeDAO;
import eu.scscdev.dev.uniquepeople.assignment.db.Employee;
import eu.scscdev.dev.uniquepeople.assignment.repository.EmployeeRepository;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Log
@Controller
public class EmployeeController {

    @Inject
    private EmployeeDAO employeeDAO;
    @Inject
    private EmployeeRepository employeeRepository;

    @GetMapping("employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
        Optional<Employee> employee = employeeDAO.findOne(id);
        if (!employee.isPresent()) {
            log.info("Employee not found");
            return ResponseEntity.notFound().build();
        }
        log.info("Employee found: " + employee.toString());
        return ResponseEntity.of(employee);
    }

    @Secured("ADMIN")
    @PutMapping("employees")
    public ResponseEntity generateEmployee(HttpServletRequest request) {
        Employee employee = employeeRepository.saveAndFlush(Employee.builder()
            .firstName(request.getParameter("firstName"))
            .lastName(request.getParameter("lastName"))
            .address(request.getParameter("address"))
            .build());
        log.info("Employee created: " + employee.toString());
        return ResponseEntity.ok().build();
    }
}
