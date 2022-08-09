package br.dev.kuhn.springboottesting.repository;

import br.dev.kuhn.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder().firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build();
    }

    @Test
    public void givenFirstAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployee () {
        //g
        employeeRepository.save(employee);
        String firstName = "Jardel";
        String lastName = "Kuhn";

        //w
        Employee employeeSaved = employeeRepository.findByNativeSQLNamedParams(firstName, lastName);

        //t
        assertThat(employeeSaved).isNotNull();
    }

    @Test
    public void givenFirstAndLastName_whenFindByNativeSQL_thenReturnEmployee () {
        //g
        employeeRepository.save(employee);
        String firstName = "Jardel";
        String lastName = "Kuhn";

        //w
        Employee employeeSaved = employeeRepository.findByNativeSQL(firstName, lastName);

        //t
        assertThat(employeeSaved).isNotNull();
    }

    @Test
    public void givenFirstAndLastName_whenFindByJPQLNamedParams_thenReturnEmployee () {
        //g
        employeeRepository.save(employee);
        String firstName = "Jardel";
        String lastName = "Kuhn";

        //w
        Employee employeeSaved = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        //t
        assertThat(employeeSaved).isNotNull();
    }

    @Test
    public void givenFirstAndLastName_whenFindByJPQL_thenReturnEmployee () {
        //g
        employeeRepository.save(employee);
        String firstName = "Jardel";
        String lastName = "Kuhn";

        //w
        Employee employeeSaved = employeeRepository.findByJPQL(firstName, lastName);

        //t
        assertThat(employeeSaved).isNotNull();
    }

    @Test
    public void givenEmployee_whenDelete_thenRemove () {
        //g
        employeeRepository.save(employee);

        //w
        employeeRepository.delete(employee);
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail("mail@mail.com");

        //t
        assertThat(optionalEmployee.isPresent()).isFalse();
    }

    @Test
    public void givenEmployee_whenUpdated_thenReturnUpdated () {
        //g
        employeeRepository.save(employee);

        //w
        Employee employeeSaved = employeeRepository.findById(employee.getId()).get();
        employeeSaved.setEmail("other@mail.com");
        employeeSaved.setFirstName("FirstName");
        employeeSaved.setLastName("LastName");
        Employee employeeUpdated = employeeRepository.save(employeeSaved);

        //t
        assertThat(employeeUpdated.getEmail()).isEqualTo("other@mail.com");
        assertThat(employeeUpdated.getFirstName()).isEqualTo("FirstName");
        assertThat(employeeUpdated.getLastName()).isEqualTo("LastName");
    }

    @Test
    public void givenEmployeeName_whenFindByEmail_thenReturnEmployee () {
        //g
        employeeRepository.save(employee);

        //w
        Employee employeeSaved = employeeRepository.findByEmail("mail@mail.com").get();

        //t
        assertThat(employeeSaved).isNotNull();
    }

    @Test
    public void givenEmployee_whenFindById_thenReturnEmployee () {
        //g
        employeeRepository.save(employee);

        //w
        Employee employeeSaved = employeeRepository.findById(employee.getId()).get();

        //t
        assertThat(employeeSaved).isNotNull();
        assertThat(employeeSaved.getId()).isGreaterThan(0);
    }

    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList () {
        //g
        Employee employee1 = Employee.builder().firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build();
        Employee employee2 = Employee.builder().firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build();
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

        //when - action or behaviour that we test
        Employee saved = employeeRepository.save(employee);

        //then - verify/test the output
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);
    }
}
