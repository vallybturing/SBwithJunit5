package com.csi.dao.impl;

import com.csi.dao.EmployeeDao;
import com.csi.exception.EmployeeNotFound;
import com.csi.model.Employee;
import com.csi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeDaoImpl implements EmployeeDao {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee saveData(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getDataById(int empId) {
        return employeeRepository.findById(empId).orElseThrow(() -> new EmployeeNotFound("Employee Not Found !!!!"));
    }

    @Override
    public List<Employee> getAllData() {
        return employeeRepository.findAll();
    }

    @Override
    public void deleteEmployeeById(int empId) {
        employeeRepository.deleteById(empId);
    }

    @Override
    public Page<Employee> getAllData(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public List<Employee> searchEmployees(String name) {
        return employeeRepository.findByEmpNameContainingIgnoreCase(name);
    }

    @Override
    public Page<Employee> searchEmployees(String name, Pageable pageable) {
        return employeeRepository.findByEmpNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public List<Employee> saveAll(List<Employee> employeeList) {
        return employeeRepository.saveAll(employeeList);
    }

    @Override
    public List<Employee> updateAll(List<Employee> employees) {
        // Ensure each employee exists before update
        for (Employee emp : employees) {
            if (!employeeRepository.existsById(emp.getEmpId())) {
                throw new RuntimeException("Employee not found with ID: " + emp.getEmpId());
            }
        }
        return employeeRepository.saveAll(employees);
    }

    @Override
    public void deleteAllByIds(List<Integer> empIds) {
        for (Integer empId : empIds) {
            if (!employeeRepository.existsById(empId)) {
                throw new RuntimeException("Employee not found with ID: " + empId);
            }
        }
        employeeRepository.deleteAllById(empIds);
    }
}