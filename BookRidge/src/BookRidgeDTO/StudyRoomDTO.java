package BookRidgeDTO;

public class StudyRoomDTO {
    private int srNo;      // 스터디룸 번호
    private String srName;  // 스터디룸 이름
    private int srPerson;   // 수용 인원
    private String stCheck; // 예약 가능 여부 ('Y' or 'N')

    // Getter and Setter methods
    public int getSrNo() {
        return srNo;
    }

    public void setSrNo(int srNo) {
        this.srNo = srNo;
    }

    public String getSrName() {
        return srName;
    }

    public void setSrName(String srName) {
        this.srName = srName;
    }

    public int getSrPerson() {
        return srPerson;
    }

    public void setSrPerson(int srPerson) {
        this.srPerson = srPerson;
    }

    public String getStCheck() {
        return stCheck;
    }

    public void setStCheck(String stCheck) {
        this.stCheck = stCheck;
    }

    @Override
    public String toString() {
        return "StudyRoomDTO{" +
                "srNo=" + srNo +
                ", srName='" + srName + '\'' +
                ", srPerson=" + srPerson +
                ", stCheck='" + stCheck + '\'' +
                '}';
    }
}
