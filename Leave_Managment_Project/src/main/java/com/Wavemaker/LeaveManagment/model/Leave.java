package com.Wavemaker.LeaveManagment.model;

import java.sql.Date;

public class Leave {

    public Leave() {

    }

    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    public enum LeaveType{
        SICK,PTO,CASUAL_LEAVE
    }
    private int leaveId;
    private int empId;
    private LeaveType leaveType;
    private Date fromDate;
    private Date toDate;
    private String reason;
    private int managerId;
    private Status status=Status.PENDING;

    public Leave(   int empId, LeaveType leaveType, Date fromDate, Date toDate, String reason, int managerId) {
        this.empId = empId;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.managerId = managerId;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }
    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }
    public Date getFromDate() {
        return Date.valueOf(fromDate.toLocalDate());
    }
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    public Date getToDate() {
        return Date.valueOf(toDate.toLocalDate());
    }
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Leave{" +
                "leaveId=" + leaveId +
                ", empId=" + empId +
                ", leaveType='" + leaveType + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", reason='" + reason + '\'' +
                ", managerId=" + managerId +
                ", status='" + status + '\'' +
                '}';
    }
}