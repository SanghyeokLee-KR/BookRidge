package BookRidgeUtility;

import BookRidgeDAO.UtilitySQL.AlertSQL;
import BookRidgeDTO.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class AlertUtility extends ResourcesUtility {
    AlertSQL alertSQL = new AlertSQL();

    // 알림 생성 메서드
    public void createAlert(int memberNo, String alertType, String message) {
        AlertDTO alert = new AlertDTO();
        alert.setMemberNo(memberNo);
        alert.setAlertType(alertType);
        alert.setAlertMessage(message);

        if (alertSQL.createAlert(alert)) {
            System.out.println("알림이 성공적으로 생성되었습니다.");
        } else {
            System.out.println("알림 생성에 실패했습니다.");
        }
    }

    // 알림 메시지를 출력하는 메서드
    private void displayAlertMessage(AlertDTO alert, int memberNo) {
        String alertMessage;

        // 날짜 형식 지정 (시간 제외)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 대출 알림일 경우 반납 기한일 포함
        if ("대출".equals(alert.getAlertType())) {
            // 반납일을 생성일 + 7일로 설정
            Date returnDate = new Date(alert.getCreatedAt().getTime() + (7L * 24 * 60 * 60 * 1000));

            alertMessage = String.format("유형: %s | 내용: 도서 %s | 대출일: %s | 반납 기한: %s | 읽음 여부: %s",
                    alert.getAlertType(),
                    alert.getAlertMessage(),
                    dateFormat.format(alert.getCreatedAt()),
                    dateFormat.format(returnDate),
                    alert.getIsRead().equals("Y") ? "읽음" : "안 읽음");
        } else {
            // 그 외의 알림 처리 (예: 예약 알림)
            alertMessage = String.format("유형: %s | 내용: %s | 생성일: %s | 읽음 여부: %s",
                    alert.getAlertType(),
                    alert.getAlertMessage(),
                    dateFormat.format(alert.getCreatedAt()),
                    alert.getIsRead().equals("Y") ? "읽음" : "안 읽음");
        }

        System.out.println(alertMessage);

        // 알림을 읽음 상태로 업데이트
        if ("N".equals(alert.getIsRead())) {
            alertSQL.markAlertAsRead(alert.getAlertId());  // 알림을 읽음 상태로 변경
        }
    }


    // 전체 알림 조회 메서드
    public void displayAllAlerts(int memberNo) {
        List<AlertDTO> alerts = alertSQL.getAlertsByMember(memberNo);

        if (alerts.isEmpty()) {
            System.out.println("새로운 알림이 없습니다.");
        } else {
            for (AlertDTO alert : alerts) {
                displayAlertMessage(alert, memberNo);
            }
        }
    }

    // 안 읽은 알림 조회 메서드
    public void displayUnreadAlerts(int memberNo) {
        List<AlertDTO> alerts = alertSQL.getUnreadAlertsByMember(memberNo);

        if (alerts.isEmpty()) {
            System.out.println("읽지 않은 알림이 없습니다.");
        } else {
            for (AlertDTO alert : alerts) {
                displayAlertMessage(alert, memberNo);
            }
        }
    }
}
