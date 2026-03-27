package BookRidgeUtility;

public class ColorUtility {

    // ANSI escape codes for text colors
    public static final String RESET = "\u001B[0m";        // 리셋 (기본 색상으로 복원)
    public static final String BLACK = "\u001B[30m";       // 검정색
    public static final String RED = "\u001B[31m";         // 빨간색
    public static final String GREEN = "\u001B[32m";       // 초록색
    public static final String YELLOW = "\u001B[33m";      // 노란색
    public static final String BLUE = "\u001B[34m";        // 파란색
    public static final String PURPLE = "\u001B[35m";      // 보라색
    public static final String CYAN = "\u001B[36m";        // 청록색
    public static final String WHITE = "\u001B[37m";       // 흰색

    // 밝은 색상
    public static final String BLACK_BRIGHT = "\u001B[90m"; // 밝은 검은색 (회색)
    public static final String RED_BRIGHT = "\u001B[91m";   // 밝은 빨간색
    public static final String GREEN_BRIGHT = "\u001B[92m"; // 밝은 초록색
    public static final String YELLOW_BRIGHT = "\u001B[93m";// 밝은 노란색
    public static final String BLUE_BRIGHT = "\u001B[94m";  // 밝은 파란색
    public static final String MAGENTA_BRIGHT = "\u001B[95m";// 밝은 마젠타
    public static final String CYAN_BRIGHT = "\u001B[96m";  // 밝은 시안
    public static final String WHITE_BRIGHT = "\u001B[97m"; // 밝은 흰색

    // 배경색
    public static final String BG_BLACK = "\u001B[40m";    // 배경 검정색
    public static final String BG_RED = "\u001B[41m";      // 배경 빨간색
    public static final String BG_GREEN = "\u001B[42m";    // 배경 초록색
    public static final String BG_YELLOW = "\u001B[43m";   // 배경 노란색
    public static final String BG_BLUE = "\u001B[44m";     // 배경 파란색
    public static final String BG_PURPLE = "\u001B[45m";   // 배경 보라색
    public static final String BG_CYAN = "\u001B[46m";     // 배경 청록색
    public static final String BG_WHITE = "\u001B[47m";    // 배경 흰색

    // 밝은 배경색
    public static final String BG_BLACK_BRIGHT = "\u001B[100m"; // 밝은 검은색 배경
    public static final String BG_RED_BRIGHT = "\u001B[101m";   // 밝은 빨간색 배경
    public static final String BG_GREEN_BRIGHT = "\u001B[102m"; // 밝은 초록색 배경
    public static final String BG_YELLOW_BRIGHT = "\u001B[103m";// 밝은 노란색 배경
    public static final String BG_BLUE_BRIGHT = "\u001B[104m";  // 밝은 파란색 배경
    public static final String BG_MAGENTA_BRIGHT = "\u001B[105m";// 밝은 마젠타 배경
    public static final String BG_CYAN_BRIGHT = "\u001B[106m";  // 밝은 시안 배경
    public static final String BG_WHITE_BRIGHT = "\u001B[107m"; // 밝은 흰색 배경

    // ANSI escape codes for text styles
    public static final String BOLD = "\u001B[1m";         // 굵게
    public static final String UNDERLINE = "\u001B[4m";    // 밑줄
    public static final String REVERSED = "\u001B[7m";     // 색상 반전

    // 텍스트에 색상 적용하는 유틸리티 메서드
    public static String colorText(String text, String color) {
        return color + text + RESET;
    }

    // 텍스트를 한 글자씩 천천히 출력하는 메서드
    public static void slowPrint(String message, long millisPerChar) {
        for (char c : message.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(millisPerChar); // 글자 사이에 지연 시간 설정
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(); // 줄바꿈
    }

    // 텍스트를 한 글자씩 천천히 출력하는 메서드 (색상 포함)
    public static void slowPrint(String message, String color, long millisPerChar) {
        // 색상을 출력
        System.out.print(color);

        // 메시지를 한 글자씩 출력
        for (char c : message.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(millisPerChar); // 글자 사이에 지연 시간 설정
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 색상 초기화 (기본 색상으로 돌아가기)
        System.out.print(ColorUtility.RESET);
        System.out.println(); // 줄바꿈
    }

    public static void clearConsole() {
        // 50줄 정도의 빈 줄을 출력하여 화면을 "지우는" 효과
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

}
