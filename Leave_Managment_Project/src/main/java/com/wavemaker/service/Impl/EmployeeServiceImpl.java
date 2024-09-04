package com.wavemaker.service.Impl;


import com.wavemaker.service.EmployeeService;
import com.wavemaker.model.Employee;
import com.wavemaker.repository.EmployeeRepository;
import com.wavemaker.repository.Impl.EmployeeRepositoryImpl;

import java.sql.SQLException;


public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl() throws SQLException {

        this.employeeRepository = new EmployeeRepositoryImpl();
    }

    @Override
    public void create(Employee employee) {

        employeeRepository.create(employee);
    }

    @Override
    public Employee getEmployeeByLoginId(int loginId) {

        return employeeRepository.getEmployeeByLoginId(loginId);
    }

    @Override
    public void createEmployee(Employee employee) {

    }

    @Override
    public Employee getEmployee(int empId) {
        return null;
    }

    @Override
    public boolean updateEmployee(Employee employee) {

        employeeRepository.update(employee);
        return false;
    }

    @Override
    public boolean deleteEmployee(int empId) {
        employeeRepository.delete(empId);
        return false;
    }
}