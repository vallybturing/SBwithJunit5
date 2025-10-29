package com.csi.service.impl;

import com.csi.dao.EmployeeDao;
import com.csi.dto.EmployeeDTO;
import com.csi.model.Employee;
import com.csi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public Employee saveData(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmpName(employeeDTO.getEmpName());
        employee.setEmpAddress(employeeDTO.getEmpAddress());
        employee.setEmpContactNumber(Long.parseLong(employeeDTO.getEmpContactNumber()));
        employee.setEmpSalary(Double.parseDouble(employeeDTO.getEmpSalary()));
        employee.setEmpDOB(employeeDTO.getEmpDOB());
        employee.setEmpEmail(employeeDTO.getEmpEmail());
        return employeeDao.saveData(employee);
    }

    @Override
    public Employee updateData(int empId, EmployeeDTO employeeDTO) {
        Employee employee = employeeDao.getDataById(empId);
        employee.setEmpName(employeeDTO.getEmpName());
        employee.setEmpAddress(employeeDTO.getEmpAddress());
        employee.setEmpContactNumber(Long.parseLong(employeeDTO.getEmpContactNumber()));
        employee.setEmpSalary(Double.parseDouble(employeeDTO.getEmpSalary()));
        employee.setEmpDOB(employeeDTO.getEmpDOB());
        employee.setEmpEmail(employeeDTO.getEmpEmail());
        return employeeDao.saveData(employee);
    }

    @Override
    public Employee getDataById(int empId) {
        return employeeDao.getDataById(empId);
    }

    @Override
    public List<Employee> getAllData() {
        return employeeDao.getAllData();
    }

    @Override
    public void deleteEmployeeById(int empId) {
        employeeDao.deleteEmployeeById(empId);
    }

    @Override
    public Page<Employee> getAllData(Pageable pageable) {

       return employeeDao.getAllData(pageable);

    }

    @Override
    public List<Employee> searchEmployees(String name) {
        return employeeDao.searchEmployees(name);
    }

    @Override
    public Page<Employee> searchEmployees(String name, Pageable pageable) {
        return employeeDao.searchEmployees(name, pageable);
    }

    @Override
    public List<Employee> saveAll(List<EmployeeDTO> employees) {
        List<Employee> employeeList = new ArrayList<>();
        for (EmployeeDTO emp : employees) {
            Employee employee = new Employee();
            employee.setEmpName(emp.getEmpName());
            employee.setEmpAddress(emp.getEmpAddress());
            employee.setEmpContactNumber(Long.parseLong(emp.getEmpContactNumber()));
            employee.setEmpSalary(Double.parseDouble(emp.getEmpSalary()));
            employee.setEmpDOB(emp.getEmpDOB());
            employee.setEmpEmail(emp.getEmpEmail());
            employeeList.add(employee);
        }
        return employeeDao.saveAll(employeeList);
    }

    @Override
    public List<Employee> updateAll(List<Employee> employees) {
        return employeeDao.updateAll(employees);
    }

    @Override
    public void deleteAllByIds(List<Integer> empIds) {
        employeeDao.deleteAllByIds(empIds);
    }

}