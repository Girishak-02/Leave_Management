package com.wavemaker.controller;

import com.wavemaker.service.EmployeeService;
import com.wavemaker.service.Impl.EmployeeServiceImpl;
import com.wavemaker.service.Impl.LeaveServiceImpl;
import com.wavemaker.service.LeaveService;
import com.wavemaker.model.Employee;
import com.wavemaker.model.Leave;
import com.wavemaker.model.Response;
import com.wavemaker.util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/leaves")
public class LeaveServlet extends HttpServlet {
    private static Gson gson;
    private static EmployeeService employeeService;
    private final LeaveService leaveService = new LeaveServiceImpl();

    public LeaveServlet() throws SQLException {
    }
    @Override
    public void init() {
        try {
            employeeService = new EmployeeServiceImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            Integer empId = (Integer) session.getAttribute("loginId");
            if (empId != null) {
                List<Leave> leaves = leaveService.getLeavesByEmpId(empId);
                Response response = new Response();
                response.setSuccess(true);
                response.setLeaves(leaves);
                resp.setContentType("application/json");
                PrintWriter out = resp.getWriter();
                out.print(gson.toJson(response));
                out.flush();
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Employee ID is required");
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Integer loginId = (Integer) session.getAttribute("loginId");
            if (loginId == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("Employee ID is not available in the session.");
                return;
            }
            Leave leave = gson.fromJson(request.getReader(), Leave.class); // Use the correctly configured gson
            Employee employee = employeeService.getEmployeeByLoginId(loginId);
            if (employee != null) {
                leave.setEmpId(employee.getEmpId());
                leave.setManagerId(employee.getManagerId());
                leave.setStatus("PENDING");
            }
            leaveService.createLeave(leave);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("Leave added successfully");
        }
    }
}
