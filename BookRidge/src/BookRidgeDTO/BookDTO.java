package BookRidgeDTO;

import java.sql.Date;
import java.sql.Timestamp;

public class BookDTO {

    private String bookNo;            // 도서번호
    private String title;             // 도서명
    private String author;            // 저자명
    private String status;            // 대출 상태
    private String publisher;         // 출판사명
    private Date publishDate;         // 출판일
    private String categoryCode;      // 분류기호
    private Timestamp registerDate;   // 등록일자

    // 기본 생성자
    public BookDTO() {
    }

    // 모든 필드를 포함하는 생성자
    public BookDTO(String bookNo, String title, String author, String status, String publisher, Date publishDate, String categoryCode, Timestamp registerDate) {
        this.bookNo = bookNo;
        this.title = title;
        this.author = author;
        this.status = status;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.categoryCode = categoryCode;
        this.registerDate = registerDate;
    }

    // Getter 및 Setter 메서드
    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Timestamp getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Timestamp registerDate) {
        this.registerDate = registerDate;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "bookNo='" + bookNo + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status='" + status + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishDate=" + publishDate +
                ", categoryCode='" + categoryCode + '\'' +
                ", registerDate=" + registerDate +
                '}';
    }
}
