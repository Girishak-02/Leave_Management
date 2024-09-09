package com.wavemaker.model;

import java.util.List;
import java.util.Map;

public class Response {
    private boolean success;
    private Leave leave;
    private List<Leave> leaves;
    private String message;
    private Map<String, Integer> leaveCount;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Leave getLeave() {
        return leave;
    }

    public void setLeave(Leave leave) {
        this.leave = leave;
    }

    public List<Leave> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<Leave> leaves) {
        this.leaves = leaves;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNewStatus(String newStatus) {
        this.message = newStatus;
    }

    public void setLeaveCount(Map<String, Integer> leaveCount) {
        this.leaveCount = leaveCount;
    }

    public Map<String, Integer> getLeaveCount() {
        return leaveCount;
    }
}

