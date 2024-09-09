package com.wavemaker.repository.Impl;

import com.wavemaker.model.Leave;
import com.wavemaker.repository.LeaveRepository;
import com.wavemaker.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LeaveRepositoryImpl implements LeaveRepository {

    private static final Logger logger = LoggerFactory.getLogger(LeaveRepositoryImpl.class);
    private static final String LIST_OF_LEAVES = "SELECT * FROM LEAVES WHERE EMP_ID = ?";
    private static final String UPDATE_LEAVE_STATUS = "UPDATE LEAVES SET STATUS =? WHERE LEAVE_ID = ?";
    private static final String GET_LEAVE_BY_ID = "SELECT * FROM LEAVES WHERE LEAVE_ID = ?";
    private static final String DELETE_LEAVE = "DELETE FROM LEAVES WHERE LEAVE_ID = ?";
    private static final String GET_LEAVE_BY_MANAGER = "SELECT * FROM LEAVES WHERE MANAGER_ID = ?";
    private static final String INSERT = "INSERT INTO LEAVES (EMP_ID, LEAVE_TYPE, FROM_DATE, TO_DATE, REASON, MANAGER_ID,STATUS) VALUES (?, ?, ?, ?, ?, ?,?)";
    private static final String GET_LEAVE_COUNT = "SELECT LEAVE_TYPE, SUM(DATEDIFF (TO_DATE,FROM_DATE)+1) FROM LEAVES WHERE EMP_ID = ? AND STATUS='APPROVED' GROUP BY LEAVE_TYPE";
    private final Connection conn;
    public LeaveRepositoryImpl() throws SQLException {
        this.conn = DBUtil.getConnection();
    }

    @Override
    public void createLeave(Leave leave) {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT)) {
            stmt.setInt(1, leave.getEmpId());
            stmt.setString(2, leave.getLeaveType());
            stmt.setDate(3, java.sql.Date.valueOf(leave.getFromDate()));
            stmt.setDate(4, java.sql.Date.valueOf(leave.getToDate()));
            stmt.setString(5, leave.getReason());
            stmt.setInt(6, leave.getManagerId());
            stmt.setString(7, leave.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error creating leave", e);
            throw new RuntimeException("Error creating leave: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Leave> findByEmpId(int empId) {
        List<Leave> leaves = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(LIST_OF_LEAVES)) {

            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Leave leave = new Leave();

                leave.setLeaveId(rs.getInt("LEAVE_ID"));
                leave.setEmpId(rs.getInt("EMP_ID"));
                leave.setLeaveType(rs.getString("LEAVE_TYPE"));
                leave.setFromDate(rs.getDate("FROM_DATE").toLocalDate());
                leave.setToDate(rs.getDate("TO_DATE").toLocalDate());
                leave.setReason(rs.getString("REASON"));
                leave.setManagerId(rs.getInt("MANAGER_ID"));  // MANAGER_ID is being set here
                leave.setStatus(rs.getString("STATUS"));
                leaves.add(leave);
            }

            logger.info("Leaves read for employee: {}", empId);
        } catch (SQLException e) {
            logger.error("Error reading leaves for employee", e);
        }
        return leaves;
    }

    @Override
    public boolean updateLeaveStatus(int leaveId, String newStatus) {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_LEAVE_STATUS)) {
            stmt.setInt(2, leaveId);
            stmt.setString(1, newStatus);
            System.out.println(leaveId);
            System.out.println(newStatus);
            stmt.executeUpdate();
            logger.info("Leave approved: {}", leaveId);
            return  true;
        } catch (SQLException e) {
            logger.error("Error approving leave", e);
        }
        return false;
    }

    @Override
    public Map<String, Integer> getLeaveCount(Integer empId) {
        try{
            PreparedStatement statement = conn.prepareStatement(GET_LEAVE_COUNT);
            statement.setInt(1, empId);
            ResultSet rs = statement.executeQuery();
            Map<String, Integer> leaveCount = new ConcurrentHashMap<>();

            while(rs.next()){
                leaveCount.put(rs.getString("LEAVE_TYPE"), rs.getInt(2));

            }
            return leaveCount;

        }catch(SQLException e){
            logger.error("Error getting leave count", e);
        }
        return Map.of();
    }

    @Override
    public Leave getLeaveRequestById(int leaveId) {
        Leave leave = null;
        try (PreparedStatement stmt = conn.prepareStatement(GET_LEAVE_BY_ID)) {
            stmt.setInt(1, leaveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                leave = new Leave();
                leave.setEmpId(rs.getInt("EMP_ID"));
                leave.setLeaveType(rs.getString("LEAVE_TYPE"));
                leave.setFromDate(rs.getDate("FROM_DATE").toLocalDate());
                leave.setToDate(rs.getDate("TO_DATE").toLocalDate());
                leave.setReason(rs.getString("REASON"));
                leave.setManagerId(rs.getInt("MANAGER_ID"));
                leave.setStatus(rs.getString("STATUS"));


            }
            logger.info("Leave read for leave ID: {}", leaveId);
        } catch (SQLException e) {
            logger.error("Error reading leave for leave ID", e);
        }
        return leave;
    }

    @Override
    public void deleteLeave(int leaveId) {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_LEAVE)) {
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
        try (PreparedStatement stmt = conn.prepareStatement(GET_LEAVE_BY_MANAGER)) {
            stmt.setInt(1, managerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Leave leave = new Leave();
                leave.setEmpId(rs.getInt("EMP_ID"));
                leave.setLeaveId(rs.getInt("LEAVE_ID"));
                leave.setLeaveType((rs.getString("LEAVE_TYPE")));
                leave.setFromDate(rs.getDate("FROM_DATE").toLocalDate());
                leave.setToDate(rs.getDate("TO_DATE").toLocalDate());
                leave.setReason(rs.getString("REASON"));
                leave.setManagerId(rs.getInt("MANAGER_ID"));
                leave.setStatus(rs.getString("STATUS"));
                leaves.add(leave);
            }
            logger.info("Leaves read for manager: {}", managerId);
        } catch (SQLException e) {
            logger.error("Error reading leaves for manager", e);
        }
        return leaves;
    }

}


