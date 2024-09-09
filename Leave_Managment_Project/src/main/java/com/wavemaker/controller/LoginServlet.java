package com.wavemaker.controller;

import com.wavemaker.model.Login;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            Login login = new Login(email, password);
            int loginId = login.validate(email, password);
            if (loginId != -1) {
                session.setAttribute("loginId", loginId);
                response.sendRedirect("Dashboard.html");
            } else {
                System.out.println("Login failed");
                response.sendRedirect("LoginPage.html");
            }
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
            response.sendRedirect("LoginPage.html");
            System.out.println("Login failed");
        }
    }

}