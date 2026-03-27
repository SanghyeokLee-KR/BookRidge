package BookRidgeDTO;

import java.sql.Date;

public class ReservationDTO {
    private int reservationNo;     // 예약 번호
    private int memberNo;          // 회원 번호
    private String bookNo;         // 도서 번호
    private Date reservationDate;  // 예약일
    private Date expirationDate;   // 예약 만료일
    private String status;         // 예약 상태 (예약중, 완료, 취소)

    // Getters and Setters
    public int getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(int reservationNo) {
        this.reservationNo = reservationNo;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "예약 [ " +
                "예약 번호 : " + reservationNo +
                " | 회원 번호 : " + memberNo +
                " | 도서 번호 : " + bookNo +
                " | 예약일 : " + reservationDate +
                " | 예약 만료일 : " + expirationDate +
                " | 예약 상태 : " + status +
                " ]";
    }

}
