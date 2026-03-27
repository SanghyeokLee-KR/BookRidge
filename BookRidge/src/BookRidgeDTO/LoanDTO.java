package BookRidgeDTO;

import java.sql.Date;

public class LoanDTO {
    private int loanNo;           // 대출 번호
    private int memberNo;         // 회원 번호
    private String bookNo;        // 도서 번호
    private Date loanDate;        // 대출일
    private Date dueDate;         // 반납 기한일
    private Date returnDate;      // 실제 반납일
    private String isOverdue;     // 연체 여부 (Y/N)

    // Getters and Setters

    public int getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(int loanNo) {
        this.loanNo = loanNo;
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

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(String isOverdue) {
        this.isOverdue = isOverdue;
    }

    @Override
    public String toString() {
        return "LoanDTO{" +
                "loanNo=" + loanNo +
                ", memberNo=" + memberNo +
                ", bookNo='" + bookNo + '\'' +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", isOverdue='" + isOverdue + '\'' +
                '}';
    }

    // Getter and Setter for mNo (회원 번호)
    public int getmNo() {
        return memberNo;
    }

    public void setmNo(int mNo) {
        this.memberNo = mNo;
    }
}
