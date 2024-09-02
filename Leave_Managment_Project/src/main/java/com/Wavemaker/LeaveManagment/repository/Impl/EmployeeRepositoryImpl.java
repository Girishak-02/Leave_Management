package com.Wavemaker.LeaveManagment.repository.Impl;

import com.Wavemaker.LeaveManagment.model.Employee;
import com.Wavemaker.LeaveManagment.repository.EmployeeRepository;
import com.Wavemaker.LeaveManagment.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    public static final Logger logger = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);
    private static final String INSERT = "INSERT INTO EMPLOYEES (LOGIN_ID, EMP_NAME, EMP_MAIL, DOB, PHONE_NUMBER, MANAGER_ID) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT = "SELECT * FROM EMPLOYEES WHERE EMP_ID = ?";
    private static final String UPDATE = "UPDATE EMPLOYEES SET LOGIN_ID = ?, EMP_NAME = ?, EMP_MAIL = ?, DOB = ?, PHONE_NUMBER = ?, MANAGER_ID = ? WHERE EMP_ID = ?";
    private static final String DELETE = "DELETE FROM EMPLOYEES WHERE EMP_ID = ?";
    private static final String SELECT_BY_MANAGER = "SELECT * FROM EMPLOYEES WHERE MANAGER_ID = ?";

    private final Connection conn;

    public EmployeeRepositoryImpl() throws SQLException {
        this.conn = DBUtil.getConnection();
    }

    @Override
    public Employee read(int empId) {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpId(rs.getInt("EMP_ID"));
                employee.setLoginId(rs.getInt("LOGIN_ID"));
                employee.setEmpName(rs.getString("EMP_NAME"));
                employee.setEmpMail(rs.getString("EMP_MAIL"));
                employee.setDob(rs.getDate("DOB"));
                employee.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                employee.setManagerId(rs.getInt("MANAGER_ID"));
                logger.info("Employee read: {}", employee.getEmpName());
                return employee;
            }
        } catch (SQLException e) {
            logger.error("Error reading employee", e);
        }
        return null;
    }

    @Override
    public int create(Employee employee) {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, employee.getLoginId());
            stmt.setString(2, employee.getEmpName());
            stmt.setString(3, employee.getEmpMail());
            stmt.setDate(4, new java.sql.Date(employee.getDob().getTime()));
            stmt.setString(5, employee.getPhoneNumber());
            stmt.setInt(6, employee.getManagerId());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int empId = generatedKeys.getInt(1); // This gets the auto-generated EMP_ID
                logger.info("Employee created with EMP_ID: {}", empId);
                return empId;
            }
        } catch (SQLException e) {
            logger.error("Error creating employee", e);
        }
        return -1;
    }

    @Override
    public void update(Employee employee) {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            stmt.setInt(1, employee.getLoginId());
            stmt.setString(2, employee.getEmpName());
            stmt.setString(3, employee.getEmpMail());
            stmt.setDate(4, new java.sql.Date(employee.getDob().getTime()));
            stmt.setString(5, employee.getPhoneNumber());
            stmt.setInt(6, employee.getManagerId());
            stmt.setInt(7, employee.getEmpId());
            stmt.executeUpdate();
            logger.info("Employee updated: {}", employee.getEmpName());
        } catch (SQLException e) {
            logger.error("Error updating employee", e);
        }
    }

    @Override
    public void delete(int empId) {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, empId);
            stmt.executeUpdate();
            logger.info("Employee deleted: {}", empId);
        } catch (SQLException e) {
            logger.error("Error deleting employee", e);
        }
    }

    @Override
    public List<Employee> getEmployeesByManagerId(int managerId) {
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MANAGER)) {
            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpId(rs.getInt("EMP_ID"));
                employee.setLoginId(rs.getInt("LOGIN_ID"));
                employee.setEmpName(rs.getString("EMP_NAME"));
                employee.setEmpMail(rs.getString("EMP_MAIL"));
                employee.setDob(rs.getDate("DOB"));
                employee.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                employee.setManagerId(rs.getInt("MANAGER_ID"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            logger.error("Error getting employees by manager ID", e);
        }
        return employees;
    }

    @Override
    public Employee getEmployeeByMailAndManagerId(String managerMail, int i) {
        return null;
    }
}