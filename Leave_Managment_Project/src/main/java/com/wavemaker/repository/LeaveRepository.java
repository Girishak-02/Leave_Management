package com.wavemaker.repository;

import com.wavemaker.model.Leave;

import java.util.List;
import java.util.Map;

public interface LeaveRepository {
    void createLeave(Leave leave);

    List<Leave> findByEmpId(int empId);

    Leave getLeaveRequestById(int leaveId);

    void deleteLeave(int leaveId);

    List<Leave> findByManagerId(int managerId);

    boolean updateLeaveStatus(int leaveId, String newStatus);


    Map<String, Integer> getLeaveCount(Integer empId);
}