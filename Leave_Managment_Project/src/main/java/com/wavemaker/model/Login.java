package com.wavemaker.model;

import com.wavemaker.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    private int id;
    private String email;
    private String password;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public Login(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int validate(String email, String password) throws ClassNotFoundException {
        boolean status = false;
        int loginId = -1;
        int empId = -1;
        String query = "SELECT * FROM LOGIN WHERE EMAIL = ? AND PASSWORD = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            System.out.println(email);
            System.out.println(password);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                loginId = rs.getInt("EMP_ID");
                return loginId;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginId;

    }
}