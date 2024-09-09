package com.wavemaker.repository;

import com.wavemaker.model.Login;

public interface LoginRepository {
    Login getLoginByEmail(String email) throws ClassNotFoundException;
}
