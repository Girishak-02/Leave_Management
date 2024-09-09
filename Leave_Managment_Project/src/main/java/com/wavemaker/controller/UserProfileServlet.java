package com.wavemaker.controller;

import com.wavemaker.model.UserProfile;
import com.wavemaker.util.DBUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
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
        Integer empLoginIdParam = (Integer) session.getAttribute("loginId");
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        if (empLoginIdParam != null) {

            try (Connection conn = DBUtil.getConnection()) {
                String SELECT = " SELECT * FROM EMPLOYEES WHERE EMP_ID = ?;";
                try (PreparedStatement stmt = conn.prepareStatement(SELECT)) {
                    stmt.setInt(1, empLoginIdParam);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        UserProfile userProfile = new UserProfile();
                        userProfile.setEmpId(empLoginIdParam);
                        userProfile.setEmpName(rs.getString("EMP_NAME"));
                        userProfile.setEmpEmail(rs.getString("EMP_EMAIL"));
                        userProfile.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                        userProfile.setDob(String.valueOf(rs.getDate("DOB")));
                        userProfile.setManagerId(rs.getInt("MANAGER_ID"));
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
            out.println("{\"message\": \"Emp ID is required\"}");
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
                String INSERT = "INSERT INTO EMPLOYEES (EMP_ID,EMP_NAME, EMP_EMAIL, PHONE_NUMBER) VALUES (?,?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(INSERT)) {
                    stmt.setInt(1, userProfile.getEmpId());
                    stmt.setString(2, userProfile.getEmpName());
                    stmt.setString(3, userProfile.getEmpEmail());
                    stmt.setString(4, userProfile.getPhoneNumber());
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