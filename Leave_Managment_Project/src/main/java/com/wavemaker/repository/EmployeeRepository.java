package com.wavemaker.repository;

import com.wavemaker.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    Employee read(int empId);

    int create(Employee employee);

    void update(Employee employee);

    void delete(int empId);

    List<Employee> getEmployeesByManagerId(int managerId);

    Employee getEmployeeByMailAndManagerId(String managerMail, int i);

    Employee getEmployeeByLoginId(int loginId);
}