package BookRidgeUtility;

import BookRidgeDAO.UtilitySQL.*;
import BookRidgeDTO.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ResourcesUtility {

    Scanner sc = new Scanner(System.in);
    // 맴버
    MemberDTO memberDTO = new MemberDTO();
    MemberSQL memberSQL = new MemberSQL();
    AlertDTO alertDTO = new AlertDTO();

    LoanManageSQL loanManageSQL = new LoanManageSQL(); // 대출 관련 SQL 객체

    BookManageSQL bookManageSQL = new BookManageSQL();

    // 관리자
    AdminDTO adminDTO = new AdminDTO();
    AdminSQL adminSQL = new AdminSQL();

    AlertSQL alertSQL = new AlertSQL();
    // 로그
    ActivityLogDTO log = new ActivityLogDTO();

    // 스캐너 닫기
    public void closeScanner() {
        if (sc != null) {
            sc.close();
        }
    }
}
