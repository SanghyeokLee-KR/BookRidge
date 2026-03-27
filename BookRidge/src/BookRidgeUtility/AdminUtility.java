package BookRidgeUtility;

import BookRidgeDTO.ActivityLogDTO;
import BookRidgeDTO.AdminDTO;
import BookRidgeDTO.MemberDTO;

import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.List;

import static BookRidgeUtility.ColorUtility.*;

public class AdminUtility extends ResourcesUtility {

    // 관리자 로그인 메서드
    public AdminDTO adminLogin() {

        try {
            // 관리자 아이디와 비밀번호 입력받기
            System.out.print("관리자 아이디를 입력하세요 >> ");
            String aId = sc.next();
            adminDTO.setaId(aId);

            System.out.print("비밀번호를 입력하세요 >> ");
            String aPwd = sc.next();
            adminDTO.setaPwd(aPwd);

            // DAO를 통해 로그인 시도
            AdminDTO loggedInAdmin = adminSQL.loginAdmin(adminDTO);

            if (loggedInAdmin != null) {
                System.out.println("관리자 로그인 성공: " + loggedInAdmin.getaNm() + "님 환영합니다.");
            } else {
                System.out.println("관리자 로그인에 실패했습니다. 아이디 또는 비밀번호를 확인해주세요.");
            }

            return loggedInAdmin; // 로그인 성공 시 로그인된 관리자 정보 반환, 실패 시 null 반환

        } catch (InputMismatchException e) {
            System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
            sc.nextLine(); // 잘못된 입력 버퍼 비우기
        } catch (Exception e) {
            System.out.println("예상치 못한 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // 예외 발생 시 null 반환
    }

    // 전체 회원 조회 메서드
    public void viewAllMembers() {
        List<MemberDTO> memberList = adminSQL.getAllMembers();

        System.out.println(CYAN + "┌────────────────────────────────────────────── 👥 전체 회원 목록 ───────────────────────────────────────────────┐" + RESET);
        for (MemberDTO member : memberList) {
            // 상태에 따라 색상을 다르게 설정 (정상: GREEN, 탈퇴/차단: RED)
            String statusColor = "정상".equals(member.getmStatus()) ? GREEN : RED;

            // 회원 정보 출력
            System.out.printf(
                    YELLOW + "  회원번호" + GREEN + " >> " + WHITE + "%d  " +
                            YELLOW + "아이디" + GREEN + " >> " + WHITE + "%s  " +
                            YELLOW + "이름" + GREEN + " >> " + WHITE + "%s  " +
                            YELLOW + "전화번호" + GREEN + " >> " + WHITE + "%s  " +
                            YELLOW + "이메일" + GREEN + " >> " + WHITE + "%s  " +
                            YELLOW + "상태" + GREEN + " >> " + statusColor + "%s\n" + RESET,
                    member.getmNo(), member.getmId(), member.getmName(), member.getmPhone(), member.getmEmail(), member.getmStatus());
        }
        System.out.println(CYAN + "└───────────────────────────────────────────────────────────────────────────────────────────────────────────────┘" + RESET);
    }

    // 회원 검색 메서드
    public void viewMemberById() {
        System.out.print("🔍 " + YELLOW + "검색할 이름 또는 아이디를 입력하세요: " + RESET);
        String keyword = sc.next();

        List<MemberDTO> memberList = adminSQL.searchMembers(keyword);

        if (!memberList.isEmpty()) {
            System.out.println(CYAN + "┌───────────────────── 🔍 검색 결과 ─────────────────────────────────────────────────────────────────────────────┐" + RESET);
            for (MemberDTO member : memberList) {
                // 상태에 따라 색상을 다르게 설정 (정상: GREEN, 탈퇴/차단: RED)
                String statusColor = "정상".equals(member.getmStatus()) ? GREEN : RED;

                // 회원 정보 출력
                System.out.printf(
                        YELLOW + "  회원번호" + GREEN + " >> " + WHITE + "%d  " +
                                YELLOW + "아이디" + GREEN + " >> " + WHITE + "%s  " +
                                YELLOW + "이름" + GREEN + " >> " + WHITE + "%s  " +
                                YELLOW + "전화번호" + GREEN + " >> " + WHITE + "%s  " +
                                YELLOW + "이메일" + GREEN + " >> " + WHITE + "%s  " +
                                YELLOW + "상태" + GREEN + " >> " + statusColor + "%s\n" + RESET,
                        member.getmNo(), member.getmId(), member.getmName(), member.getmPhone(), member.getmEmail(), member.getmStatus());
            }
            System.out.println(CYAN + "└───────────────────────────────────────────────────────────────────────────────────────────────────────────────┘" + RESET);
        } else {
            System.out.println(RED + "검색 결과가 없습니다." + RESET);
        }
    }

    // 유저 수를 확인하는 메서드
    public void displayUserCount() {
        int userCount = adminSQL.getUserCount();
        System.out.println("현재 데이터베이스에 저장된 회원 수: " + userCount);

    }

    // 회원 활동 로그 조회 메서드
    public void viewMemberActivityLog() {
        try {
            System.out.print("🔍 " + YELLOW + "활동 로그를 조회할 회원의 번호나 아이디를 입력하세요: " + RESET);
            String input = sc.next();

            int memberNo = -1; // 초기화
            if (input.matches("\\d+")) { // 입력이 숫자인 경우
                memberNo = Integer.parseInt(input);
            } else {
                // 아이디로 회원 번호 찾기
                memberNo = adminSQL.getMemberNoById(input);
                if (memberNo == -1) {
                    System.out.println(RED + "❌ 해당 아이디를 가진 회원이 존재하지 않습니다." + RESET);
                    return;
                }
            }

            List<ActivityLogDTO> logList = adminSQL.getActivityLogs(memberNo);

            if (!logList.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 밀리초 제거된 형식 지정
                int sequence = 1; // 순번 초기화
                System.out.println(CYAN + "┌──────────────────────────────────────────────────── 📝 활동 로그 ─────────────────────────────────────────────────────┐" + RESET);
                for (ActivityLogDTO log : logList) {
                    String formattedDate = sdf.format(log.getActivityDate()); // 밀리초 제거된 시간 형식으로 출력
                    System.out.printf(
                            GREEN + "  번호" + WHITE + " >> %d  " +
                                    BLUE + "활동 유형" + WHITE + " >> %s  " +
                                    YELLOW + "활동 상세" + WHITE + " >> %s  " +
                                    BLUE + "활동 일시" + WHITE + " >> %s\n" + RESET,
                            sequence++, log.getActivityType(), log.getActivityDetail(), formattedDate
                    );
                }
                System.out.println(CYAN + "└──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘" + RESET);
            } else {
                System.out.println(RED + "❌ 해당 회원의 활동 로그가 없습니다." + RESET);
            }

        } catch (InputMismatchException e) {
            System.out.println(RED + "❌ 잘못된 입력입니다. 다시 시도해주세요." + RESET);
            sc.nextLine(); // 잘못된 입력 버퍼 비우기
        } catch (Exception e) {
            System.out.println(RED + "❌ 예상치 못한 오류가 발생했습니다: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }

    // 회원 정보 수정 메서드
    public void updateMemberInfo() {
        System.out.print("수정할 회원의 아이디를 입력하세요 >> ");
        String mId = sc.next();

        // 아이디 중복 체크
        if (!memberSQL.isIdDuplicate(mId)) {
            System.out.println("아이디를 다시 확인하십시오");
            return;
        }

        // 수정할 필드 선택
        System.out.println("수정할 항목을 선택하세요:");
        System.out.println("[1] 비밀번호 [2] 이메일 [3] 전화번호");
        int choice = sc.nextInt();

        // 선택한 항목에 따른 필드명과 새 값 입력
        String field = (choice == 1) ? "비밀번호" : (choice == 2) ? "이메일" : (choice == 3) ? "전화번호" : null;
        if (field == null) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        System.out.printf("새로운 %s를 입력하세요 >> ", field);
        String newValue = sc.next();

        adminSQL.updateMemberInfoInDB(mId, choice, newValue);
        System.out.printf("%s가 성공적으로 변경되었습니다.\n", field);
    }

    // 회원 상태 변경 메서드
    public void changeMemberStatus() {

        System.out.print("상태를 변경할 회원의 아이디를 입력하세요 >> ");
        String mId = sc.next();

        // 아이디 중복 체크
        if (!memberSQL.isIdDuplicate(mId)) {
            System.out.println("아이디를 다시 확인하십시오");
            return;
        }

        // 상태 입력 및 검증
        String status = "";
        while (true) {
            System.out.print("변경할 상태를 입력하세요 (정상/차단) >> ");
            status = sc.next();

            if ("정상".equals(status) || "차단".equals(status)) {
                break; // 올바른 입력이면 반복 종료
            } else {
                System.out.println("잘못된 입력입니다. '정상' 또는 '차단'을 입력해주세요.");
            }
        }

        // AdminSQL로 상태 변경 요청
        adminSQL.updateMemberStatus(mId, status);
        System.out.printf("회원의 상태가 '%s'로 변경되었습니다.\n", status);
    }

    // 회원 탈퇴
    public void deleteMemberStatus() {
        System.out.print("상태를 변경할 회원의 아이디를 입력하세요 >> ");
        String mId = sc.next();

        // 아이디 중복 체크
        if (!memberSQL.isIdDuplicate(mId)) {
            System.out.println("아이디를 다시 확인하십시오");
            return;
        }


        adminSQL.deleteMemberStatus(mId);
    }
}