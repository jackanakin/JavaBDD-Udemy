package br.dev.kuhn.springboottesting.service;

import br.dev.kuhn.springboottesting.model.Employee;
import br.dev.kuhn.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder().firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build();
    }

    @Test
    public void givenEmployee_whenSaveEmployeeObject_thenReturnEmployeeObject () {
        //g
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //w
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //t
        assertThat(savedEmployee).isNotNull();
    }
}
