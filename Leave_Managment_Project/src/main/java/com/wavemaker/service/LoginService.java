package com.wavemaker.service;

public interface LoginService {
    boolean validateLogin(String email, String password) throws ClassNotFoundException;

    int getLoginId(String email) throws ClassNotFoundException;
}
