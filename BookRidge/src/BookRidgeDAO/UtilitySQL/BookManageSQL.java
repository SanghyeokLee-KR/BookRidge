package BookRidgeDAO.UtilitySQL;

import BookRidgeDTO.BookDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookManageSQL extends ResourcesSQL {

    // 도서번호로 도서 존재 여부 확인 메서드
    public boolean isBookExist(String bookNo) {
        String sql = "SELECT COUNT(*) FROM BOOKS WHERE BOOK_NO = ?";
        boolean exists = false;

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo); // 도서번호 바인딩
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // 해당 도서번호가 존재하면 true 반환
                exists = rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제
        }

        return exists;
    }

    // 카테고리별 페이징 처리를 위한 도서 목록 조회 메서드
    public List<BookDTO> CategoryPaging(String categoryCode, int pageNum, int limit) {
        List<BookDTO> bookList = new ArrayList<>();
        int startRow = (pageNum - 1) * limit + 1;
        int endRow = pageNum * limit;

        String sql = "SELECT * FROM ( " +
                "  SELECT BOOK_NO, TITLE, AUTHOR, PUBLISHER, PUBLISH_DATE, CATEGORY_CODE, STATUS, ROWNUM AS RN " +  // STATUS 추가
                "  FROM BOOKS " +
                "  WHERE CATEGORY_CODE = ? AND ROWNUM <= ? " +
                ") WHERE RN >= ?";

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, categoryCode); // 카테고리 코드 바인딩
            pstmt.setInt(2, endRow); // 마지막 행 번호 설정
            pstmt.setInt(3, startRow); // 첫 번째 행 번호 설정

            rs = pstmt.executeQuery();

            // 결과를 BookDTO 객체로 변환하여 리스트에 추가
            while (rs.next()) {
                BookDTO book = new BookDTO();
                book.setBookNo(rs.getString("BOOK_NO"));
                book.setTitle(rs.getString("TITLE"));
                book.setAuthor(rs.getString("AUTHOR"));
                book.setPublisher(rs.getString("PUBLISHER"));
                book.setPublishDate(rs.getDate("PUBLISH_DATE"));
                book.setCategoryCode(rs.getString("CATEGORY_CODE"));
                book.setStatus(rs.getString("STATUS"));  // 대출 상태 추가
                bookList.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제
        }

        return bookList; // 카테고리별 페이징 처리된 도서 목록 반환
    }

    // 카테고리별 총 도서 수를 구하는 메서드
    public int totalBooksCategory(String categoryCode) {
        String sql = "SELECT COUNT(*) FROM BOOKS WHERE CATEGORY_CODE = ?";
        int count = 0;

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, categoryCode); // 카테고리 코드 바인딩

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1); // 카테고리별 총 도서 수 가져오기
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제
        }

        return count;
    }

    // 뷰를 사용하는 페이징 메서드 (카테고리 코드 없이 전체 조회)
    public List<BookDTO> getBooksByPage(int pageNum, int limit) {
        List<BookDTO> bookList = new ArrayList<>();
        int startRow = (pageNum - 1) * limit + 1;
        int endRow = pageNum * limit;

        String sql = "SELECT * FROM BOOKLIST_VIEW WHERE RN BETWEEN ? AND ?";

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, startRow);
            pstmt.setInt(2, endRow);

            rs = pstmt.executeQuery();

            // 결과를 BookDTO 객체로 변환하여 리스트에 추가
            while (rs.next()) {
                BookDTO book = new BookDTO();
                book.setBookNo(rs.getString("BOOK_NO"));
                book.setTitle(rs.getString("TITLE"));
                book.setAuthor(rs.getString("AUTHOR"));
                book.setPublisher(rs.getString("PUBLISHER"));
                book.setPublishDate(rs.getDate("PUBLISH_DATE"));
                book.setCategoryCode(rs.getString("CATEGORY_CODE"));
                book.setStatus(rs.getString("STATUS"));  // 대출 상태 추가
                bookList.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제
        }

        return bookList; // 페이징 처리된 도서 목록 반환
    }

    // 전체 도서 수를 구하는 메서드 (페이징에 사용)
    public int getTotalBooksCount() {
        String sql = "SELECT COUNT(*) FROM BOOKS";
        int count = 0;

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1); // 총 도서 수 가져오기
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제
        }

        return count;
    }

    // 카테고리별 도서 목록 페이징 처리 메서드
    public List<BookDTO> getBooksByCategoryWithPaging(String category, int currentPage, int pageSize) {
        return CategoryPaging(category, currentPage, pageSize); // 기존 메서드를 재활용
    }

    // 카테고리별 총 도서 수를 구하는 메서드
    public int getTotalBooksCountByCategory(String category) {
        return totalBooksCategory(category); // 기존 메서드를 재활용
    }

    // 도서 검색 쿼리 (STATUS 포함)
    public List<BookDTO> searchBooks(String keyword, int pageNum, int pageSize) {
        List<BookDTO> bookList = new ArrayList<>();
        int startRow = (pageNum - 1) * pageSize + 1;
        int endRow = pageNum * pageSize;

        String sql = "SELECT * FROM ( " +
                "  SELECT BOOK_NO, TITLE, AUTHOR, PUBLISHER, PUBLISH_DATE, CATEGORY_CODE, STATUS, ROWNUM AS RN " +
                "  FROM BOOKS " +
                "  WHERE (TITLE LIKE ? OR AUTHOR LIKE ? OR PUBLISHER LIKE ? OR CATEGORY_CODE LIKE ?) " +
                "  AND ROWNUM <= ? " +
                ") WHERE RN >= ?";

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            String searchKeyword = "%" + keyword + "%";
            pstmt.setString(1, searchKeyword);
            pstmt.setString(2, searchKeyword);
            pstmt.setString(3, searchKeyword);
            pstmt.setString(4, searchKeyword);
            pstmt.setInt(5, endRow);
            pstmt.setInt(6, startRow);

            rs = pstmt.executeQuery();

            // 결과를 BookDTO 객체로 변환하여 리스트에 추가
            while (rs.next()) {
                BookDTO book = new BookDTO();
                book.setBookNo(rs.getString("BOOK_NO"));
                book.setTitle(rs.getString("TITLE"));
                book.setAuthor(rs.getString("AUTHOR"));
                book.setPublisher(rs.getString("PUBLISHER"));
                book.setPublishDate(rs.getDate("PUBLISH_DATE"));
                book.setCategoryCode(rs.getString("CATEGORY_CODE"));
                book.setStatus(rs.getString("STATUS")); // 대출 상태 추가
                bookList.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return bookList;
    }

    // 검색된 총 도서 수 계산
    public int getTotalSearchResults(String keyword) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM BOOKS WHERE TITLE LIKE ? OR AUTHOR LIKE ? OR PUBLISHER LIKE ? OR CATEGORY_CODE LIKE ?";

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            String searchKeyword = "%" + keyword + "%";
            pstmt.setString(1, searchKeyword);
            pstmt.setString(2, searchKeyword);
            pstmt.setString(3, searchKeyword);
            pstmt.setString(4, searchKeyword);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return total;
    }

    // 도서 추가 메서드
    public void addBook(BookDTO bookDTO) {
        // 시퀀스에서 생성된 번호 앞에 'BA00'을 붙여서 BOOK_NO 생성
        String sql = "INSERT INTO BOOKS (BOOK_NO, TITLE, AUTHOR, STATUS, PUBLISHER, PUBLISH_DATE, CATEGORY_CODE) " +
                "VALUES (CONCAT('BA00', BOOK_NO_SEQ.NEXTVAL), ?, ?, ?, ?, ?, ?)";

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, bookDTO.getTitle());
            pstmt.setString(2, bookDTO.getAuthor());
            pstmt.setString(3, bookDTO.getStatus());
            pstmt.setString(4, bookDTO.getPublisher());
            pstmt.setDate(5, bookDTO.getPublishDate());
            pstmt.setString(6, bookDTO.getCategoryCode());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("도서가 성공적으로 추가되었습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제
        }
    }

    // 도서 수정 메서드
    public void updateBook(BookDTO book) {
        String sql = "UPDATE BOOKS SET TITLE = ?, AUTHOR = ?, STATUS = ?, PUBLISHER = ?, PUBLISH_DATE = ?, CATEGORY_CODE = ? " +
                "WHERE BOOK_NO = ?";

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getStatus());
            pstmt.setString(4, book.getPublisher());
            pstmt.setDate(5, new java.sql.Date(book.getPublishDate().getTime())); // Date 변환
            pstmt.setString(6, book.getCategoryCode());
            pstmt.setString(7, book.getBookNo());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("도서 정보가 성공적으로 수정되었습니다.");
            } else {
                System.out.println("도서 수정에 실패했습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    // 도서 삭제 메서드
    public void deleteBook(String bookNo) {
        String sql = "DELETE FROM BOOKS WHERE BOOK_NO = ?";

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("도서가 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("해당 도서번호를 가진 도서가 없습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // 도서 번호 조회
    public BookDTO getBookByBookNo(String bookNo) {
        BookDTO bookDTO = null;
        String sql = "SELECT * FROM BOOKS WHERE BOOK_NO = ?";

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                bookDTO = new BookDTO();
                bookDTO.setBookNo(rs.getString("BOOK_NO"));
                bookDTO.setTitle(rs.getString("TITLE"));
                bookDTO.setAuthor(rs.getString("AUTHOR"));
                bookDTO.setPublisher(rs.getString("PUBLISHER"));
                bookDTO.setPublishDate(rs.getDate("PUBLISH_DATE"));
                bookDTO.setCategoryCode(rs.getString("CATEGORY_CODE"));
                bookDTO.setStatus(rs.getString("STATUS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return bookDTO;
    }

    // 책 상태 확인 메서드 추가
    public String getBookStatus(String bookNo) {
        String sql = "SELECT STATUS FROM BOOKS WHERE BOOK_NO = ?";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("STATUS"); // 책의 상태 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null; // 상태를 찾지 못할 경우 null 반환
    }




}
