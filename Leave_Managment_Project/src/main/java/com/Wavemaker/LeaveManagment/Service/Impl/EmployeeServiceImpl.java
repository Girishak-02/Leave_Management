package com.Wavemaker.LeaveManagment.Service.Impl;


import com.Wavemaker.LeaveManagment.model.Employee;
import com.Wavemaker.LeaveManagment.repository.EmployeeRepository;
import com.Wavemaker.LeaveManagment.Service.EmployeeService;


public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    @Override
    public void create(Employee employee) {

        employeeRepository.create(employee);
    }

    @Override
    public Employee getEmployeeById(int empId) {

        return employeeRepository.read(empId);
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