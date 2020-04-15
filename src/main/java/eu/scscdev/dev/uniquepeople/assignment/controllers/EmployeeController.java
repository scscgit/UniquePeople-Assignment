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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ForkJoinPool;

@Log
@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("employees/{id}")
    public DeferredResult<ResponseEntity<Employee>> getEmployeeById(@PathVariable long id) {
        var result = new DeferredResult<ResponseEntity<Employee>>();
        ForkJoinPool.commonPool().submit(() -> {
            var employee = employeeService.findOne(id);
            if (!employee.isPresent()) {
                log.info("Employee not found");
                result.setResult(ResponseEntity.notFound().build());
            }
            log.info("Employee found: " + employee.toString());
            result.setResult(ResponseEntity.of(employee));
        });
        return result;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("employees")
    public DeferredResult<ResponseEntity<?>> generateEmployee(HttpServletRequest request) {
        var result = new DeferredResult<ResponseEntity<?>>();
        ForkJoinPool.commonPool().submit(() -> {
            var employee = employeeService.update(Employee.builder()
                .firstName(request.getParameter("firstName"))
                .lastName(request.getParameter("lastName"))
                .address(request.getParameter("address"))
                .build());
            log.info("Employee created: " + employee.toString());
            result.setResult(ResponseEntity.ok().build());
        });
        return result;
    }
}
