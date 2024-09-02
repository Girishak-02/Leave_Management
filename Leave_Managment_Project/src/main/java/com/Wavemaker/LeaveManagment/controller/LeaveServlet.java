package com.Wavemaker.LeaveManagment.controller;

import com.Wavemaker.LeaveManagment.model.Response;
import com.Wavemaker.LeaveManagment.model.Leave;
import com.Wavemaker.LeaveManagment.repository.LeaveRepository;
import com.Wavemaker.LeaveManagment.repository.Impl.LeaveRepositoryImpl;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/leaves")
public class LeaveServlet extends HttpServlet {
    private final LeaveRepository leaveRepository = new LeaveRepositoryImpl();
    private final Gson gson = new Gson(); // Gson for JSON

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empIdParam = req.getParameter("empId");
        if (empIdParam != null) {
            int empId = Integer.parseInt(empIdParam);
            List<Leave> leaves = leaveRepository.findByEmpId(empId);
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
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Leave leave = gson.fromJson(req.getReader(), Leave.class);
        leaveRepository.create(leave);
        Response response = new Response();
        response.setSuccess(true);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(response));
        out.flush();
    }


  }
