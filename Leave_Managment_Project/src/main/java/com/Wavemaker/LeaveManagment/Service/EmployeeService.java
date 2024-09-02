package com.Wavemaker.LeaveManagment.Service;


import com.Wavemaker.LeaveManagment.model.Employee;


public interface EmployeeService {
    void createEmployee(Employee employee);
    Employee getEmployee(int empId);
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(int empId);

    void create(Employee employee);

    Employee getEmployeeById(int i);
}