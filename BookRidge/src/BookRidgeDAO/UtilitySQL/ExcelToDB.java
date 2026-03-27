package BookRidgeDAO.UtilitySQL;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.ParseException;
import java.util.Date;

public class ExcelToDB extends ResourcesSQL {

    public static void main(String[] args) {
        ExcelToDB excelToDB = new ExcelToDB();
        excelToDB.insertExcelData("C:\\Users\\user\\Desktop\\자바 프로젝트\\Book.xls"); // 엑셀 파일 경로 설정
    }

    public void insertExcelData(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new HSSFWorkbook(fis)) { // .xls 파일을 읽기 위해 HSSFWorkbook 사용

            connect(); // 데이터베이스 연결

            // BOOKS 테이블에 데이터 삽입
            String sql = "INSERT INTO BOOKS (BOOK_NO, TITLE, AUTHOR, STATUS, PUBLISHER, PUBLISH_DATE, CATEGORY_CODE, REGISTER_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);

            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 선택

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // 첫 번째 행은 헤더이므로 건너뜀

                // 도서번호
                String bookNo = getCellValueAsString(row.getCell(0));  // 도서번호
                String title = getCellValueAsString(row.getCell(1));  // 도서명
                String author = getCellValueAsString(row.getCell(2)); // 저자
                String status = getCellValueAsString(row.getCell(3)); // 대출상태
                String publisher = getCellValueAsString(row.getCell(4)); // 출판사

                // 출판일 처리
                java.sql.Date publishDate = null;
                if (row.getCell(5) != null) {
                    // 출판일이 날짜 형식인 경우
                    if (row.getCell(5).getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(row.getCell(5))) {
                        publishDate = new java.sql.Date(row.getCell(5).getDateCellValue().getTime());
                    } else {
                        // 출판일이 문자열 형식인 경우
                        String publishDateStr = getCellValueAsString(row.getCell(5));
                        publishDate = parsePublishDate(publishDateStr); // 다양한 형식 처리
                    }
                }

                String categoryCode = getCellValueAsString(row.getCell(6)); // 분류기호

                // 등록일자
                java.sql.Timestamp registerDate = null;
                if (row.getCell(7) != null && row.getCell(7).getCellType() == CellType.STRING) {
                    try {
                        // Convert Korean time indicators to English
                        String dateStr = row.getCell(7).getStringCellValue()
                                .replace("오후", "PM")
                                .replace("오전", "AM");

                        // Parse using SimpleDateFormat with a custom pattern
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss", Locale.ENGLISH);
                        java.util.Date parsedDate = sdf.parse(dateStr);

                        // Convert to Timestamp
                        registerDate = new java.sql.Timestamp(parsedDate.getTime());
                    } catch (ParseException e) {
                        System.out.println("등록일자 변환 중 오류 발생: " + e.getMessage());
                    }
                }

                pstmt.setString(1, bookNo);
                pstmt.setString(2, title);
                pstmt.setString(3, author);
                pstmt.setString(4, status);
                pstmt.setString(5, publisher);
                pstmt.setDate(6, publishDate);
                pstmt.setString(7, categoryCode);
                pstmt.setTimestamp(8, registerDate);

                pstmt.addBatch(); // Batch 처리로 성능 향상
            }

            pstmt.executeBatch(); // Batch 실행
            System.out.println("엑셀 데이터가 성공적으로 DB에 삽입되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(); // 자원 해제
        }
    }

    // 셀 값이 null인지 확인하고 문자열로 반환
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return ""; // null일 경우 빈 문자열 반환
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue()); // 날짜 형식 셀일 경우
                } else {
                    return String.valueOf((int) cell.getNumericCellValue()); // 숫자 형식 셀일 경우
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return ""; // 그 외의 경우 빈 문자열 반환
        }
    }

    // 출판일을 다양한 형식으로 처리하는 메서드
    private java.sql.Date parsePublishDate(String dateStr) {
        try {
            if (dateStr.matches("\\d{8}")) { // 8자리 "YYYYMMDD" 형식
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                return new java.sql.Date(sdf.parse(dateStr).getTime());
            } else if (dateStr.matches("\\d{6}")) { // 6자리 "YYYYMM" 형식 (마지막 일자를 01일로 설정)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                Date parsedDate = sdf.parse(dateStr);
                return new java.sql.Date(parsedDate.getTime());
            } else if (dateStr.matches("\\d{4}")) { // 4자리 "YYYY" 형식 (마지막 월일을 01월 01일로 설정)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                Date parsedDate = sdf.parse(dateStr);
                return new java.sql.Date(parsedDate.getTime());
            } else if (dateStr.matches("\\d{7}")) { // 7자리 "YYYYDDD" 형식 (연도와 연중 일수)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyDDD");
                return new java.sql.Date(sdf.parse(dateStr).getTime());
            } else {
                System.out.println("알 수 없는 출판일 형식: " + dateStr);
            }
        } catch (ParseException e) {
            System.out.println("출판일자 변환 중 오류 발생: " + e.getMessage());
        }
        return null;
    }
}
