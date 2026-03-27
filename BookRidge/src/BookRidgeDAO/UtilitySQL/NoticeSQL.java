package BookRidgeDAO.UtilitySQL;

import BookRidgeDTO.NoticeDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeSQL extends ResourcesSQL {

    // 공지사항 삽입 메서드
    public boolean insertNotice(NoticeDTO noticeDTO) {
        String sql = "INSERT INTO NOTICES (NOTICE_ID, TITLE, CONTENT, CREATED_AT, ADMIN_NO, IS_ACTIVE) " +
                     "VALUES (NOTICE_SEQ.NEXTVAL, ?, ?, SYSTIMESTAMP, ?, 'Y')";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, noticeDTO.getTitle());
            pstmt.setString(2, noticeDTO.getContent());
            pstmt.setInt(3, noticeDTO.getAdminNo());

            int result = pstmt.executeUpdate();
            return result > 0; // 성공 시 true 반환

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    // 공지사항 수정 메서드
    public boolean updateNotice(NoticeDTO noticeDTO) {
        String sql = "UPDATE NOTICES SET TITLE = ?, CONTENT = ?, UPDATED_AT = SYSTIMESTAMP WHERE NOTICE_ID = ?";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, noticeDTO.getTitle());
            pstmt.setString(2, noticeDTO.getContent());
            pstmt.setInt(3, noticeDTO.getNoticeId());

            int result = pstmt.executeUpdate();
            return result > 0; // 성공 시 true 반환

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    // 공지사항 비활성화 메서드
    public boolean deactivateNotice(int noticeId) {
        String sql = "UPDATE NOTICES SET IS_ACTIVE = 'N' WHERE NOTICE_ID = ?";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, noticeId);

            int result = pstmt.executeUpdate();
            return result > 0; // 성공 시 true 반환

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    // 전체 공지사항 조회 메서드
    public List<NoticeDTO> getAllNotices() {
        String sql = "SELECT * FROM NOTICES ORDER BY CREATED_AT DESC";
        List<NoticeDTO> noticeList = new ArrayList<>();
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                NoticeDTO notice = new NoticeDTO();
                notice.setNoticeId(rs.getInt("NOTICE_ID"));
                notice.setTitle(rs.getString("TITLE"));
                notice.setContent(rs.getString("CONTENT"));
                notice.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                notice.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
                notice.setAdminNo(rs.getInt("ADMIN_NO"));
                notice.setIsActive(rs.getString("IS_ACTIVE"));
                
                noticeList.add(notice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return noticeList;
    }

    // 공지사항 조회를 위한 메서드
    public NoticeDTO getNoticeById(int noticeId) {
        String sql = "SELECT NOTICE_ID, TITLE, CONTENT, CREATED_AT, UPDATED_AT, ADMIN_NO, IS_ACTIVE FROM NOTICES WHERE NOTICE_ID = ?";
        NoticeDTO notice = null;

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, noticeId); // 입력받은 공지사항 ID 설정
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // 결과가 있을 경우 NoticeDTO에 데이터 저장
                notice = new NoticeDTO();
                notice.setNoticeId(rs.getInt("NOTICE_ID"));
                notice.setTitle(rs.getString("TITLE"));
                notice.setContent(rs.getString("CONTENT"));
                notice.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                notice.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
                notice.setAdminNo(rs.getInt("ADMIN_NO"));
                notice.setIsActive(rs.getString("IS_ACTIVE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제
        }

        return notice; // 조회된 공지사항 반환
    }
}
