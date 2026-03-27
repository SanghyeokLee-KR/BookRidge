package BookRidgeDTO;

import java.util.Date;

public class ActivityLogDTO {
    private int logId;          // 로그 고유 번호
    private int memberNo;       // 회원 번호
    private String activityType;  // 활동 유형 (예: 로그인, 로그아웃, 대출, 반납 등)
    private String activityDetail; // 활동 상세 내용
    private Date activityDate;   // 활동 일시


    // Getters and Setters
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(String activityDetail) {
        this.activityDetail = activityDetail;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    // toString() 메서드 오버라이드
    @Override
    public String toString() {
        return "활동기록 [ " +
                "로그 고유 번호 : " + logId +
                " | 회원 번호 : " + memberNo +
                " | 활동 유형 : " + activityType +
                " | 활동 상세내용 : " + activityDetail +
                " | 활동 일시 : " + activityDate +
                " ]";
    }
}
