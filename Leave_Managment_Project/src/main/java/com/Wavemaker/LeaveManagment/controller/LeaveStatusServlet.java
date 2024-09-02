package com.Wavemaker.LeaveManagment.controller;


import com.Wavemaker.LeaveManagment.model.Response;
import com.Wavemaker.LeaveManagment.repository.Impl.LeaveRepositoryImpl;
import com.Wavemaker.LeaveManagment.repository.LeaveRepository;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/leave-status")
public class LeaveStatusServlet extends HttpServlet {
    private final LeaveRepository leaveRepository = new LeaveRepositoryImpl();
    private final Gson gson = new Gson(); // Gson for JSON

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IOException {
        LeaveStatusChangeRequest statusChangeRequest = gson.fromJson(req.getReader(), LeaveStatusChangeRequest.class);

        int leaveId = statusChangeRequest.getLeaveId();
        String action = statusChangeRequest.getAction();

        String newStatus;
        if ("approve".equalsIgnoreCase(action)) {
            newStatus = "Approved";
        } else if ("reject".equalsIgnoreCase(action)) {
            newStatus = "Rejected";
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            return;
        }

        boolean updated = leaveRepository.updateLeaveStatus(leaveId, newStatus);

        Response response = new Response();
        if (updated) {
            response.setSuccess(true);
            response.setNewStatus(newStatus);
        } else {
            response.setSuccess(false);
        }

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(response));
        out.flush();
    }

    private static class LeaveStatusChangeRequest {
        private int leaveId;
        private String action;

        public int getLeaveId() {
            return leaveId;
        }

        public void setLeaveId(int leaveId) {
            this.leaveId = leaveId;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }
}
