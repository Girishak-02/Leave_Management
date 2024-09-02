package com.Wavemaker.LeaveManagment.repository;

import com.Wavemaker.LeaveManagment.model.Login;

public interface LoginRepository {
    Login getLoginByEmail(String email) throws ClassNotFoundException;
}
