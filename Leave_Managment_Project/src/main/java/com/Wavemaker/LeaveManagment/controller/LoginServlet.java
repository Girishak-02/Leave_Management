package com.Wavemaker.LeaveManagment.controller;

import com.Wavemaker.LeaveManagment.model.Login;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Login login = new Login(email, password);

        try {
            if (login.validate(email,password)) {

                //HttpSession session = request.getSession();
                session.setAttribute("email", email);
                response.sendRedirect("Dashboard.html");
            } else {
                System.out.println("failed");
                response.sendRedirect("LoginPage.html");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("LoginPage.html");
        }

    }
}
