package br.dev.kuhn.springboottesting.controller;

import br.dev.kuhn.springboottesting.model.Employee;
import br.dev.kuhn.springboottesting.service.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeServiceImpl employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        //g
        Long employeeId = 1L;
        //Employee savedEmployee = Employee.builder().id(employeeId).firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build();
        Employee updatedEmployee = Employee.builder().id(employeeId).firstName("Jardel2").lastName("Kuhn2").email("mail2@mail.com").build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        //given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

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
        Long employeeId = 1L;
        Employee savedEmployee = Employee.builder().id(employeeId).firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build();
        Employee updatedEmployee = Employee.builder().id(employeeId).firstName("Jardel2").lastName("Kuhn2").email("mail2@mail.com").build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

        //w
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
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
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //w
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //t
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //g
        Long employeeId = 1L;
        Employee employee = Employee.builder().id(employeeId).firstName("Jardel").lastName("Kuhn").email("mail@mail.com").build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //w
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

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
        given(employeeService.getAllEmployee()).willReturn(employeeList);

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
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocationOnMock) -> invocationOnMock.getArgument(0));

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
