package com.Wavemaker.LeaveManagment.Service.Impl;

import com.Wavemaker.LeaveManagment.model.Login;
import com.Wavemaker.LeaveManagment.repository.Impl.LoginRepositoryImpl;
import com.Wavemaker.LeaveManagment.repository.LoginRepository;
//import com.Wavemaker.LeaveManagment.Repository.Impl.LoginRepositoryImpl;
import com.Wavemaker.LeaveManagment.Service.LoginService;

import java.sql.SQLException;

public class LoginServiceImpl implements LoginService {
    private final LoginRepository loginRepository;

    public LoginServiceImpl() throws SQLException {
        this.loginRepository = new LoginRepositoryImpl(); // Assume the repository is correctly implemented
    }

    @Override
    public boolean validateLogin(String email, String password) throws ClassNotFoundException {
        Login login = loginRepository.getLoginByEmail(email);
        return login != null && login.getPassword().equals(password);
    }

    @Override
    public int getLoginId(String email) throws ClassNotFoundException {
        Login login = loginRepository.getLoginByEmail(email);
        return login != null ? login.getId() : -1;
    }
}
