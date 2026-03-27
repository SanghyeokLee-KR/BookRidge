package BookRidgeDAO.UtilitySQL;

import BookRidgeDTO.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanManageSQL extends ResourcesSQL {

    // 사용자가 이미 대출 중인 도서인지 확인하는 메서드
    public boolean isBookAlreadyLoanedByMember(String bookNo, int memberNo) {
        String sql = "SELECT COUNT(*) FROM LOANS WHERE BOOK_NO = ? AND M_NO = ? AND RETURN_DATE IS NULL";
        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);
            pstmt.setInt(2, memberNo);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true; // 이미 대출 중인 도서일 경우
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제
        }
        return false; // 대출 중이 아니거나 오류가 발생했을 경우 false 반환
    }

    // 사용자 대출권수 확인 메서드
    public int getLoanCountByMember(int mNo) {
        String sql = "SELECT COUNT(*) AS LOAN_COUNT FROM LOANS WHERE M_NO = ? AND RETURN_DATE IS NULL";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, mNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("LOAN_COUNT"); // 대출 중인 도서 개수 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return 0; // 오류 발생 시 기본적으로 0 반환
    }

    // 대출 가능 상태인지 보는 메서드
    public boolean isBookAvailable(String bookNo) {
        String sql = "SELECT STATUS FROM BOOKS WHERE BOOK_NO = ?";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String status = rs.getString("STATUS");
                return "대출가능".equals(status); // 도서가 "대출가능" 상태인지 확인
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false; // 대출 불가능하면 false 반환
    }

    // 도서 대출 처리
    public boolean loanBook(LoanDTO loanDTO) {
        String sql = "INSERT INTO LOANS (LOAN_NO, M_NO, BOOK_NO, LOAN_DATE, DUE_DATE, IS_OVERDUE) " +
                "VALUES (LOAN_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, loanDTO.getmNo()); // 회원 번호
            pstmt.setString(2, loanDTO.getBookNo()); // 도서 번호
            pstmt.setDate(3, loanDTO.getLoanDate()); // 대출일
            pstmt.setDate(4, loanDTO.getDueDate()); // 반납 기한일
            pstmt.setString(5, loanDTO.getIsOverdue()); // 연체 여부

            int result = pstmt.executeUpdate();

            // 도서 상태를 '대출중'으로 변경
            if (result > 0) {
                return updateBookStatusToLoaned(loanDTO.getBookNo());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
        return false;
    }

    // book 테이블 상태 업데이트 함수
    public boolean updateBookStatusToLoan(String bookNo) {
        String sql = "UPDATE BOOKS SET STATUS = '대출중' WHERE BOOK_NO = ?";

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);

            int result = pstmt.executeUpdate();
            return result > 0; // 업데이트된 행의 개수가 0보다 크면 성공

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(); // 자원 해제
        }
    }

    // 기간 업데이트
    public boolean updateOverdueStatus() {
        String sql = "UPDATE LOANS " +
                "SET IS_OVERDUE = 'Y' " +
                "WHERE DUE_DATE < SYSDATE AND IS_OVERDUE = 'N'";

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(); // 자원 해제
        }
    }

    // 도서가 회원이 대출한 도서인지 확인하는 메서드
    public boolean isBookLoanedToMember(String bookNo, int memberNo) {
        String sql = "SELECT COUNT(*) FROM LOANS WHERE BOOK_NO = ? AND M_NO = ? AND RETURN_DATE IS NULL";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);
            pstmt.setInt(2, memberNo);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true; // 대출 중인 도서일 경우
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    // 대출 정보를 가져오는 메서드
    public LoanDTO getLoanInfo(String bookNo, int memberNo) {
        String sql = "SELECT * FROM LOANS WHERE BOOK_NO = ? AND M_NO = ? AND RETURN_DATE IS NULL";
        LoanDTO loanDTO = null;
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);
            pstmt.setInt(2, memberNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                loanDTO = new LoanDTO();
                loanDTO.setLoanNo(rs.getInt("LOAN_NO"));
                loanDTO.setMemberNo(rs.getInt("M_NO"));
                loanDTO.setBookNo(rs.getString("BOOK_NO"));
                loanDTO.setLoanDate(rs.getDate("LOAN_DATE"));
                loanDTO.setDueDate(rs.getDate("DUE_DATE"));
                loanDTO.setReturnDate(rs.getDate("RETURN_DATE"));
                loanDTO.setIsOverdue(rs.getString("IS_OVERDUE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return loanDTO;
    }

    // 도서 반납을 처리하는 메서드
    public boolean returnBook(LoanDTO loanDTO) {
        String sql = "UPDATE LOANS SET RETURN_DATE = ?, IS_OVERDUE = ? WHERE LOAN_NO = ?";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, loanDTO.getReturnDate());
            pstmt.setString(2, loanDTO.getIsOverdue());
            pstmt.setInt(3, loanDTO.getLoanNo());

            int result = pstmt.executeUpdate();
            return result > 0; // 업데이트가 성공했는지 확인
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // 도서 상태를 '대출가능'으로 변경하는 메서드
    public boolean updateBookStatusToAvailable(String bookNo) {
        String sql = "UPDATE BOOKS SET STATUS = '대출가능' WHERE BOOK_NO = ?";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);

            int result = pstmt.executeUpdate();
            return result > 0; // 업데이트 성공 여부
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    public List<LoanDTO> getLoanedBooksByMember(int memberNo) {
        List<LoanDTO> loanedBooks = new ArrayList<>();
        String sql = "SELECT * FROM LOANS WHERE M_NO = ? AND RETURN_DATE IS NULL"; // 반납되지 않은 도서만 조회

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberNo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LoanDTO loanDTO = new LoanDTO();
                loanDTO.setLoanNo(rs.getInt("LOAN_NO"));
                loanDTO.setMemberNo(rs.getInt("M_NO"));
                loanDTO.setBookNo(rs.getString("BOOK_NO"));
                loanDTO.setLoanDate(rs.getDate("LOAN_DATE"));
                loanDTO.setDueDate(rs.getDate("DUE_DATE"));
                loanDTO.setIsOverdue(rs.getString("IS_OVERDUE"));
                loanedBooks.add(loanDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return loanedBooks; // 대출된 도서 목록 반환
    }

    // 예약자 정보 조회
    public ReservationDTO getFirstReservation(String bookNo) {
        String sql = "SELECT * FROM RESERVATIONS WHERE BOOK_NO = ? AND STATUS = '예약중' ORDER BY RESERVATION_DATE ASC";

        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                ReservationDTO reservation = new ReservationDTO();
                reservation.setReservationNo(rs.getInt("RESERVATION_NO"));
                reservation.setMemberNo(rs.getInt("M_NO"));
                reservation.setBookNo(rs.getString("BOOK_NO"));
                reservation.setReservationDate(rs.getDate("RESERVATION_DATE"));
                reservation.setExpirationDate(rs.getDate("EXPIRATION_DATE"));
                reservation.setStatus(rs.getString("STATUS"));
                return reservation;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return null;
    }

    // 도서를 예약하는 메서드
    public boolean reserveBook(ReservationDTO reservationDTO) {
        String sql = "INSERT INTO RESERVATIONS (RESERVATION_NO, M_NO, BOOK_NO, RESERVATION_DATE, STATUS) " +
                "VALUES (RESERVATION_SEQ.NEXTVAL, ?, ?, ?, '예약중')";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, reservationDTO.getMemberNo());
            pstmt.setString(2, reservationDTO.getBookNo());
            pstmt.setDate(3, reservationDTO.getReservationDate());

            int result = pstmt.executeUpdate();

            // 도서 상태를 '예약중'으로 변경
            if (result > 0) {
                return updateBookStatusToReserved(reservationDTO.getBookNo());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    // 예약중을 확인하는 메서드
    public boolean isBookAlreadyReserved(String bookNo) {
        String sql = "SELECT COUNT(*) FROM RESERVATIONS WHERE BOOK_NO = ? AND STATUS = '예약중'";

        try {
            connect();  // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true;  // 예약중인 도서가 존재할 경우 true 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();  // 자원 해제
        }

        return false;  // 예약중인 도서가 없을 경우 false 반환
    }

    // 예약 취소 메서드
    public boolean cancelReservation(int reservationNo, int memberNo) {
        String sql = "UPDATE RESERVATIONS SET STATUS = '취소' WHERE RESERVATION_NO = ? AND M_NO = ? AND STATUS = '예약중'";

        try {
            connect();  // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, reservationNo); // 예약 번호를 바인딩
            pstmt.setInt(2, memberNo);      // 회원 번호를 바인딩
            int result = pstmt.executeUpdate();

            return result > 0;  // 업데이트된 행이 있으면 true 반환

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();  // 자원 해제
        }
    }

    // 예약한 도서 조회
    public List<ReservationDTO> getReservationsByMember(int memberNo) {
        List<ReservationDTO> reservations = new ArrayList<>();
        String sql = "SELECT * FROM RESERVATIONS WHERE M_NO = ? AND STATUS = '예약중'";

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberNo); // 회원 번호 바인딩
            rs = pstmt.executeQuery();

            // 결과를 ReservationDTO 객체로 변환하여 리스트에 추가
            while (rs.next()) {
                ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO.setReservationNo(rs.getInt("RESERVATION_NO"));
                reservationDTO.setMemberNo(rs.getInt("M_NO"));
                reservationDTO.setBookNo(rs.getString("BOOK_NO"));
                reservationDTO.setReservationDate(rs.getDate("RESERVATION_DATE"));
                reservationDTO.setExpirationDate(rs.getDate("EXPIRATION_DATE"));
                reservationDTO.setStatus(rs.getString("STATUS"));
                reservations.add(reservationDTO);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제
        }

        return reservations; // 예약된 도서 목록 반환
    }

    // 도서 상태를 '예약중'으로 변경하는 메서드
    public boolean updateBookStatusToReserved(String bookNo) {
        String sql = "UPDATE BOOKS SET STATUS = '예약중' WHERE BOOK_NO = ?";

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);

            int result = pstmt.executeUpdate();
            return result > 0; // 업데이트 성공 여부 반환
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(); // 자원 해제
        }
    }

    // 도서 상태를 '대출중'으로 변경하는 메서드
    public boolean updateBookStatusToLoaned(String bookNo) {
        String sql = "UPDATE BOOKS SET STATUS = '대출중' WHERE BOOK_NO = ?";

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bookNo);

            int result = pstmt.executeUpdate();
            return result > 0; // 업데이트 성공 여부 반환
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(); // 자원 해제
        }
    }

    // 예약을 대출로 전환하는 메서드
    public boolean convertReservationToLoan(ReservationDTO reservation) {
        String loanInsertSql = "INSERT INTO LOANS (LOAN_NO, M_NO, BOOK_NO, LOAN_DATE, DUE_DATE, IS_OVERDUE) " +
                "VALUES (LOAN_SEQ.NEXTVAL, ?, ?, ?, ?, 'N')";
        String reservationUpdateSql = "UPDATE RESERVATIONS SET STATUS = '완료' WHERE RESERVATION_NO = ?";
        String updateBookStatusSql = "UPDATE BOOKS SET STATUS = '대출중' WHERE BOOK_NO = ?";

        try {
            connect(); // 데이터베이스 연결

            // 트랜잭션 시작
            con.setAutoCommit(false);

            // 대출 정보 삽입
            pstmt = con.prepareStatement(loanInsertSql);
            pstmt.setInt(1, reservation.getMemberNo()); // 회원 번호
            pstmt.setString(2, reservation.getBookNo()); // 도서 번호
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis()); // 현재 날짜
            pstmt.setDate(3, currentDate); // 대출일

            // 반납 기한일을 14일 후로 설정
            long twoWeeksInMillis = 14L * 24 * 60 * 60 * 1000; // 14일 밀리초
            java.sql.Date dueDate = new java.sql.Date(System.currentTimeMillis() + twoWeeksInMillis); // 반납 기한일
            pstmt.setDate(4, dueDate);

            System.out.println("대출일: " + currentDate + ", 반납 기한일: " + dueDate); // 디버깅용 로그 출력

            int result = pstmt.executeUpdate();

            if (result == 0) {
                con.rollback(); // 삽입 실패 시 롤백
                return false;
            }

            // 예약 상태 업데이트
            pstmt = con.prepareStatement(reservationUpdateSql);
            pstmt.setInt(1, reservation.getReservationNo());
            result = pstmt.executeUpdate();

            if (result == 0) {
                con.rollback(); // 업데이트 실패 시 롤백
                return false;
            }

            // 도서 상태를 '대출중'으로 변경
            pstmt = con.prepareStatement(updateBookStatusSql);
            pstmt.setString(1, reservation.getBookNo());
            result = pstmt.executeUpdate();

            if (result == 0) {
                con.rollback(); // 상태 업데이트 실패 시 롤백
                return false;
            }

            con.commit(); // 모든 작업 성공 시 커밋

            // 알림 생성 - 대출 완료 알림
            AlertDTO alertDTO = new AlertDTO();
            alertDTO.setMemberNo(reservation.getMemberNo());
            alertDTO.setAlertType("대출");
            alertDTO.setAlertMessage("도서 " + reservation.getBookNo() + "의 대출이 완료되었습니다.");

            AlertSQL alertSQL = new AlertSQL();
            alertSQL.createAlert(alertDTO);  // 알림 생성

            System.out.println("알림이 생성되었습니다: 도서 " + reservation.getBookNo() + " 대출 완료.");

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback(); // 예외 발생 시 롤백
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            closeResources(); // 자원 해제
        }
        return false;
    }


    // 예약된 도서를 대출로 전환하는 메서드
    public boolean checkAndConvertReservation(String bookNo) {
        // 첫 번째 예약자를 가져옴
        ReservationDTO reservationDTO = getFirstReservation(bookNo);

        if (reservationDTO != null) {
            // 예약을 대출로 전환
            return convertReservationToLoan(reservationDTO);
        }

        return false; // 예약자가 없으면 false 반환
    }

    public int calculateOverdueDays(int memberNo) {
        int overdueDays = 0;

        // 여기에 실제 연체일 계산 로직을 추가
        // 예시: 데이터베이스에서 연체된 도서를 조회하고, 반납일과 현재 날짜를 비교하여 연체일 수 계산
        String sql = "SELECT DUE_DATE, RETURN_DATE FROM LOANS WHERE M_NO = ? AND RETURN_DATE IS NOT NULL";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberNo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Date dueDate = rs.getDate("DUE_DATE");
                Date returnDate = rs.getDate("RETURN_DATE");

                if (returnDate != null && returnDate.after(dueDate)) {
                    long diffInMillis = returnDate.getTime() - dueDate.getTime();
                    int daysOverdue = (int) (diffInMillis / (1000 * 60 * 60 * 24)); // 밀리초를 일수로 변환
                    overdueDays += daysOverdue;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return overdueDays;
    }


    // 대출 제한 여부 확인 메서드
    public boolean canMemberLoan(int memberNo) {
        String sql = "SELECT OVERDUE_DAYS, LAST_OVERDUE_RESET_DATE FROM MEMBER WHERE M_NO = ?";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberNo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int overdueDays = rs.getInt("OVERDUE_DAYS");
                Date lastOverdueResetDate = rs.getDate("LAST_OVERDUE_RESET_DATE");

                // 만약 연체 일수가 0이면 대출 가능
                if (overdueDays == 0) {
                    return true;
                }

                // 연체가 발생한 이후로 overdueDays만큼의 시간이 지났는지 확인
                Date currentDate = new Date(System.currentTimeMillis());
                long diffInMillis = currentDate.getTime() - lastOverdueResetDate.getTime();
                int daysSinceReset = (int) (diffInMillis / (1000 * 60 * 60 * 24));

                if (daysSinceReset >= overdueDays) {
                    // 연체 일수가 지났다면 연체 기록을 초기화하고 대출 가능
                    resetOverdueDays(memberNo);
                    return true;
                } else {
                    System.out.println("연체로 인해 " + (overdueDays - daysSinceReset) + "일 동안 대출이 불가능합니다.");
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return false;
    }

    // 연체 기록 초기화 메서드
    private void resetOverdueDays(int memberNo) {
        String sql = "UPDATE MEMBER SET OVERDUE_DAYS = 0, LAST_OVERDUE_RESET_DATE = ? WHERE M_NO = ?";
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, new Date(System.currentTimeMillis()));
            pstmt.setInt(2, memberNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // 전체 인기 도서 조회 메서드 (상위 10권만)
    public List<BookDTO> getAllPopularBooks() {
        String sql = "SELECT * FROM ( " +
                "SELECT B.BOOK_NO, B.TITLE, COUNT(L.BOOK_NO) AS LOAN_COUNT " +
                "FROM LOANS L " +
                "JOIN BOOKS B ON L.BOOK_NO = B.BOOK_NO " +
                "WHERE TO_CHAR(L.LOAN_DATE, 'YYYYMM') = TO_CHAR(SYSDATE, 'YYYYMM') " +  // 이번 달 대출만 조회
                "GROUP BY B.BOOK_NO, B.TITLE " +
                "ORDER BY LOAN_COUNT DESC " +
                ") WHERE ROWNUM <= 10"; // 상위 10개만 선택
        return executeBookQuery(sql);
    }

    // 남성 회원 인기 도서 조회 메서드 (상위 10권만)
    public List<BookDTO> getMalePopularBooks() {
        String sql = "SELECT * FROM ( " +
                "SELECT B.BOOK_NO, B.TITLE, COUNT(L.BOOK_NO) AS LOAN_COUNT " +
                "FROM LOANS L " +
                "JOIN BOOKS B ON L.BOOK_NO = B.BOOK_NO " +
                "JOIN MEMBER M ON L.M_NO = M.M_NO " +
                "WHERE M.M_GENDER = '남성' " +
                "AND TO_CHAR(L.LOAN_DATE, 'YYYYMM') = TO_CHAR(SYSDATE, 'YYYYMM') " +  // 이번 달 대출만 조회
                "GROUP BY B.BOOK_NO, B.TITLE " +
                "ORDER BY LOAN_COUNT DESC " +
                ") WHERE ROWNUM <= 10"; // 상위 10개만 선택
        return executeBookQuery(sql);
    }

    // 여성 회원 인기 도서 조회 메서드 (상위 10권만)
    public List<BookDTO> getFemalePopularBooks() {
        String sql = "SELECT * FROM ( " +
                "SELECT B.BOOK_NO, B.TITLE, COUNT(L.BOOK_NO) AS LOAN_COUNT " +
                "FROM LOANS L " +
                "JOIN BOOKS B ON L.BOOK_NO = B.BOOK_NO " +
                "JOIN MEMBER M ON L.M_NO = M.M_NO " +
                "WHERE M.M_GENDER = '여성' " +
                "AND TO_CHAR(L.LOAN_DATE, 'YYYYMM') = TO_CHAR(SYSDATE, 'YYYYMM') " +  // 이번 달 대출만 조회
                "GROUP BY B.BOOK_NO, B.TITLE " +
                "ORDER BY LOAN_COUNT DESC " +
                ") WHERE ROWNUM <= 10"; // 상위 10개만 선택
        return executeBookQuery(sql);
    }

    // 공통 메서드: SQL 쿼리 실행 후 BookDTO 리스트 반환
    private List<BookDTO> executeBookQuery(String sql) {
        List<BookDTO> books = new ArrayList<>();
        try {
            connect();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BookDTO book = new BookDTO();
                book.setBookNo(rs.getString("BOOK_NO"));
                book.setTitle(rs.getString("TITLE"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return books;
    }

    // 대출 현황 보고서 SQL
    public List<LoanDTO> getLoanStatus() {
        String sql = "SELECT BOOK_NO, M_NO, LOAN_DATE, DUE_DATE, RETURN_DATE " +
                "FROM LOANS " +
                "ORDER BY LOAN_DATE DESC";
        return executeLoanQuery(sql);
    }


    // 연체자 목록 보고서 SQL
    public List<MemberDTO> getOverdueMembers() {
        String sql = "SELECT L.LOAN_NO, L.M_NO, L.BOOK_NO, L.LOAN_DATE, L.DUE_DATE " +
                "FROM LOANS L " +
                "WHERE L.DUE_DATE < SYSDATE AND L.RETURN_DATE IS NULL";
        return executeMemberQuery(sql);
    }

    // 대출 현황 보고서 실행 메서드
    public List<LoanDTO> executeLoanQuery(String sql) {
        List<LoanDTO> loanList = new ArrayList<>();
        try {
            connect(); // DB 연결 메서드 호출
            pstmt = con.prepareStatement(sql); // 쿼리 준비
            rs = pstmt.executeQuery(); // 쿼리 실행

            while (rs.next()) {
                LoanDTO loan = new LoanDTO();
                loan.setBookNo(rs.getString("BOOK_NO"));
                loan.setMemberNo(rs.getInt("M_NO"));
                loan.setLoanDate(rs.getDate("LOAN_DATE"));
                loan.setDueDate(rs.getDate("DUE_DATE"));
                loan.setReturnDate(rs.getDate("RETURN_DATE"));
                loanList.add(loan); // 리스트에 대출 정보 추가
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제 메서드
        }
        return loanList;
    }

    // 연체자 목록 보고서 실행 메서드
    public List<MemberDTO> executeMemberQuery(String sql) {
        List<MemberDTO> memberList = new ArrayList<>();
        try {
            connect(); // DB 연결
            pstmt = con.prepareStatement(sql); // 쿼리 준비
            rs = pstmt.executeQuery(); // 쿼리 실행

            while (rs.next()) {
                MemberDTO member = new MemberDTO();
                member.setmNo(rs.getInt("M_NO"));
                member.setmName(rs.getString("M_NAME"));
                member.setmPhone(rs.getString("M_PHONE"));
                memberList.add(member); // 리스트에 회원 정보 추가
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제
        }
        return memberList;
    }


}
