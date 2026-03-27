package BookRidgeDTO;

public class AdminDTO {
    private int aNo;          // 어드민 고유 번호
    private String aId;       // 어드민 아이디
    private String aNm;       // 어드민 이름
    private String aPwd;      // 비밀번호
    private String aGender;   // 성별
    private String aPhone;    // 휴대전화
    private String aEmail;    // 이메일
    private String aRole;     // 역할
    private String aStatus;   // 사용 여부

    // Getter 및 Setter 메서드
    public int getaNo() {
        return aNo;
    }

    public void setaNo(int aNo) {
        this.aNo = aNo;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getaNm() {
        return aNm;
    }

    public void setaNm(String aNm) {
        this.aNm = aNm;
    }

    public String getaPwd() {
        return aPwd;
    }

    public void setaPwd(String aPwd) {
        this.aPwd = aPwd;
    }

    public String getaGender() {
        return aGender;
    }

    public void setaGender(String aGender) {
        this.aGender = aGender;
    }

    public String getaPhone() {
        return aPhone;
    }

    public void setaPhone(String aPhone) {
        this.aPhone = aPhone;
    }

    public String getaEmail() {
        return aEmail;
    }

    public void setaEmail(String aEmail) {
        this.aEmail = aEmail;
    }

    public String getaRole() {
        return aRole;
    }

    public void setaRole(String aRole) {
        this.aRole = aRole;
    }

    public String getaStatus() {
        return aStatus;
    }

    public void setaStatus(String aStatus) {
        this.aStatus = aStatus;
    }

    // toString 메서드
    @Override
    public String toString() {
        return "AdminDTO{" +
                "aNo=" + aNo +
                ", aId='" + aId + '\'' +
                ", aNm='" + aNm + '\'' +
                ", aPwd='" + aPwd + '\'' +
                ", aGender='" + aGender + '\'' +
                ", aPhone='" + aPhone + '\'' +
                ", aEmail='" + aEmail + '\'' +
                ", aRole='" + aRole + '\'' +
                ", aStatus='" + aStatus + '\'' +
                '}';
    }
}
