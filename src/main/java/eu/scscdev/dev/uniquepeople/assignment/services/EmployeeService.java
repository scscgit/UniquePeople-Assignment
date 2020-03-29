package eu.scscdev.dev.uniquepeople.assignment.services;

import eu.scscdev.dev.uniquepeople.assignment.dao.EmployeeDAO;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class EmployeeService {

    @Delegate
    private final EmployeeDAO employeeDAO;
}
