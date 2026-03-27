package BookRidgeDAO.UtilitySQL;

import BookRidgeDTO.WishBookDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishBookSQL extends ResourcesSQL {

    // 희망 도서 신청 SQL 메서드
    public void insertWishBook(WishBookDTO wishBook) {
        String sql = "INSERT INTO WISH_BOOKS (WISH_BOOK_ID, M_NO, TITLE, AUTHOR, PUBLISHER, REQUEST_DATE, STATUS) " +
                "VALUES (WISH_BOOK_SEQ.NEXTVAL, ?, ?, ?, ?, SYSTIMESTAMP, '대기')";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, wishBook.getMemberNo());  // M_NO
            pstmt.setString(2, wishBook.getTitle());   // TITLE
            pstmt.setString(3, wishBook.getAuthor());  // AUTHOR
            pstmt.setString(4, wishBook.getPublisher()); // PUBLISHER

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("희망 도서 신청 성공!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // 대기 중인 희망 도서 목록을 조회하는 메서드
    public List<WishBookDTO> getPendingWishBooks() {
        String sql = "SELECT WISH_BOOK_ID, M_NO, TITLE, AUTHOR, PUBLISHER, REQUEST_DATE, STATUS, ADMIN_RESPONSE, ADMIN_NO " +
                "FROM WISH_BOOKS " +
                "WHERE STATUS = '대기'";

        List<WishBookDTO> wishBookList = new ArrayList<>();

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                WishBookDTO wishBook = new WishBookDTO();
                wishBook.setWishBookId(rs.getInt("WISH_BOOK_ID"));
                wishBook.setMemberNo(rs.getInt("M_NO"));
                wishBook.setTitle(rs.getString("TITLE"));
                wishBook.setAuthor(rs.getString("AUTHOR"));
                wishBook.setPublisher(rs.getString("PUBLISHER"));
                wishBook.setRequestDate(rs.getTimestamp("REQUEST_DATE"));
                wishBook.setStatus(rs.getString("STATUS"));
                wishBook.setAdminResponse(rs.getString("ADMIN_RESPONSE"));
                wishBook.setAdminNo(rs.getInt("ADMIN_NO"));

                wishBookList.add(wishBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return wishBookList;
    }

    // 희망 도서 상태 업데이트 메서드
    public void updateWishBookStatus(int wishBookId, String status, String adminResponse, int adminNo) {
        String sql = "UPDATE WISH_BOOKS SET STATUS = ?, ADMIN_RESPONSE = ?, ADMIN_NO = ? WHERE WISH_BOOK_ID = ?";

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, adminResponse);
            pstmt.setInt(3, adminNo);
            pstmt.setInt(4, wishBookId);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("희망 도서 상태가 성공적으로 업데이트되었습니다.");
            } else {
                System.out.println("희망 도서 상태 업데이트에 실패했습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public List<WishBookDTO> getWishBookHistory(int memberNo) {
        String sql = "SELECT WISH_BOOK_ID, M_NO, TITLE, AUTHOR, PUBLISHER, REQUEST_DATE, STATUS, ADMIN_RESPONSE " +
                "FROM WISH_BOOKS WHERE M_NO = ?";
        List<WishBookDTO> wishBookList = new ArrayList<>();

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberNo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                WishBookDTO wishBook = new WishBookDTO();
                wishBook.setWishBookId(rs.getInt("WISH_BOOK_ID"));
                wishBook.setMemberNo(rs.getInt("M_NO"));
                wishBook.setTitle(rs.getString("TITLE"));
                wishBook.setAuthor(rs.getString("AUTHOR"));
                wishBook.setPublisher(rs.getString("PUBLISHER"));
                wishBook.setRequestDate(rs.getTimestamp("REQUEST_DATE"));
                wishBook.setStatus(rs.getString("STATUS"));
                wishBook.setAdminResponse(rs.getString("ADMIN_RESPONSE"));
                wishBookList.add(wishBook);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return wishBookList;
    }

}
