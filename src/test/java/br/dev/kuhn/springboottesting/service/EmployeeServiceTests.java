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
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
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
    public void givenEmployeeId_whenDeleteEmployee_thenNothing () {
        //g
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        //w
        employeeService.deleteEmployee(employeeId);

        //t
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee () {
        //g
        given(employeeRepository.save(employee)).willReturn(employee);

        employee.setEmail("update@mail.com");
        employee.setFirstName("FirstName");

        //w
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //t
        assertThat(updatedEmployee.getEmail()).isEqualTo("update@mail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("FirstName");
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject () {
        //g
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        //w
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //t
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployee_thenReturnEmptyEmployeeList () {
        //g
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //w
        List<Employee> employeeList = employeeService.getAllEmployee();

        //t
        assertThat(employeeList.size()).isEqualTo(0);
    }

    @Test
    public void givenEmployeeList_whenGetAllEmployee_thenReturnEmployeeList () {
        //g
        Employee employee2 = Employee.builder().firstName("Jardel2").lastName("Kuhn2").email("mail2@mail.com").build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

        //w
        List<Employee> employeeList = employeeService.getAllEmployee();

        //t
        assertThat(employeeList.size()).isEqualTo(2);
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
