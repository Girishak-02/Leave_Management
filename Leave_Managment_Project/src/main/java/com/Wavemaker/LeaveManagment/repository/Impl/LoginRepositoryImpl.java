package com.Wavemaker.LeaveManagment.repository.Impl;

import com.Wavemaker.LeaveManagment.model.Login;
import com.Wavemaker.LeaveManagment.repository.LoginRepository;
import com.Wavemaker.LeaveManagment.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRepositoryImpl implements LoginRepository {
    private static final String SELECT_BY_EMAIL = "SELECT * FROM LOGIN WHERE EMAIL = ?";

    private final Connection conn;

    public LoginRepositoryImpl() throws SQLException {
        this.conn = DBUtil.getConnection();
    }

    @Override
    public Login getLoginByEmail(String email) throws ClassNotFoundException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_BY_EMAIL)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Login(rs.getInt("Id"), rs.getString("EMAIL"), rs.getString("PASSWORD"));
            }
        } catch (SQLException e) {
            throw new ClassNotFoundException("Error retrieving login information", e);
        }
        return null;
    }
}
