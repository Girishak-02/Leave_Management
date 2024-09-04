package com.wavemaker.service;

import com.wavemaker.model.Leave;

import java.util.List;

public interface LeaveService {
    void createLeave(Leave leave);

    List<Leave> getLeavesByEmpId(int empId);

    boolean updateLeaveStatus(int leaveId, String newStatus);

    Leave getLeaveRequestById(int leaveId);

    void deleteLeave(int leaveId);

    List<Leave> getLeavesByManagerId(int managerId);

    List<Leave> findByEmpId(int empId);
}