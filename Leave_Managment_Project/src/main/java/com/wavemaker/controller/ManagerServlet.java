package com.wavemaker.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wavemaker.model.Employee;
import com.wavemaker.model.Leave;
import com.wavemaker.model.Response;
import com.wavemaker.repository.Impl.LeaveRepositoryImpl;
import com.wavemaker.repository.LeaveRepository;
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

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String leaveIdParam = req.getParameter("leaveId");
//        String action = req.getParameter("status");
//
//        if (leaveIdParam == null || action == null) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing leaveId or status.");
//            return;
//        }
//
//        int leaveId;
//        try {
//            leaveId = Integer.parseInt(leaveIdParam);
//        } catch (NumberFormatException e) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid leaveId format.");
//            return;
//        }
//
//        String newStatus;
//        if ("approve".equalsIgnoreCase(action)) {
//            newStatus = "Approved";
//        } else if ("reject".equalsIgnoreCase(action)) {
//            newStatus = "Rejected";
//        } else {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action. Must be 'approve' or 'reject'.");
//            return;
//        }
//
//        boolean updated = leaveRepository.updateLeaveStatus(leaveId, newStatus);
//
//        if (updated) {
//            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.getWriter().write("Leave status updated successfully.");
//        } else {
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update leave status.");
//        }
//    }
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    HttpSession session = request.getSession(false);
//    if (session == null) {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write(gson.toJson("Unauthorized"));
//        return;
//    }

    try {

        System.out.println("Inside post method");
        int leaveId = Integer.parseInt(request.getParameter("leaveId"));
        String status = request.getParameter("status");

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
