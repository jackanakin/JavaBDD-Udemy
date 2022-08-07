package br.dev.kuhn.springboottesting.service;

import br.dev.kuhn.springboottesting.exception.ResourceNotFoundException;
import br.dev.kuhn.springboottesting.model.Employee;
import br.dev.kuhn.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
    public void givenExistingEmail_whenSaveEmployeeObject_thenThrowsException () {
        //g
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        //given(employeeRepository.save(employee)).willReturn(employee);

        //w
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //t
        verify(employeeRepository, never()).save(any(Employee.class));
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
