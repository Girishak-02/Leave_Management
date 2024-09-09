package com.wavemaker.service.Impl;

import com.wavemaker.service.LoginService;
import com.wavemaker.model.Login;
import com.wavemaker.repository.Impl.LoginRepositoryImpl;
import com.wavemaker.repository.LoginRepository;

import java.sql.SQLException;

public class LoginServiceImpl implements LoginService {
    private final LoginRepository loginRepository;

    public LoginServiceImpl() throws SQLException {
        this.loginRepository = new LoginRepositoryImpl();
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
