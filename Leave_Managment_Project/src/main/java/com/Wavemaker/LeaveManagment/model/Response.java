package com.Wavemaker.LeaveManagment.model;

import java.util.List;
public class Response {
    private boolean success;
    private Leave leave;
    private List<Leave> leaves;
    private String message;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public Leave getLeave() { return leave; }
    public void setLeave(Leave leave) { this.leave = leave; }
    public List<Leave> getLeaves() { return leaves; }
    public void setLeaves(List<Leave> leaves) { this.leaves = leaves; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public void setNewStatus(String newStatus) {
        this.message = newStatus;
    }
}

