package BookRidgeDAO.UtilitySQL;

import BookRidgeDTO.AlertDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertSQL extends ResourcesSQL {

    // 알림 생성 메서드
    public boolean createAlert(AlertDTO alertDTO) {
        String sql = "INSERT INTO ALERTS (ALERT_ID, M_NO, ALERT_TYPE, ALERT_MESSAGE, IS_READ, CREATED_AT) " +
                     "VALUES (ALERT_SEQ.NEXTVAL, ?, ?, ?, 'N', SYSTIMESTAMP)";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, alertDTO.getMemberNo());
            pstmt.setString(2, alertDTO.getAlertType());
            pstmt.setString(3, alertDTO.getAlertMessage());

            int result = pstmt.executeUpdate();
            return result > 0;  // 성공 시 true 반환

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    // 특정 회원의 알림 목록 조회 메서드
    public List<AlertDTO> getAlertsByMember(int memberNo) {
        String sql = "SELECT * FROM ALERTS WHERE M_NO = ? ORDER BY CREATED_AT DESC";
        List<AlertDTO> alerts = new ArrayList<>();
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberNo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AlertDTO alert = new AlertDTO();
                alert.setAlertId(rs.getInt("ALERT_ID"));
                alert.setMemberNo(rs.getInt("M_NO"));
                alert.setAlertType(rs.getString("ALERT_TYPE"));
                alert.setAlertMessage(rs.getString("ALERT_MESSAGE"));
                alert.setIsRead(rs.getString("IS_READ"));
                alert.setCreatedAt(rs.getTimestamp("CREATED_AT"));

                alerts.add(alert);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return alerts;
    }

    // 알림 읽음 상태 업데이트 메서드
    public boolean markAlertAsRead(int alertId) {
        String sql = "UPDATE ALERTS SET IS_READ = 'Y' WHERE ALERT_ID = ?";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, alertId);

            int result = pstmt.executeUpdate();
            return result > 0;  // 성공 시 true 반환

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    // AlertSQL 클래스 내 읽지 않은 알림 개수를 가져오는 메서드
    public int getAlertCount(int memberNo) {
        String sql = "SELECT COUNT(*) AS UNREAD_COUNT FROM ALERTS WHERE M_NO = ? AND IS_READ = 'N'";
        int unreadCount = 0;

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                unreadCount = rs.getInt("UNREAD_COUNT");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return unreadCount;
    }

    // 특정 회원의 안 읽은 알림 목록 조회 메서드
    public List<AlertDTO> getUnreadAlertsByMember(int memberNo) {
        String sql = "SELECT * FROM ALERTS WHERE M_NO = ? AND IS_READ = 'N' ORDER BY CREATED_AT DESC";
        List<AlertDTO> alerts = new ArrayList<>();
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberNo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AlertDTO alert = new AlertDTO();
                alert.setAlertId(rs.getInt("ALERT_ID"));
                alert.setMemberNo(rs.getInt("M_NO"));
                alert.setAlertType(rs.getString("ALERT_TYPE"));
                alert.setAlertMessage(rs.getString("ALERT_MESSAGE"));
                alert.setIsRead(rs.getString("IS_READ"));
                alert.setCreatedAt(rs.getTimestamp("CREATED_AT"));

                alerts.add(alert);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return alerts;
    }

}
