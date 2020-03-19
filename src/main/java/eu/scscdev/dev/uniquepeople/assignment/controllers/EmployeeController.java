package eu.scscdev.dev.uniquepeople.assignment.controllers;

import eu.scscdev.dev.uniquepeople.assignment.dao.EmployeeDAO;
import eu.scscdev.dev.uniquepeople.assignment.db.Employee;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.inject.Inject;
import java.util.Optional;

//@Log
public class EmployeeController {

    // TODO: Let's try the Lombok instead
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Inject
    private EmployeeDAO employeeDAO;

    @GetMapping("employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
        Optional<Employee> employee = employeeDAO.findOne(id);
        if (!employee.isPresent()) {
            logger.info("Employee not found");
            return ResponseEntity.notFound().build();
        }
        logger.info("Employee found");
        return ResponseEntity.of(employee);
    }
}
