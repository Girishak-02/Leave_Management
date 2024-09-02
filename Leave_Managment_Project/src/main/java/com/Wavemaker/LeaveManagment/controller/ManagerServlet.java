package com.Wavemaker.LeaveManagment.controller;

import com.Wavemaker.LeaveManagment.model.Leave;
import com.Wavemaker.LeaveManagment.model.Response;
import com.Wavemaker.LeaveManagment.repository.Impl.LeaveRepositoryImpl;
import com.Wavemaker.LeaveManagment.repository.LeaveRepository;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/manager/leaves")
public class ManagerServlet extends HttpServlet {
    private LeaveRepository leaveRepository;
    @Override
    public void init() throws ServletException {
        leaveRepository = new LeaveRepositoryImpl();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String managerIdParam = request.getParameter("managerId");
        if (managerIdParam != null) {
            int managerId = Integer.parseInt(managerIdParam);
            List<Leave> teamLeaves = leaveRepository.findByManagerId(managerId);
            Gson gson = new Gson();
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
    }
