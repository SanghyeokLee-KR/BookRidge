package BookRidgeUtility;

import BookRidgeDTO.LoanDTO;
import BookRidgeDTO.MemberDTO;
import jdk.swing.interop.SwingInterOpUtils;

import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Pattern;

public class MemberUtility extends ResourcesUtility {

    // 정규식 패턴 정의
    private static final String ID_PATTERN = "^[a-zA-Z0-9]{5,15}$";  // 아이디: 영문자와 숫자, 5~15자
    private static final String PW_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$";  // 비밀번호: 대소문자, 숫자, 특수문자 포함 최소 8자
    private static final String NAME_PATTERN = "^[가-힣]{2,5}$";  // 이름: 한글 2~5자
    private static final String PHONE_PATTERN = "^(01[0-9]\\d{7,8})|(0[2-6][0-9]\\d{7,8})$"; // 전화번호 : 숫자만 허용
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"; // 이메일 패턴

    // 로그인 메서드
    public MemberDTO MemberLogin() {
        try {
            // 사용자로부터 로그인 정보를 입력받음
            System.out.print("아이디를 입력하세요: ");
            String mId = sc.next();
            memberDTO.setmId(mId);

            System.out.print("비밀번호를 입력하세요: ");
            String mPw = sc.next();
            memberDTO.setmPw(mPw);

            // DAO를 통해 로그인 시도
            MemberDTO loggedInMember = memberSQL.loginMember(memberDTO);

            if (loggedInMember != null) {
                System.out.println("로그인 성공! " + loggedInMember.getmName() + "님 환영합니다.");

                // 로그인 성공 시 활동 로그 저장
                adminSQL.insertActivityLog(loggedInMember.getmNo(), "로그인", "회원 로그인 성공");

            } else {
                System.out.println("로그인에 실패했습니다. 아이디 또는 비밀번호를 확인해주세요.");

                int memberNo = adminSQL.getMemberNoById(mId); // 아이디로 회원 번호 조회

                if (memberNo != -1) {
                    // 아이디는 존재하지만 비밀번호가 틀린 경우
                    adminSQL.insertActivityLog(memberNo, "로그인 실패", "회원 로그인 실패 - 비밀번호 불일치");
                }
            }

            return loggedInMember; // 로그인 성공 시 로그인된 사용자 정보 반환, 실패 시 null 반환

        } catch (InputMismatchException e) {
            System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
            sc.nextLine(); // 잘못된 입력 버퍼 비우기
        } catch (Exception e) {
            System.out.println("예상치 못한 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // 예외 발생 시 null 반환
    }

    // 회원가입 메서드
    public void MemberJoin() {
        boolean validInput = false;

        while (!validInput) {
            try {
                // 사용자로부터 회원 정보를 입력 받음
                System.out.println("회원정보를 입력하세요");

                // 아이디 입력 및 검증
                System.out.println("\n아이디는 영문자와 숫자로 구성된 5~15자로 입력해주세요.");
                System.out.print("아이디 >> ");
                String mId = sc.next();
                if (!Pattern.matches(ID_PATTERN, mId)) {
                    throw new IllegalArgumentException("아이디는 영문자와 숫자로 구성된 5~15자로 입력해주세요.");
                }
                memberDTO.setmId(mId);

                // 비밀번호 입력 및 검증
                System.out.println("\n비밀번호는 대소문자, 숫자, 특수문자를 포함한 최소 8자로 입력해주세요.");
                System.out.print("비밀번호 >> ");
                String mPw = sc.next();
                if (!Pattern.matches(PW_PATTERN, mPw)) {
                    throw new IllegalArgumentException("비밀번호는 대소문자, 숫자, 특수문자를 포함한 최소 8자로 입력해주세요.");
                }
                memberDTO.setmPw(mPw);

                // 이름 입력 및 검증
                System.out.println("\n이름은 한글 2~5자로 입력해주세요");
                System.out.print("이름 >> ");
                String mName = sc.next();
                if (!Pattern.matches(NAME_PATTERN, mName)) {
                    throw new IllegalArgumentException("이름은 한글 2~5자로 입력해주세요.");
                }
                memberDTO.setmName(mName);

                // 성별 입력 및 검증
                String mGender = "";
                while (true) {
                    System.out.println("\n성별을 선택해주세요.");
                    System.out.println("1: 남성");
                    System.out.println("2: 여성");
                    System.out.print("선택 >> ");
                    String genderInput = sc.next();

                    if ("1".equals(genderInput)) {
                        mGender = "남성";
                        break;
                    } else if ("2".equals(genderInput)) {
                        mGender = "여성";
                        break;
                    } else {
                        System.out.println("잘못 선택하셨습니다. 1 또는 2를 선택해주세요.");
                    }
                }
                memberDTO.setmGender(mGender);

                // 전화번호 입력 및 검증
                System.out.println("\n전화번호를 입력해주세요 (예: 01012345678 또는 021234567)");
                System.out.print("전화번호 >> ");
                String mPhone = sc.next();
                if (!Pattern.matches(PHONE_PATTERN, mPhone)) {
                    throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다. 01012345678 또는 021234567 형식으로 입력해주세요.");
                }
                memberDTO.setmPhone(mPhone);

                // 이메일 입력 및 검증
                System.out.println("\n이메일을 입력해주세요 (예: example@example.com)");
                System.out.print("이메일 >> ");
                String mEmail = sc.next();
                if (!Pattern.matches(EMAIL_PATTERN, mEmail)) {
                    throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다. example@example.com 형식으로 입력해주세요.");
                }
                memberDTO.setmEmail(mEmail);

                // 유효한 입력을 받았을 때
                validInput = true;

                // 회원가입 처리
                memberSQL.registerMember(memberDTO);

            } catch (IllegalArgumentException e) {
                // 유효성 검사 실패 시 메시지 출력 후 재입력 유도
                System.out.println(e.getMessage());
                System.out.println("다시 시도해주세요.");
                sc.nextLine(); // 입력 버퍼 비우기
            } catch (Exception e) {
                // 기타 예외 처리
                System.out.println(memberDTO);
                System.out.println("오류가 발생했습니다: " + e.getMessage());
                System.out.println("다시 시도해주세요.");
                sc.nextLine(); // 입력 버퍼 비우기
            }
        }
    }

    // 내 정보 조회 메서드
    public void viewMyInfo(MemberDTO loggedInUser) {
        memberSQL.viewMyInfo(loggedInUser.getmId());
    }

    // 내 정보 수정 메서드
    public void updateMyInfo(MemberDTO loggedInUser) {
        try {
            System.out.print("새 비밀번호를 입력하세요: ");
            String newPassword = sc.next();
            System.out.print("새 전화번호를 입력하세요: ");
            String newPhone = sc.next();

            loggedInUser.setmPw(newPassword);
            loggedInUser.setmPhone(newPhone);

            boolean isUpdated = memberSQL.updateMyInfo(loggedInUser);

            if (isUpdated) {
                System.out.println("정보가 성공적으로 수정되었습니다.");
                adminSQL.insertActivityLog(loggedInUser.getmNo(), "정보 수정", "회원 정보 수정 성공");
            } else {
                System.out.println("정보 수정에 실패했습니다.");
                adminSQL.insertActivityLog(loggedInUser.getmNo(), "정보 수정", "회원 정보 수정 실패");
            }
        } catch (InputMismatchException e) {
            System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
            sc.nextLine(); // 잘못된 입력 버퍼 비우기
        }
    }

    // 회원 탈퇴 메서드
    public boolean deleteMyAccount(MemberDTO loggedInUser) {

        boolean isDeleted = memberSQL.deleteMyAccount(loggedInUser.getmId());
        if (isDeleted) {
            System.out.println("회원 탈퇴가 완료되었습니다.");
            adminSQL.insertActivityLog(loggedInUser.getmNo(), "회원 탈퇴", "회원 탈퇴 성공");
        } else {
            System.out.println("회원 탈퇴에 실패했습니다.");
            adminSQL.insertActivityLog(loggedInUser.getmNo(), "회원 탈퇴", "회원 탈퇴 실패");
        }
        return isDeleted;
    }

    // 로그아웃 메서드
    public void MemberLogout(MemberDTO loggedInMember) {
        try {
            if (loggedInMember != null) {
                // 로그아웃 처리
                System.out.println("로그아웃 되었습니다.");

                // 로그아웃 성공 시 활동 로그 저장
                adminSQL.insertActivityLog(loggedInMember.getmNo(), "로그아웃", "회원 로그아웃 성공");

            } else {
                System.out.println("로그인된 사용자가 없습니다.");
            }
        } catch (Exception e) {
            System.out.println("로그아웃 처리 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
