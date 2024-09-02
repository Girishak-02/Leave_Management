package com.Wavemaker.LeaveManagment.repository;

import com.Wavemaker.LeaveManagment.model.Leave;

import java.util.List;

public interface LeaveRepository {
    boolean create(Leave leave);
    List<Leave> findByEmpId(int empId);
    void approveLeave(int leaveId);
    void rejectLeave(int leaveId);
    Leave getLeaveRequestById(int leaveId);
    void deleteLeave(int leaveId);

    List<Leave> findByManagerId(int managerId);

    boolean updateLeaveStatus(int leaveId, String newStatus);
}