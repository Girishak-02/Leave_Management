package com.wavemaker.model;

import java.sql.Date;

public class Employee {
    private int empId;
    private int loginId;
    private String empName;
    private String empEmail;
    private Date dob;
    private String phoneNumber;
    private int managerId;

    public Employee() {
    }

    public int getEmpId() {

        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getLoginId() {

        return loginId;
    }

    public void setLoginId(int loginId) {

        this.loginId = loginId;
    }

    public String getEmpName() {

        return empName;
    }

    public void setEmpName(String empName) {

        this.empName = empName;
    }

    public String getEmpMail() {

        return empEmail;
    }

    public void setEmpMail(String empMail) {
        this.empEmail = empMail;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    public int getManagerId() {

        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", loginId=" + loginId +
                ", empName='" + empName + '\'' +
                ", empMail='" + empEmail + '\'' +
                ", dob=" + dob +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", managerId=" + managerId +
                '}';
    }

    public void setEmpEmail(String empEmail) {

    }
}