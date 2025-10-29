package com.csi.service.impl;

import com.csi.dao.EmployeeDao;
import com.csi.dto.EmployeeDTO;
import com.csi.exception.EmployeeNotFound;
import com.csi.model.Employee;
import com.csi.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    void getAllData() {
        List<Employee> employees = employeeService.getAllData();
        assertEquals(4, employees.size());
    }

    @Test
    void saveData() {
        EmployeeDTO employeeDTO = new EmployeeDTO("Chaitanya K", "Shri Lanka", "6354289752",
                "95679.68", new Date(2 - 10 - 1997), "chaitanya@gmail.com");
        Employee employee = employeeService.saveData(employeeDTO);
        Employee employeeData = employeeDao.getDataById(employee.getEmpId());
        assertEquals(employee, employeeData);
    }

    @Test
    void updateData() {
        //Update Data
        Employee employeeData = employeeDao.getDataById(4);
        EmployeeDTO employeeDTO = new EmployeeDTO("Chaitanya K", "Sri Lanka", "6354289752",
                "95679.68", new Date(2 - 10 - 1997), "chaitanyak@gmail.com");
        employeeService.updateData(employeeData.getEmpId(), employeeDTO);
        employeeData = employeeDao.getDataById(4);
        assertEquals(employeeDTO.getEmpEmail(), employeeData.getEmpEmail());
        assertEquals(employeeDTO.getEmpAddress(), employeeData.getEmpAddress());
    }

    @Test
    void getDataById() {
        Employee employee = employeeService.getDataById(1);
        assertEquals("Ram K", employee.getEmpName());
    }

    @Test
    void deleteEmployeeById() throws  Exception{
        employeeService.deleteEmployeeById(2);
        assertThrows(EmployeeNotFound.class, () -> employeeService.getDataById(2));
    }

    @Test
    void getAllDataWithPagination() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Employee> employees = employeeService.getAllData(pageable).getContent();
        assertEquals(2, employees.size());
    }

    @Test
    void searchEmployeeData() {
        List<Employee> employees = employeeService.searchEmployees("ram");
        assertEquals(1, employees.size());
    }

    @Test
    void searchEmployeeDataWithPagination() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Employee> employees = employeeService.searchEmployees("am", pageable);
        assertEquals(1, employees.getContent().size());
    }

    @Test
    void updateSaveDeleteAllTest() {

        //save
        List<EmployeeDTO> employeeDTO = new ArrayList<>();
        employeeDTO.add(new EmployeeDTO("Name 1", "United Stated Of America",
                "8523647982", "852365.62", new Date(13 - 11 - 2003), "akshay.k@gmail.com"));
        employeeDTO.add(new EmployeeDTO("Name 2", "United Stated Of America",
                "8523647982", "852365.62", new Date(13 - 11 - 2003), "akshay1.k@gmail.com"));

        List<Employee> result = employeeService.saveAll(employeeDTO);
        Employee employeeData = employeeDao.getDataById(result.get(0).getEmpId());
        assertEquals("Name 1", employeeData.getEmpName());

        //update
        result.get(0).setEmpName("John Kennedy");
        List<Employee> result_update = employeeService.updateAll(result);
        employeeData = employeeDao.getDataById(result_update.get(0).getEmpId());
        assertEquals("John Kennedy", employeeData.getEmpName());

        //delete
        List<Integer> empIds =  result.stream().map(Employee::getEmpId).toList();
        employeeService.deleteAllByIds(empIds);;
        assertThrows(EmployeeNotFound.class, () -> employeeService.getDataById(empIds.get(0)));

    }
}