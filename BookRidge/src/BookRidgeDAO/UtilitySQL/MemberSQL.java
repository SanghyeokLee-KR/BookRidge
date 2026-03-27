package BookRidgeDAO.UtilitySQL;

import BookRidgeDTO.MemberDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static BookRidgeUtility.ColorUtility.*;

public class MemberSQL extends ResourcesSQL {

    // 회원가입 메서드
    public void registerMember(MemberDTO memberDTO) {
        // 아이디 중복 검사
        if (isIdDuplicate(memberDTO.getmId())) {
            System.out.println("입력하신 아이디는 이미 사용 중입니다. 다른 아이디를 입력해주세요.");
            return; // 중복된 경우 회원가입 진행하지 않음
        }

        // 회원 번호 시퀀스 생성
        memberDTO.setmNo(generateMemberNo());

        // 현재 날짜를 'YYYY-MM-DD' 형식으로 설정
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        memberDTO.setmEnrollDate(formattedDate); // 날짜 설정

        // 기본 값 설정
        memberDTO.setmRental("Y");  // 대출 가능 여부 기본값 설정
        memberDTO.setmStatus("정상"); // 회원 상태 기본값 설정

        // 데이터베이스 연결 및 회원 등록
        String sql = "INSERT INTO MEMBER (M_NO, M_ID, M_PW, M_NAME, M_GENDER, M_PHONE, M_EMAIL, M_ENROLLDATE, M_IS_RENTAL, M_STATUS) " + "VALUES (?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";
        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);

            // PreparedStatement에 각 파라미터 설정
            pstmt.setInt(1, memberDTO.getmNo());
            pstmt.setString(2, memberDTO.getmId());
            pstmt.setString(3, memberDTO.getmPw());
            pstmt.setString(4, memberDTO.getmName());
            pstmt.setString(5, memberDTO.getmGender());
            pstmt.setString(6, memberDTO.getmPhone());
            pstmt.setString(7, memberDTO.getmEmail());
            pstmt.setString(8, memberDTO.getmEnrollDate());
            pstmt.setString(9, memberDTO.getmRental());
            pstmt.setString(10, memberDTO.getmStatus());

