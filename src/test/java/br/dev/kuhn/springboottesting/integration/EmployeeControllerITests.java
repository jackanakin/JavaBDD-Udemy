package br.dev.kuhn.springboottesting.integration;

import br.dev.kuhn.springboottesting.model.Employee;
import br.dev.kuhn.springboottesting.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200 () throws Exception {
        //g
        Employee savedEmployee = Employee.builder().firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build();
        employeeRepository.save(savedEmployee);

        //w
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        //t
        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        //g
        Long employeeId = 1L;
        Employee updatedEmployee = Employee.builder().id(employeeId).firstName("Jardel2").lastName("Kuhn2").email("mail2@mail.com").build();

        //w
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //t
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //g
        Employee savedEmployee = Employee.builder().firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder().firstName("Jardel2").lastName("Kuhn2").email("mail2@mail.com").build();

        //w
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //t
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())))
                .andDo(print());
    }

    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //g
        Long employeeId = 1L;

        //w
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //t
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //g
        Employee employee = Employee.builder().firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build();
        employeeRepository.save(employee);

        //w
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        //t
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andDo(print());
    }

    @Test
    public void givenListOffEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //g
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build());
        employeeList.add(Employee.builder().firstName("Jardel2").lastName("Kuhn2").email("mail2@mail.com").build());
        employeeRepository.saveAll(employeeList);

        //w
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //t
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employeeList.size())))
                .andDo(print());
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        Employee employee = Employee.builder().firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build();

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andDo(print());
    }

}
