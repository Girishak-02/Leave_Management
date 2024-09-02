package com.Wavemaker.LeaveManagment.Service.Impl;

import com.Wavemaker.LeaveManagment.model.Leave;
import com.Wavemaker.LeaveManagment.repository.LeaveRepository;
import com.Wavemaker.LeaveManagment.Service.LeaveService;
import java.util.List;

public class LeaveServiceImpl implements LeaveService {
    private final LeaveRepository leaveRepository;

    public LeaveServiceImpl(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    @Override
    public void createLeave(Leave leave) {
        leaveRepository.create(leave);
    }

    @Override
    public List<Leave> getLeavesByEmpId(int empId) {
        return leaveRepository.findByEmpId(empId);
    }

    @Override
    public void approveLeave(int leaveId) {
        leaveRepository.approveLeave(leaveId);
    }

    @Override
    public void rejectLeave(int leaveId) {
        leaveRepository.rejectLeave(leaveId);
    }

    @Override
    public Leave getLeaveRequestById(int leaveId) {
        return leaveRepository.getLeaveRequestById(leaveId);
    }
    @Override
    public void deleteLeave(int leaveId) {
        leaveRepository.deleteLeave(leaveId);
    }
    @Override
    public List<Leave> getLeavesByManagerId(int managerId) {
        return leaveRepository.findByManagerId(managerId);
    }
}