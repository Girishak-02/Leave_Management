package com.Wavemaker.LeaveManagment.repository.Impl;

import com.Wavemaker.LeaveManagment.model.Leave;
import com.Wavemaker.LeaveManagment.repository.LeaveRepository;
import com.Wavemaker.LeaveManagment.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaveRepositoryImpl implements LeaveRepository {
    private static final Logger logger = LoggerFactory.getLogger(LeaveRepositoryImpl.class);
    private static final String LIST_OF_LEAVES = "SELECT * FROM LEAVES WHERE EMP_ID = ?";
    private static final String APPROVE_LEAVE = "UPDATE LEAVES SET STATUS = 'APPROVED' WHERE LEAVE_ID = ?";
    private static final String REJECT_LEAVE = "UPDATE LEAVES SET STATUS = 'REJECTED' WHERE LEAVE_ID = ?";
    private static final String GET_LEAVE_BY_ID = "SELECT * FROM LEAVES WHERE LEAVE_ID = ?";
    private static final String DELETE_LEAVE = "DELETE FROM LEAVES WHERE LEAVE_ID = ?";
    private static final String GET_LEAVE_BY_MANAGER = "SELECT * FROM LEAVES WHERE MANAGER_ID = ?";
    private static final String INSERT = "INSERT INTO LEAVES (EMP_ID, LEAVE_TYPE, FROM_DATE, TO_DATE, REASON, MANAGER_ID, STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
    @Override
    public boolean create(Leave leave) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT)) {

            stmt.setInt(1, leave.getEmpId()); // Assuming you have an EMP_ID field in Leave
            stmt.setString(2, leave.getLeaveType().name());
            stmt.setDate(3, new Date(leave.getFromDate().getTime()));
            stmt.setDate(4, new Date(leave.getToDate().getTime()));
            stmt.setString(5, leave.getReason());
            stmt.setInt(6, leave.getManagerId());
            stmt.setString(7, "PENDING"); // Set initial status to PENDING

            stmt.executeUpdate();
            logger.info("Leave created for employee: {} and manager: {}", leave.getEmpId(), leave.getManagerId());
        } catch (SQLException e) {
            logger.error("Error creating leave", e);
            throw new RuntimeException(e); // Handle exception appropriately
        }
        return false;
    }

    @Override
    public List<Leave> findByEmpId(int empId) {
        List<Leave> leaves = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(LIST_OF_LEAVES)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
               Leave leave = new Leave(
                        rs.getInt("EMP_ID"),
                        Leave.LeaveType.valueOf(rs.getString("LEAVE_TYPE")),
                        rs.getDate("FROM_DATE"),
                        rs.getDate("TO_DATE"),
                        rs.getString("REASON"),
                        rs.getInt("MANAGER_ID")
                );
                leave.setLeaveId(rs.getInt("LEAVE_ID"));
                leave.setStatus(Leave.Status.valueOf(rs.getString("STATUS")));
                leaves.add(leave);
            }
            logger.info("Leaves read for employee: {}", empId);
        } catch (SQLException e) {
            logger.error("Error reading leaves for employee", e);
        }
        return leaves;
    }

    @Override
    public void approveLeave(int leaveId) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(APPROVE_LEAVE)) {
            stmt.setInt(1, leaveId);
            stmt.executeUpdate();
            logger.info("Leave approved: {}", leaveId);
        } catch (SQLException e) {
            logger.error("Error approving leave", e);
        }
    }

    @Override
    public void rejectLeave(int leaveId) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(REJECT_LEAVE)) {
            stmt.setInt(1, leaveId);
            stmt.executeUpdate();
            logger.info("Leave rejected: {}", leaveId);
        } catch (SQLException e) {
            logger.error("Error rejecting leave", e);
        }
    }

    @Override
    public Leave getLeaveRequestById(int leaveId) {
        Leave leave = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_LEAVE_BY_ID)) {
            stmt.setInt(1, leaveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                leave = new Leave(
                        rs.getInt("EMP_ID"),
                        Leave.LeaveType.valueOf(rs.getString("LEAVE_TYPE")),
                        rs.getDate("FROM_DATE"),
                        rs.getDate("TO_DATE"),
                        rs.getString("REASON"),
                        rs.getInt("MANAGER_ID")
                );
                leave.setLeaveId(rs.getInt("LEAVE_ID"));
                leave.setStatus(Leave.Status.valueOf(rs.getString("STATUS")));
            }
            logger.info("Leave read for leave ID: {}", leaveId);
        } catch (SQLException e) {
            logger.error("Error reading leave for leave ID", e);
        }
        return leave;
    }

    @Override
    public void deleteLeave(int leaveId) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_LEAVE)) {
            stmt.setInt(1, leaveId);
            stmt.executeUpdate();
            logger.info("Leave deleted: {}", leaveId);
        } catch (SQLException e) {
            logger.error("Error deleting leave", e);
        }
    }
    @Override
    public List<Leave> findByManagerId(int managerId) {
        List<Leave> leaves = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_LEAVE_BY_MANAGER)) {
            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Leave leave = new Leave(
                        rs.getInt("EMP_ID"),
                        Leave.LeaveType.valueOf(rs.getString("LEAVE_TYPE")),
                        rs.getDate("FROM_DATE"),
                        rs.getDate("TO_DATE"),
                        rs.getString("REASON"),
                        rs.getInt("MANAGER_ID")
                );
                leave.setLeaveId(rs.getInt("LEAVE_ID"));
                leave.setStatus(Leave.Status.valueOf(rs.getString("STATUS")));
                leaves.add(leave);
            }
            logger.info("Leaves read for manager: {}", managerId);
        } catch (SQLException e) {
            logger.error("Error reading leaves for manager", e);
        }
        return leaves;
    }
    public boolean updateLeaveStatus(int leaveId, String newStatus) {
        try (Connection connection = DBUtil.getConnection()) {
            String sql = "UPDATE LEAVES SET STATUS = ? WHERE LEAVE_ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, newStatus);
                stmt.setInt(2, leaveId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}