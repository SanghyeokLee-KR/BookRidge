package BookRidgeDTO;

import java.sql.Date;
import java.sql.Timestamp;

public class GroupStudyReservationDTO {
    private int reservationNo;    // 예약 번호
    private int mNo;              // 회원 번호
    private int studyRoomNo;      // 스터디룸 번호
    private Date reservationDate; // 예약일
    private Timestamp startTime;  // 예약 시작 시간
    private Timestamp endTime;    // 예약 종료 시간
    private String status;        // 예약 상태 (예약중, 완료, 취소)

    private String studyRoomName;  // 스터디룸 이름 추가

    // Getter and Setter methods
    public int getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(int reservationNo) {
        this.reservationNo = reservationNo;
    }

    public int getmNo() {
        return mNo;
    }

    public void setmNo(int mNo) {
        this.mNo = mNo;
    }

    public int getStudyRoomNo() {
        return studyRoomNo;
    }

    public void setStudyRoomNo(int studyRoomNo) {
        this.studyRoomNo = studyRoomNo;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // getter and setter for studyRoomName
    public String getStudyRoomName() {
        return studyRoomName;
    }

    public void setStudyRoomName(String studyRoomName) {
        this.studyRoomName = studyRoomName;
    }

    @Override
    public String toString() {
        return "스터디룸 예약 [ " +
                "예약 번호 : " + reservationNo +
                " | 회원 번호 : " + mNo +
                " | 스터디룸 번호 : " + studyRoomNo +
                " | 예약일 : " + reservationDate +
                " | 예약 시작 시간 : " + startTime +
                " | 예약 종료 시간 : " + endTime +
                " | 예약 상태 : " + status +
                " ]";
    }
}
