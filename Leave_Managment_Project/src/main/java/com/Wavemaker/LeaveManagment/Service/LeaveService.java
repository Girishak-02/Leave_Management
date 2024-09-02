package com.Wavemaker.LeaveManagment.Service;

import com.Wavemaker.LeaveManagment.model.Leave;
import java.util.List;

public interface LeaveService {
    void createLeave(Leave leave);
    List<Leave> getLeavesByEmpId(int empId);
    void approveLeave(int leaveId);
    void rejectLeave(int leaveId);
    Leave getLeaveRequestById(int leaveId);
    void deleteLeave(int leaveId);
    List<Leave> getLeavesByManagerId(int managerId);
}