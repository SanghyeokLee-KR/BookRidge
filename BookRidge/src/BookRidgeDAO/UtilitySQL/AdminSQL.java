package BookRidgeDAO.UtilitySQL;

import BookRidgeDTO.ActivityLogDTO;
import BookRidgeDTO.AdminDTO;
import BookRidgeDTO.MemberDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminSQL extends ResourcesSQL {

    // 관리자 로그인 SQL 메서드
    public AdminDTO loginAdmin(AdminDTO adminDTO) {
        String sql = "SELECT * FROM ADMIN WHERE A_ID = ? AND A_PWD = ?";
        AdminDTO loggedInAdmin = null;

        try {
            connect(); // 데이터베이스 연결
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, adminDTO.getaId());
            pstmt.setString(2, adminDTO.getaPwd());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // 로그인 성공 시 AdminDTO 객체에 데이터 설정
                loggedInAdmin = new AdminDTO();
                loggedInAdmin.setaNo(rs.getInt("A_NO"));
                loggedInAdmin.setaId(rs.getString("A_ID"));
                loggedInAdmin.setaPwd(rs.getString("A_PWD"));
                loggedInAdmin.setaNm(rs.getString("A_NM"));
                loggedInAdmin.setaPhone(rs.getString("A_PHONE"));
                loggedInAdmin.setaEmail(rs.getString("A_EMAIL"));
                loggedInAdmin.setaRole(rs.getString("A_ROLE"));
                loggedInAdmin.setaStatus(rs.getString("A_STATUS"));
            }

        } catch (SQLException e) {
            System.out.println("관리자 로그인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제 및 연결 종료
        }

        return loggedInAdmin; // 로그인 성공 시 관리자 정보 반환, 실패 시 null 반환
    }

    // 전체 회원 조회 메서드
    public List<MemberDTO> getAllMembers() {
        String sql = "SELECT M_NO, M_ID, M_NAME, M_PHONE, M_EMAIL, M_STATUS FROM MEMBER ORDER BY M_NO ASC";
        List<MemberDTO> memberList = new ArrayList<>();

        try {
            connect(); // 데이터베이스 연결
            System.out.println("데이터베이스에 연결되었습니다."); // 디버깅 출력
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MemberDTO member = new MemberDTO();
                member.setmNo(rs.getInt("M_NO"));
                member.setmId(rs.getString("M_ID"));
                member.setmName(rs.getString("M_NAME"));
                member.setmPhone(rs.getString("M_PHONE"));
                member.setmEmail(rs.getString("M_EMAIL"));
                member.setmStatus(rs.getString("M_STATUS"));
                memberList.add(member);
            }

            System.out.println("조회된 회원 수: " + memberList.size()); // 디버깅 출력

        } catch (SQLException e) {
            System.out.println("회원 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return memberList;
    }

    // 이름 또는 아이디로 회원 검색 메서드
    public List<MemberDTO> searchMembers(String keyword) {
        String sql = "SELECT M_NO, M_ID, M_NAME, M_PHONE, M_EMAIL, M_STATUS " +
                "FROM MEMBER WHERE M_NAME LIKE ? OR M_ID LIKE ?";
        List<MemberDTO> memberList = new ArrayList<>();

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            String searchKeyword = "%" + keyword + "%";
            pstmt.setString(1, searchKeyword);
            pstmt.setString(2, searchKeyword);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                MemberDTO member = new MemberDTO();
                member.setmNo(rs.getInt("M_NO"));
                member.setmId(rs.getString("M_ID"));
                member.setmName(rs.getString("M_NAME"));
                member.setmPhone(rs.getString("M_PHONE"));
                member.setmEmail(rs.getString("M_EMAIL"));
                member.setmStatus(rs.getString("M_STATUS"));
                memberList.add(member);
            }

        } catch (SQLException e) {
            System.out.println("회원 검색 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return memberList;
    }

    // 유저 수를 확인하는 메서드
    public int getUserCount() {

        String sql = "SELECT COUNT(*) AS total FROM MEMBER";
        int userCount = 0;

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                userCount = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println("회원 수 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return userCount;
    }

    // 회원 활동 로그 저장 메서드
    public void insertActivityLog(int memberNo, String activityType, String activityDetail) {
        String sql = "INSERT INTO MEMBER_ACTIVITY_LOG (LOG_ID, M_NO, ACTIVITY_TYPE, ACTIVITY_DETAIL, ACTIVITY_DATE) " +
                "VALUES (MEMBER_ACTIVITY_LOG_SEQ.NEXTVAL, ?, ?, ?, SYSTIMESTAMP)";

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberNo);
            pstmt.setString(2, activityType);
            pstmt.setString(3, activityDetail);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("활동 로그 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제 및 연결 종료
        }
    }

    // 회원 활동 로그 조회 메서드
    public List<ActivityLogDTO> getActivityLogs(int memberNo) {
        String sql = "SELECT LOG_ID, M_NO, ACTIVITY_TYPE, ACTIVITY_DETAIL, ACTIVITY_DATE " +
                     "FROM MEMBER_ACTIVITY_LOG WHERE M_NO = ? ORDER BY LOG_ID ASC";
        List<ActivityLogDTO> logList = new ArrayList<>();

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberNo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ActivityLogDTO log = new ActivityLogDTO();
                log.setLogId(rs.getInt("LOG_ID"));
                log.setMemberNo(rs.getInt("M_NO"));
                log.setActivityType(rs.getString("ACTIVITY_TYPE"));
                log.setActivityDetail(rs.getString("ACTIVITY_DETAIL"));
                log.setActivityDate(rs.getTimestamp("ACTIVITY_DATE")); // 시간 포함한 TIMESTAMP 사용
                logList.add(log);
            }

        } catch (SQLException e) {
            System.out.println("활동 로그 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return logList;
    }


    // 회원 정보 수정 SQL 처리 메서드
    public void updateMemberInfoInDB(String memberId, int choice, String newValue) {
        // 선택한 항목에 맞는 SQL 쿼리 설정
        String sql = (choice == 1) ? "UPDATE MEMBER SET M_PW = ? WHERE M_ID = ?"
                : (choice == 2) ? "UPDATE MEMBER SET M_EMAIL = ? WHERE M_ID = ?"
                : "UPDATE MEMBER SET M_PHONE = ? WHERE M_ID = ?";

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newValue);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // 회원 상태 변경 SQL 처리 메서드
    public void updateMemberStatus(String memberId, String status) {
        String sql = "UPDATE MEMBER SET M_STATUS = ? WHERE M_ID = ?";

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // 회원 탈티 메서드
    public void deleteMemberStatus(String mId) {
        String sql = "UPDATE MEMBER SET M_STATIS = ? WHERE M_ID = ?";

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "탈퇴");
            pstmt.setString(2, mId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // 관리자 정보를 ID로 조회하는 메서드
    public AdminDTO getAdminById(int adminNo) {
        String sql = "SELECT A_NM, A_ROLE FROM ADMIN WHERE A_NO = ?";
        AdminDTO adminDTO = null;

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, adminNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                adminDTO = new AdminDTO();
                adminDTO.setaNm(rs.getString("A_NM"));  // 관리자 이름 설정
                adminDTO.setaRole(rs.getString("A_ROLE"));  // 관리자 직위 설정
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return adminDTO;
    }
}
