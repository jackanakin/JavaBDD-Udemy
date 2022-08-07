package br.dev.kuhn.springboottesting.service;

import br.dev.kuhn.springboottesting.exception.ResourceNotFoundException;
import br.dev.kuhn.springboottesting.model.Employee;
import br.dev.kuhn.springboottesting.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        if (savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee already exist with given email:" + savedEmployee.get().getEmail());
        }

        return employeeRepository.save(employee);
    }
}
