package BookRidgeDTO;

// 회원 정보를 담고 있는 DTO 클래스

public class MemberDTO {
    private int mNo;        // 회원번호
    private String mId;     // 아이디
    private String mPw;     // 비밀번호
    private String mName;   // 이름
    private String mGender; // 성별 여부 (문자열 형식, '남성' or '여성')
    private String mPhone;  // 전화번호
    private String mEmail;  // 회원 이메일
    private String mEnrollDate; // 가입일 (문자열 형식, 실제 사용 시 Date 타입으로 변환 고려)

    private String mRental; // 대출 가능 여부 (문자열 형식, 'Y' or 'N')
    private String mStatus; // 회원 상태 (문자열 형식, 예: '정상', '탈퇴', '정지')

    // 회원번호를 반환하는 getter 메서드
    public int getmNo() {
        return mNo;
    }

    // 회원번호를 설정하는 setter 메서드
    public void setmNo(int mNo) {
        this.mNo = mNo;
    }

    // 아이디를 반환하는 getter 메서드
    public String getmId() {
        return mId;
    }

    // 아이디를 설정하는 setter 메서드
    public void setmId(String mId) {
        this.mId = mId;
    }

    // 비밀번호를 반환하는 getter 메서드
    public String getmPw() {
        return mPw;
    }

    // 비밀번호를 설정하는 setter 메서드
    public void setmPw(String mPw) {
        this.mPw = mPw;
    }

    // 이름을 반환하는 getter 메서드
    public String getmName() {
        return mName;
    }

    // 이름을 설정하는 setter 메서드
    public void setmName(String mName) {
        this.mName = mName;
    }

    // 전화번호를 반환하는 getter 메서드
    public String getmPhone() {
        return mPhone;
    }

    // 전화번호를 설정하는 setter 메서드
    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    // 가입일을 반환하는 getter 메서드
    public String getmEnrollDate() {
        return mEnrollDate;
    }

    // 가입일을 설정하는 setter 메서드
    public void setmEnrollDate(String mEnrollDate) {
        this.mEnrollDate = mEnrollDate;
    }

    // 대출 가능 여부를 반환하는 getter 메서드
    public String getmRental() {
        return mRental;
    }

    // 대출 가능 여부를 설정하는 setter 메서드
    public void setmRental(String mRental) {
        this.mRental = mRental;
    }

    // 성별 여부를 반환하는 getter 메서드
    public String getmGender() {
        return mGender;
    }

    // 성별 여부를 설정하는 setter 메서드
    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    // 회원 상태를 반환하는 getter 메서드
    public String getmStatus() {
        return mStatus;
    }

    // 회원 상태를 설정하는 setter 메서드
    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    @Override
    public String toString() {
        return "회원 [ " +
                "회원 번호 : " + mNo +
                " | 아이디 : " + mId +
                " | 비밀번호 : " + mPw +
                " | 이름 : " + mName +
                " | 성별 여부 : " + mGender +
                " | 연락처 : " + mPhone +
                " | 이메일 : " + mEmail +
                " | 가입일 : " + mEnrollDate +
                " | 대여 가능 여부 : " + mRental +
                " | 회원 활동 상태 : " + mStatus +
                " ]";
    }

}