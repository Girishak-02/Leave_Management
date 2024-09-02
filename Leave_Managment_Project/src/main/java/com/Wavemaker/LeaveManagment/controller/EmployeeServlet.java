package com.Wavemaker.LeaveManagment.controller;

import com.Wavemaker.LeaveManagment.model.Employee;
import com.Wavemaker.LeaveManagment.repository.EmployeeRepository;
import com.Wavemaker.LeaveManagment.repository.Impl.EmployeeRepositoryImpl;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {
    private static final Gson gson = new Gson();
    private final EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    public EmployeeServlet() throws SQLException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String empIdParam = request.getParameter("empId");
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        if (empIdParam != null) {
            int empId = Integer.parseInt(empIdParam);
            Employee employee = employeeRepository.read(empId);
            if (employee != null) {
                out.println(gson.toJson(employee));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("{\"message\": \"Employee not found\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"message\": \"Employee ID is required\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String empName = request.getParameter("empName");
        String empMail = request.getParameter("empMail");
        String dobParam = request.getParameter("dob");
        String phoneNumber = request.getParameter("phoneNumber");
        String managerIdParam = request.getParameter("managerId");

        if (empName != null && empMail != null && dobParam != null && phoneNumber != null && managerIdParam != null) {

            java.sql.Date dob = java.sql.Date.valueOf(dobParam);
            int managerId = Integer.parseInt(managerIdParam);

            Employee employee = new Employee();
            employee.setEmpName(empName);
            employee.setEmpMail(empMail);
            employee.setDob(dob);
            employee.setPhoneNumber(phoneNumber);
            employee.setManagerId(managerId);

            employeeRepository.create(employee);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("{\"message\": \"Employee created successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"message\": \"All fields are required\"}");
        }
    }

@Override
protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String empIdParam = request.getParameter("empId");
    String loginIdParam = request.getParameter("loginId");
    String empName = request.getParameter("empName");
    String empMail = request.getParameter("empMail");
    String dobParam = request.getParameter("dob");
    String phoneNumber = request.getParameter("phoneNumber");
    String managerIdParam = request.getParameter("managerId");

    if (empIdParam != null && loginIdParam != null && empName != null && empMail != null && dobParam != null && phoneNumber != null && managerIdParam != null) {
        int empId = Integer.parseInt(empIdParam);
        int loginId = Integer.parseInt(loginIdParam);
        java.sql.Date dob = java.sql.Date.valueOf(dobParam);
        int managerId = Integer.parseInt(managerIdParam);

        Employee employee = new Employee();
        employee.setEmpId(empId);
        employee.setLoginId(loginId);
        employee.setEmpName(empName);
        employee.setEmpMail(empMail);
        employee.setDob(dob);
        employee.setPhoneNumber(phoneNumber);
        employee.setManagerId(managerId);

        employeeRepository.update(employee);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{\"message\": \"Employee updated successfully\"}");
    } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("{\"message\": \"All fields are required\"}");
    }
}

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String empIdParam = request.getParameter("empId");

        if (empIdParam != null) {
            int empId = Integer.parseInt(empIdParam);
            employeeRepository.delete(empId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("{\"message\": \"Employee deleted successfully\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"message\": \"Employee ID is required\"}");
        }
    }
}

