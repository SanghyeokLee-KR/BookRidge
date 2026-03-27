package BookRidgeDTO;

import java.sql.Timestamp;

public class WishBookDTO {

    private int wishBookId;         // 희망 도서 ID
    private int memberNo;           // 회원 번호 (희망 도서 신청한 회원)
    private String title;           // 희망 도서 제목
    private String author;          // 희망 도서 저자
    private String publisher;       // 희망 도서 출판사
    private Timestamp requestDate;  // 신청일
    private String status;          // 신청 상태 (대기, 승인, 반려)
    private String adminResponse;   // 관리자 응답 메시지
    private int adminNo;            // 응답한 관리자 번호

    // Getter and Setter methods
    public int getWishBookId() {
        return wishBookId;
    }

    public void setWishBookId(int wishBookId) {
        this.wishBookId = wishBookId;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public int getAdminNo() {
        return adminNo;
    }

    public void setAdminNo(int adminNo) {
        this.adminNo = adminNo;
    }

    @Override
    public String toString() {
        return "WishBookDTO{" +
                "wishBookId=" + wishBookId +
                ", memberNo=" + memberNo +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", requestDate=" + requestDate +
                ", status='" + status + '\'' +
                ", adminResponse='" + adminResponse + '\'' +
                ", adminNo=" + adminNo +
                '}';
    }
}
