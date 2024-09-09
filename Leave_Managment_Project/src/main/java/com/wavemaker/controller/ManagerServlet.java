package com.wavemaker.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wavemaker.model.Leave;
import com.wavemaker.model.Response;
import com.wavemaker.repository.Impl.LeaveRepositoryImpl;
import com.wavemaker.repository.LeaveRepository;
import com.wavemaker.util.LocalDateAdapter;
import org.json.JSONObject;
//import org.json.JSONObject;

import javax.servlet.ServletException;
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

@WebServlet("/manager/leaves")
public class ManagerServlet extends HttpServlet {
    private static Gson gson;
    private LeaveRepository leaveRepository;

    @Override
    public void init() throws ServletException {
        try {
            leaveRepository = new LeaveRepositoryImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer managerId = (Integer) session.getAttribute("loginId");
        if (managerId != null) {

            List<Leave> teamLeaves = leaveRepository.findByManagerId(managerId);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            Response resp = new Response();
            resp.setSuccess(true);
            resp.setLeaves(teamLeaves);

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(resp));
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Manager ID is required");
        }
    }
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {

        System.out.println(request.getParameter("leaveId"));
        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String jsonString = jsonBuilder.toString();

        // Parse JSON data
        JSONObject jsonObject = new JSONObject(jsonString);
        int leaveId =jsonObject.getInt("leaveId");
        String status = jsonObject.getString("status");



        if (!status.equalsIgnoreCase("Approved") && !status.equalsIgnoreCase("Rejected")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("Invalid status value"));
            return;
        }
        boolean updated = leaveRepository.updateLeaveStatus(leaveId, status);
        System.out.println(updated);
        response.setContentType("application/json");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
