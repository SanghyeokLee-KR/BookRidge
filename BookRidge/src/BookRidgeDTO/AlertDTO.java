package BookRidgeDTO;

import java.sql.Timestamp;

public class AlertDTO {
    private int alertId;         // 알림 고유 번호
    private int memberNo;        // 회원 번호 (참조)
    private String alertType;    // 알림 유형 (대출, 반납 기한, 연체 등)
    private String alertMessage; // 알림 내용
    private String isRead;       // 읽음 여부 ('Y' or 'N')
    private Timestamp createdAt; // 알림 생성 일시

    // Getter와 Setter 메서드
    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        return "AlertDTO{" +
                "alertId=" + alertId +
                ", memberNo=" + memberNo +
                ", alertType='" + alertType + '\'' +
                ", alertMessage='" + alertMessage + '\'' +
                ", isRead='" + isRead + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
