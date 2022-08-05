package br.dev.kuhn.springboottesting.repository;

import br.dev.kuhn.springboottesting.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList () {
        //g
        Employee employee1 = Employee.builder().firstName("Jardel").lastName("Kuhn").emailName("mail@mail.com").build();
        Employee employee2 = Employee.builder().firstName("Jardel").lastName("Kuhn").emailName("mail@mail.com").build();
        employeeRepository.save(employee1);employeeRepository.save(employee2);

        //w
        List<Employee> employeeList = employeeRepository.findAll();

        //t
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }



    @DisplayName("JUnit test for saving employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        //given - precondition
        Employee employee = Employee.builder().firstName("Jardel").lastName("Kuhn").emailName("mail@mail.com").build();

        //when - action or behaviour that we test
        Employee saved = employeeRepository.save(employee);

        //then - verify/test the output
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);
    }
}
