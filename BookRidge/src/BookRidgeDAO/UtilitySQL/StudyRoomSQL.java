package BookRidgeDAO.UtilitySQL;

import BookRidgeDTO.GroupStudyReservationDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudyRoomSQL extends ResourcesSQL {

    // 스터디룸 예약 삽입 SQL 메서드
    public void insertReservation(GroupStudyReservationDTO reservation) {
        String sqlInsert = "INSERT INTO GROUP_STUDY_RESERVATIONS (RESERVATION_NO, M_NO, STUDY_ROOM_NO, RESERVATION_DATE, START_TIME, END_TIME, STATUS) " +
                "VALUES (GROUP_STUDY_RESERVATION_SEQ.NEXTVAL, ?, ?, SYSDATE, ?, ?, '예약중')";

        String sqlUpdate = "UPDATE STUDY_ROOMS SET STCHECK = 'N' WHERE SRNO = ?";

        try {
            connect();  // DB 연결

            // 예약 삽입 쿼리 실행
            pstmt = con.prepareStatement(sqlInsert);
            pstmt.setInt(1, reservation.getmNo());
            pstmt.setInt(2, reservation.getStudyRoomNo());
            pstmt.setTimestamp(3, reservation.getStartTime());  // Timestamp로 전달
            pstmt.setTimestamp(4, reservation.getEndTime());    // Timestamp로 전달
            int insertResult = pstmt.executeUpdate();

            // 예약 삽입 성공 시 스터디룸 상태 업데이트
            if (insertResult > 0) {
                pstmt = con.prepareStatement(sqlUpdate);
                pstmt.setInt(1, reservation.getStudyRoomNo());  // 스터디룸 번호를 이용해 상태 업데이트
                int updateResult = pstmt.executeUpdate();
                if (updateResult > 0) {
                    System.out.println();
                } else {
                    System.out.println("스터디룸 상태 변경에 실패했습니다.");
                }
            } else {
                System.out.println("스터디룸 예약에 실패했습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();  // 자원 해제
        }
    }

    // 특정 회원의 스터디룸 예약을 조회하는 SQL 메서드
    public List<GroupStudyReservationDTO> getReservationsByMember(int memberNo) {
        List<GroupStudyReservationDTO> reservationList = new ArrayList<>();
        String sql = "SELECT GR.RESERVATION_NO, GR.M_NO, SR.SRNAME, GR.START_TIME, GR.END_TIME, GR.STATUS\n" +
                "FROM GROUP_STUDY_RESERVATIONS GR\n" +
                "JOIN STUDY_ROOMS SR ON GR.STUDY_ROOM_NO = SR.SRNO\n" +
                "WHERE GR.M_NO = ?";

        try {
            connect();  // DB 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberNo);  // 회원 번호 설정
            rs = pstmt.executeQuery();

            while (rs.next()) {
                GroupStudyReservationDTO reservation = new GroupStudyReservationDTO();
                reservation.setReservationNo(rs.getInt("RESERVATION_NO"));
                reservation.setmNo(rs.getInt("M_NO"));
                reservation.setStudyRoomName(rs.getString("SRNAME")); // 이 부분에서 "SRNAME" 열이 실제로 있는지 확인
                reservation.setStartTime(rs.getTimestamp("START_TIME"));
                reservation.setEndTime(rs.getTimestamp("END_TIME"));
                reservation.setStatus(rs.getString("STATUS"));

                reservationList.add(reservation);  // 리스트에 추가
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();  // 자원 해제
        }

        return reservationList;
    }

    // 스터디룸 예약 가능 여부 확인 메서드
    public boolean isStudyRoomAvailable(int roomNo) {
        String sql = "SELECT STCHECK FROM STUDY_ROOMS WHERE SRNO = ? AND STCHECK = 'Y'";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, roomNo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;  // 'Y'일 때 true 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false; // 기본적으로 예약 불가능으로 설정
    }

    // 예약 취소 SQL 메서드
    public boolean cancelReservation(int reservationNo) {
        String sqlCancel = "UPDATE GROUP_STUDY_RESERVATIONS SET STATUS = '취소' WHERE RESERVATION_NO = ?";
        String sqlUpdateRoom = "UPDATE STUDY_ROOMS SET STCHECK = 'Y' WHERE SRNO = (SELECT STUDY_ROOM_NO FROM GROUP_STUDY_RESERVATIONS WHERE RESERVATION_NO = ?)";

        try {
            connect(); // DB 연결

            // 예약 취소 쿼리 실행
            pstmt = con.prepareStatement(sqlCancel);
            pstmt.setInt(1, reservationNo);
            int result = pstmt.executeUpdate();

            if (result > 0) {
                // 스터디룸 상태 업데이트
                pstmt = con.prepareStatement(sqlUpdateRoom);
                pstmt.setInt(1, reservationNo);
                pstmt.executeUpdate();

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }
}
