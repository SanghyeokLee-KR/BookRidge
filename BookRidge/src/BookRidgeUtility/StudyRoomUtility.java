package BookRidgeUtility;

import BookRidgeDAO.UtilitySQL.StudyRoomSQL;
import BookRidgeDTO.GroupStudyReservationDTO;
import BookRidgeDTO.MemberDTO;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import static BookRidgeUtility.ColorUtility.*;

public class StudyRoomUtility extends ResourcesUtility {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final StudyRoomSQL studyRoomSQL = new StudyRoomSQL();

    // 다음 주의 날짜와 시간 슬롯을 출력하는 메서드
    public void displayNextWeekSchedule() {
        // 다음 주의 첫 번째 날짜 계산
        LocalDate nextMonday = LocalDate.now().plusWeeks(1).with(java.time.DayOfWeek.MONDAY);

        // 다음 주의 각 날에 대해 예약 가능한 시간 슬롯 출력
        System.out.println("다음 주 스터디룸 예약 가능한 시간대입니다:");

        for (int day = 0; day < 7; day++) {
            LocalDate currentDay = nextMonday.plusDays(day);
            System.out.println(currentDay + " 예약 가능한 시간:");
            // 10시부터 18시까지 2시간 간격으로 시간 슬롯 출력
            for (int hour = 10; hour < 18; hour += 2) {
                LocalDateTime startTime = LocalDateTime.of(currentDay, LocalTime.of(hour, 0));
                LocalDateTime endTime = startTime.plusHours(2);
                System.out.println("[" + (hour - 8) / 2 + "] " + timeFormatter.format(startTime.toLocalTime()) + " - " + timeFormatter.format(endTime.toLocalTime()));
            }
        }
    }

    // 스터디룸 예약 메뉴
    public void studyRoomReservationMenu(MemberDTO loggedInUser) {
        try {
            displayNextWeekSchedule(); // 다음 주 예약 가능 시간 출력

            System.out.print("예약할 날짜를 선택하세요 (1~7) >> ");
            int selectedDay = sc.nextInt(); // 사용자가 선택한 날짜 (1~7)
            sc.nextLine(); // 버퍼 비우기

            // 날짜 범위 확인
            if (selectedDay < 1 || selectedDay > 7) {
                System.out.println("잘못된 날짜 선택입니다. 다시 시도해주세요.");
                return;
            }

            System.out.print("예약할 시간대를 선택하세요 (1~4) >> ");
            int selectedTimeSlot = sc.nextInt(); // 사용자가 선택한 시간대 (1~4)
            sc.nextLine(); // 버퍼 비우기

            // 시간대 범위 확인
            if (selectedTimeSlot < 1 || selectedTimeSlot > 4) {
                System.out.println("잘못된 시간대 선택입니다. 다시 시도해주세요.");
                return;
            }

            System.out.print("예약할 스터디룸 번호를 입력하세요 (1~3) >> ");
            int roomNo = sc.nextInt(); // 스터디룸 번호 선택


            // 스터디룸 예약 가능 상태 확인

            if (!studyRoomSQL.isStudyRoomAvailable(roomNo)) {
                System.out.println("해당 스터디룸은 현재 예약이 불가능합니다.");
                return;
            }
            System.out.println("스터디룸 예약 가능 상태 확인 완료");

            // 스터디룸 번호 범위 확인
            if (roomNo < 1 || roomNo > 3) {
                System.out.println("잘못된 스터디룸 번호입니다. 다시 시도해주세요.");
                return;
            }

            // 예약할 시간을 계산 (1일차부터 7일차까지 중 선택)
            LocalDate selectedDate = LocalDate.now().plusWeeks(1).with(java.time.DayOfWeek.MONDAY).plusDays(selectedDay - 1);
            LocalDateTime startTime = LocalDateTime.of(selectedDate, LocalTime.of(10 + (selectedTimeSlot - 1) * 2, 0));
            LocalDateTime endTime = startTime.plusHours(2);

            // 예약 처리 로직 (SQL 연결 등)
            GroupStudyReservationDTO reservation = new GroupStudyReservationDTO();
            reservation.setmNo(loggedInUser.getmNo());
            reservation.setStudyRoomNo(roomNo); // 선택된 스터디룸 번호
            reservation.setStartTime(Timestamp.valueOf(startTime));
            reservation.setEndTime(Timestamp.valueOf(endTime));

            studyRoomSQL.insertReservation(reservation);

            System.out.println("스터디룸 예약이 완료되었습니다. " + dateTimeFormatter.format(startTime) + " - " + dateTimeFormatter.format(endTime));

        } catch (Exception e) {
            System.out.println("예약 처리 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 스터디룸 예약 조회 메서드
    public void viewReservations(MemberDTO loggedInUser) {
        // SQL 클래스 호출
        StudyRoomSQL studyRoomSQL = new StudyRoomSQL();

        // 예약 목록 가져오기
        List<GroupStudyReservationDTO> reservations = studyRoomSQL.getReservationsByMember(loggedInUser.getmNo());

        if (reservations.isEmpty()) {
            System.out.println(RED + "예약된 스터디룸이 없습니다." + RESET);
        } else {
            System.out.println(CYAN + "┌──────────────────────────────────────────  예약된 스터디룸 목록 ─────────────────────────────────────────────┐" + RESET);

            for (GroupStudyReservationDTO reservation : reservations) {
                System.out.printf(YELLOW + "  예약번호: %d" + RESET + " | "
                                + GREEN + "스터디룸: %s" + RESET + " | "
                                + BLUE + "시작: %s" + RESET + " | "
                                + BLUE + "종료: %s" + RESET + " | "
                                + CYAN + "상태: %s" + RESET + "\n",
                        reservation.getReservationNo(),
                        reservation.getStudyRoomName(),
                        reservation.getStartTime(),
                        reservation.getEndTime(),
                        reservation.getStatus().equals("예약중") ? GREEN + "예약중" + RESET : RED + reservation.getStatus() + RESET
                );
            }

            System.out.println(CYAN + "└────────────────────────────────────────────────────────────────────────────────────────────────────────────┘" + RESET);
        }
    }


    // 스터디룸 예약 취소 메서드
    public void cancelReservation(MemberDTO loggedInUser) {
        // 현재 회원의 예약 목록 조회
        StudyRoomSQL studyRoomSQL = new StudyRoomSQL();
        List<GroupStudyReservationDTO> reservations = studyRoomSQL.getReservationsByMember(loggedInUser.getmNo());

        if (reservations.isEmpty()) {
            System.out.println("취소할 스터디룸 예약이 없습니다.");
            return;
        }

        // 예약 목록 출력
        System.out.println("취소할 예약을 선택하세요:");
        for (int i = 0; i < reservations.size(); i++) {
            GroupStudyReservationDTO reservation = reservations.get(i);
            System.out.printf("[%d] 예약번호: %d | 스터디룸: %s | 시작: %s | 종료: %s | 상태: %s\n",
                    i + 1, reservation.getReservationNo(), reservation.getStudyRoomName(),
                    reservation.getStartTime(), reservation.getEndTime(), reservation.getStatus());
        }

        // 예약 선택
        System.out.print("선택 >> ");
        int choice = sc.nextInt();
        if (choice < 1 || choice > reservations.size()) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        // 예약 취소
        GroupStudyReservationDTO selectedReservation = reservations.get(choice - 1);
        boolean success = studyRoomSQL.cancelReservation(selectedReservation.getReservationNo());

        if (success) {
            System.out.println("예약이 성공적으로 취소되었습니다.");
        } else {
            System.out.println("예약 취소에 실패했습니다.");
        }

    }

}
