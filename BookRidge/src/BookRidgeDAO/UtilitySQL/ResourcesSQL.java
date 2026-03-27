package BookRidgeDAO.UtilitySQL;

import BookRidgeDAO.DBC;
import BookRidgeDTO.MemberDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResourcesSQL {

    Connection con;               // 전역 변수로 정의된 Connection
    PreparedStatement pstmt;      // 전역 변수로 정의된 PreparedStatement
    ResultSet rs;                 // 전역 변수로 정의된 ResultSet

    // 데이터베이스 연결 메서드 (구현 클래스에서 필요시 재정의)
    public void connect() {
        con = DBC.DBConnect();
    }

    // 자원 해제 메서드
    public void closeResources() {
        // ResultSet 해제
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("ResultSet 닫기 실패: " + e.getMessage());
            }
        }

        // PreparedStatement 해제
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("PreparedStatement 닫기 실패: " + e.getMessage());
            }
        }

        // Connection 해제
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Connection 닫기 실패: " + e.getMessage());
            }
        }
    }

    // 번호로 아이디 찾는 메서드
    public int getMemberNoById(String memberId) {
        String sql = "SELECT M_NO FROM MEMBER WHERE M_ID = ?";
        int memberNo = -1; // 기본값 -1 설정

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                memberNo = rs.getInt("M_NO");
            }

        } catch (SQLException e) {
            System.out.println("회원 번호 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return memberNo;
    }
}
