package com.Wavemaker.LeaveManagment.Service;

public interface LoginService {
    boolean validateLogin(String email, String password) throws ClassNotFoundException;
    int getLoginId(String email) throws ClassNotFoundException;
}