            int result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("회원가입이 성공적으로 완료되었습니다.");
            } else {
                System.out.println("회원가입에 실패하였습니다. 다시 시도해주세요.");
            }
        } catch (SQLException e) {
            System.out.println("데이터베이스 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeResources(); // 자원 해제 및 연결 종료
        }
    }

    // 아이디 중복 검사 메서드
    public boolean isIdDuplicate(String memberId) {
        String sql = "SELECT COUNT(*) FROM MEMBER WHERE M_ID = ?";
        boolean isDuplicate = false;

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                isDuplicate = rs.getInt(1) > 0; // 중복된 아이디가 있으면 true
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("아이디 중복 검사 중 오류 발생: " + e.getMessage());
        } finally {
            closeResources(); // 자원 해제
        }

        return isDuplicate;
    }

    // 로그인 메서드
    public MemberDTO loginMember(MemberDTO member) {
        String sql = "SELECT * FROM MEMBER WHERE M_ID = ? AND M_STATUS != '탈퇴'";
        MemberDTO loggedInMember = null;

        try {
            connect(); // 데이터베이스 연결

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getmId());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // 아이디로 사용자를 찾았을 때 비밀번호 비교
                String storedPassword = rs.getString("M_PW"); // 컬럼 이름 확인 및 수정
                if (storedPassword.equals(member.getmPw())) {
                    // 로그인 성공 시 MemberDTO 객체에 데이터 설정
                    loggedInMember = new MemberDTO();
                    loggedInMember.setmNo(rs.getInt("M_NO")); // 컬럼 이름 확인 및 수정
                    loggedInMember.setmId(rs.getString("M_ID")); // 컬럼 이름 확인 및 수정
                    loggedInMember.setmPw(storedPassword);
                    loggedInMember.setmName(rs.getString("M_NAME")); // 컬럼 이름 확인 및 수정
                    loggedInMember.setmGender(rs.getString("M_GENDER")); // 컬럼 이름 확인 및 수정
                    loggedInMember.setmPhone(rs.getString("M_PHONE")); // 컬럼 이름 확인 및 수정
                    loggedInMember.setmPhone(rs.getString("M_EMAIL")); // 컬럼 이름 확인 및 수정
                    loggedInMember.setmEnrollDate(rs.getString("M_ENROLLDATE")); // 컬럼 이름 확인 및 수정
                    loggedInMember.setmRental(rs.getString("M_IS_RENTAL")); // 컬럼 이름 확인 및 수정
                    loggedInMember.setmStatus(rs.getString("M_STATUS")); // 컬럼 이름 확인 및 수정

                } else {
                    System.out.println("비밀번호가 잘못되었습니다.");
                }
            } else {
                System.out.println("아이디가 존재하지 않습니다.");
            }

        } catch (Exception e) {
            System.out.println("로그인 과정 중 오류 발생: " + e.getMessage());
            throw new RuntimeException(e);

        } finally {
            // 자원 해제 및 연결 종료
            closeResources();
        }
        return loggedInMember; // 로그인 성공 시 사용자 정보 반환, 실패 시 null 반환
    }

    // 회원가입 시퀸스 추가
    public int generateMemberNo() {
        int memberNo = 0;
        String sql = "SELECT MEMBER_SEQ.NEXTVAL FROM DUAL"; // 시퀀스에서 다음 값을 가져옴

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            // 결과 처리
            if (rs.next()) {
                memberNo = rs.getInt(1); // 시퀀스에서 가져온 번호
                System.out.println("생성된 회원 번호: " + memberNo);
            } else {
                System.out.println("시퀀스 값을 가져오지 못했습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("회원 번호 생성 중 오류 발생: " + e.getMessage());
        } finally {
            // 자원 해제
            closeResources();
        }

        return memberNo;
    }

    // 내 정보 조회 SQL 메서드
    public void viewMyInfo(String memberId) {
        String sql = "SELECT M_ID, M_NAME, M_GENDER, M_PHONE, M_EMAIL, TO_CHAR(M_ENROLLDATE, 'YYYY-MM-DD') AS M_ENROLLDATE, M_IS_RENTAL " +
                "FROM MEMBER WHERE M_ID = ?";
        MemberDTO memberInfo = null;

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                memberInfo = new MemberDTO();
                memberInfo.setmId(rs.getString("M_ID"));
                memberInfo.setmName(rs.getString("M_NAME"));
                memberInfo.setmGender(rs.getString("M_GENDER"));
                memberInfo.setmPhone(rs.getString("M_PHONE"));
                memberInfo.setmEmail(rs.getString("M_EMAIL"));
                memberInfo.setmEnrollDate(rs.getString("M_ENROLLDATE"));
                memberInfo.setmRental(rs.getString("M_IS_RENTAL"));

                // 정보 출력
                System.out.println(CYAN + "┌─────────────────────────🙋‍♂️ 내 정보 조회─────────────────────────┐" + RESET);
                System.out.printf("  " + YELLOW + "아이디     " + GREEN + ">> " + WHITE + "%s%n", memberInfo.getmId());
                System.out.printf("  " + YELLOW + "이름       " + GREEN + ">> " + WHITE + "%s%n", memberInfo.getmName());
                System.out.printf("  " + YELLOW + "성별       " + GREEN + ">> " + WHITE + "%s%n", memberInfo.getmGender());
                System.out.printf("  " + YELLOW + "휴대전화   " + GREEN + ">> " + WHITE + "%s%n", memberInfo.getmPhone());
                System.out.printf("  " + YELLOW + "이메일     " + GREEN + ">> " + WHITE + "%s%n", memberInfo.getmEmail());
                System.out.printf("  " + YELLOW + "가입일     " + GREEN + ">> " + WHITE + "%s%n", memberInfo.getmEnrollDate());
                System.out.printf("  " + YELLOW + "연체 가능 여부 " + GREEN + ">> " + (("Y".equals(memberInfo.getmRental())) ? BLUE + "가능" : RED + "불가능") + RESET + "%n");
                System.out.println(CYAN + "└────────────────────────────────────────────────────────────────┘\n" + RESET);
            } else {
                System.out.println(RED + "회원 정보를 찾을 수 없습니다." + RESET);
            }

        } catch (SQLException e) {
            System.out.println(RED + "회원 정보 조회 중 오류 발생: " + e.getMessage() + RESET);
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제 및 연결 종료
        }
    }

    // 내 정보 수정 SQL 메서드
    public boolean updateMyInfo(MemberDTO member) {
        String sql = "UPDATE MEMBER SET M_PW = ?, M_PHONE = ? WHERE M_ID = ?";

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getmPw()); // 새로운 비밀번호
            pstmt.setString(2, member.getmPhone()); // 새로운 전화번호
            pstmt.setString(3, member.getmId());

            int result = pstmt.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            System.out.println("회원 정보 수정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            closeResources(); // 자원 해제 및 연결 종료
        }
    }

    // 회원 탈퇴 SQL 메서드 (M_STATUS를 '탈퇴'로 변경)
    public boolean deleteMyAccount(String memberId) {
        String sql = "UPDATE MEMBER SET M_STATUS = '탈퇴' WHERE M_ID = ?";

        try {
            connect(); // 데이터베이스 연결
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            int result = pstmt.executeUpdate();

            return result > 0; // 업데이트 성공 시 true 반환

        } catch (SQLException e) {
            System.out.println("회원 탈퇴 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            closeResources(); // 자원 해제 및 연결 종료
        }
    }
}
