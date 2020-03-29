package eu.scscdev.dev.uniquepeople.assignment.controllers;

import eu.scscdev.dev.uniquepeople.assignment.db.Employee;
import eu.scscdev.dev.uniquepeople.assignment.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpServletRequest;

@Log
@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
        var employee = employeeService.findOne(id);
        if (!employee.isPresent()) {
            log.info("Employee not found");
            return ResponseEntity.notFound().build();
        }
        log.info("Employee found: " + employee.toString());
        return ResponseEntity.of(employee);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("employees")
    public ResponseEntity generateEmployee(HttpServletRequest request) {
        var employee = employeeService.update(Employee.builder()
            .firstName(request.getParameter("firstName"))
            .lastName(request.getParameter("lastName"))
            .address(request.getParameter("address"))
            .build());
        log.info("Employee created: " + employee.toString());
        return ResponseEntity.ok().build();
    }
}
