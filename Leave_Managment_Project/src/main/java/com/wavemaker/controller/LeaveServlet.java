package com.wavemaker.controller;

import com.google.gson.JsonObject;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@WebServlet("/leaves")
public class LeaveServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LeaveServlet.class);
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            Integer empId = (Integer) session.getAttribute("loginId");
            if (empId != null) {
                List<Leave> leaves = leaveService.getLeavesByEmpId(empId);
                Map<String, Integer> leaveCount = leaveService.getLeaveCount(empId);

                Response response = new Response();
                response.setSuccess(true);
                response.setLeaves(leaves);
                response.setLeaveCount(leaveCount);

                resp.setContentType("application/json");
                PrintWriter out = resp.getWriter();
                out.print(gson.toJson(response));
                out.flush();
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Employee ID is required");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session is not valid");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();
        logger.info("Raw JSON payload: " + json);
        Leave leave = null;
        try {
            leave = gson.fromJson(json, Leave.class);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid JSON format.");
            logger.info("Error during JSON deserialization: " + e.getMessage());
            return;
        }
        if (leave != null) {
            logger.info("Deserialized Leave: " + leave);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Failed to deserialize Leave object.");
            return;
        }
        Integer loginId = (Integer) request.getSession(false).getAttribute("loginId");
        if (loginId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Employee ID is not available in the session.");
            return;
        }

        Employee employee = employeeService.getEmployeeByLoginId(loginId);
        if (employee == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("Employee not found.");
            return;
        }
        leave.setEmpId(employee.getEmpId());
        leave.setManagerId(employee.getManagerId());
        leave.setStatus("PENDING");

        try {
            leaveService.createLeave(leave);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("Leave added successfully");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error creating leave.");
            logger.info("Error creating leave: " + e.getMessage());
        }
    }

}
