package com.wavemaker.service;


import com.wavemaker.model.Employee;


public interface EmployeeService {
    void createEmployee(Employee employee);

    Employee getEmployee(int empId);

    boolean updateEmployee(Employee employee);

    boolean deleteEmployee(int empId);

    void create(Employee employee);

    Employee getEmployeeByLoginId(int i);
}