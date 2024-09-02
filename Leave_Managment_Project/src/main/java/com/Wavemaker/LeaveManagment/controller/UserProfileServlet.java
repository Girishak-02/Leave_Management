package com.Wavemaker.LeaveManagment.controller;

import com.Wavemaker.LeaveManagment.model.UserProfile;
import com.Wavemaker.LeaveManagment.util.DBUtil;
import com.google.gson.Gson;
import java.io.BufferedReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/userprofile")
public class UserProfileServlet extends HttpServlet {
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String empEmailParam = (String) session.getAttribute("email");
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        if (empEmailParam != null) {

            try (Connection conn = DBUtil.getConnection()) {
                String SELECT = " SELECT * FROM EMPLOYEES WHERE EMP_MAIL = ?;";
                try (PreparedStatement stmt = conn.prepareStatement(SELECT)) {
                    stmt.setString(1, empEmailParam);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        UserProfile userProfile = new UserProfile();
                        int empId = rs.getInt("EMP_ID");
                        System.out.println(empId);
                        userProfile.setEmpId(empId);
                        userProfile.setEmpName(rs.getString("EMP_NAME"));
                        userProfile.setEmpEmail(rs.getString("EMP_MAIL"));
//                        userProfile.setDob(rs.getDate("DOB"));
                        userProfile.setPhoneNumber(rs.getString("PHONE_NUMBER"));

                        out.println(gson.toJson(userProfile));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.println("{\"message\": \"User not found\"}");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("{\"message\": \"Error retrieving user profile\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"message\": \"User ID is required\"}");
        }
    }

    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line = null;
            reader.readLine();
            String jsonString = sb.toString();
            Gson gson = new Gson();
            UserProfile userProfile = gson.fromJson(jsonString, UserProfile.class);

            try (Connection conn = DBUtil.getConnection()) {
                String INSERT = "INSERT INTO EMPLOYEES (EMP_NAME, EMP_MAIL, PHONE_NUMBER) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(INSERT)) {
                    stmt.setString(1, userProfile.getEmpName());
                    stmt.setString(2, userProfile.getEmpEmail());
                    stmt.setString(3, userProfile.getPhoneNumber());
                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        response.setStatus(HttpServletResponse.SC_CREATED);
                        out.println("{\"message\": \"Profile added successfully\"}");
                    } else {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        out.println("{\"message\": \"Error adding profile\"}");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("{\"message\": \"Error adding profile: " + e.getMessage() + "\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"message\": \"Invalid profile data\"}");
        }
    }
}