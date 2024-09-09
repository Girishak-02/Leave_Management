package com.wavemaker.model;

import java.time.LocalDate;

public class Leave {

    private int leaveId;
    private int empId;
    private String leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
    private int managerId;
    private String status = "PENDING";
    public Leave() {

    }
    public Leave(int leaveId, int empId, String leaveType, LocalDate fromDate, LocalDate toDate, String reason, int managerId) {
        this.empId = empId;
        this.leaveId = leaveId;
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
    public String getLeaveType() {
        return leaveType;
    }
    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;
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

    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    public enum LeaveType {
        SICK, PTO, CASUAL_LEAVE
    }
}