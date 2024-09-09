package com.wavemaker.service.Impl;

import com.wavemaker.service.LeaveService;
import com.wavemaker.model.Leave;
import com.wavemaker.repository.Impl.LeaveRepositoryImpl;
import com.wavemaker.repository.LeaveRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LeaveServiceImpl implements LeaveService {
    private final LeaveRepository leaveRepository;

    public LeaveServiceImpl() throws SQLException {
        this.leaveRepository = new LeaveRepositoryImpl();
    }

    @Override
    public void createLeave(Leave leave) {
        leaveRepository.createLeave(leave);
    }

    @Override
    public List<Leave> getLeavesByEmpId(int empId) {
        return leaveRepository.findByEmpId(empId);
    }

    @Override
    public boolean updateLeaveStatus(int leaveId, String newStatus) {
        return leaveRepository.updateLeaveStatus(leaveId, newStatus);
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

    @Override
    public List<Leave> findByEmpId(int empId) {
        return List.of();
    }

    @Override
    public Map<String, Integer> getLeaveCount(Integer empId) {
        return leaveRepository.getLeaveCount(empId);
    }
}