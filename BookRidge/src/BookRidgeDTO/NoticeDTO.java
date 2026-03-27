package BookRidgeDTO;

import java.sql.Timestamp;

public class NoticeDTO {
    private int noticeId;        // 공지사항 ID
    private String title;        // 공지 제목
    private String content;      // 공지 내용
    private Timestamp createdAt; // 공지 생성일
    private Timestamp updatedAt; // 공지 수정일
    private int adminNo;         // 관리자 번호
    private String isActive;     // 활성화 여부

    // Getter and Setter methods
    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getAdminNo() {
        return adminNo;
    }

    public void setAdminNo(int adminNo) {
        this.adminNo = adminNo;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
