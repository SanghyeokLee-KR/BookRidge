package BookRidgeUtility;

import BookRidgeDAO.UtilitySQL.WishBookSQL;
import BookRidgeDTO.MemberDTO;
import BookRidgeDTO.WishBookDTO;

import java.util.List;

import static BookRidgeUtility.ColorUtility.*;

public class WishBookUtility extends ResourcesUtility {
    // 희망 도서 신청 메서드
    public void addWishBook(MemberDTO loggedInUser) {

        // 희망 도서 신청
        System.out.print("희망 도서 제목을 입력하세요 >> ");
        String title = sc.nextLine();

        System.out.print("희망 도서 저자를 입력하세요 >> ");
        String author = sc.nextLine();

        System.out.print("희망 도서 출판사를 입력하세요 >> ");
        String publisher = sc.nextLine();

        // WishBookDTO 객체 생성
        WishBookDTO wishBookDTO = new WishBookDTO();
        wishBookDTO.setMemberNo(loggedInUser.getmNo());
        wishBookDTO.setTitle(title);
        wishBookDTO.setAuthor(author);
        wishBookDTO.setPublisher(publisher);

        // 희망 도서 신청 유틸리티 메서드 호출
        System.out.println("희망 도서 신청이 완료되었습니다.");

        WishBookSQL wishBookSQL = new WishBookSQL();
        wishBookSQL.insertWishBook(wishBookDTO);  // SQL 실행
    }

    // 대기 중인 희망 도서 목록을 출력하는 메서드
    public void displayWishBooks(List<WishBookDTO> wishBookList) {
        if (wishBookList.isEmpty()) {
            System.out.println(CYAN + "대기 중인 희망 도서가 없습니다." + RESET);
        } else {
            System.out.println(CYAN + "┌───────────────────────────────────────────── 대기 중인 희망 도서 목록 ──────────────────────────────────────────────┐" + RESET);
            for (WishBookDTO wishBook : wishBookList) {
                System.out.printf(CYAN + "  " + YELLOW + "도서 ID: " + RESET + "%d " +
                                YELLOW + "| 회원 번호: " + RESET + "%d " +
                                YELLOW + "| 제목: " + RESET + "%s " +
                                YELLOW + "| 저자: " + RESET + "%s " +
                                YELLOW + "| 출판사: " + RESET + "%s " +
                                YELLOW + "| 신청일: " + RESET + "%s " +
                                YELLOW + "| 상태: " + RESET + "%s " + CYAN + "\n",
                        wishBook.getWishBookId(),
                        wishBook.getMemberNo(),
                        wishBook.getTitle(),
                        wishBook.getAuthor(),
                        wishBook.getPublisher(),
                        wishBook.getRequestDate(),
                        wishBook.getStatus());
            }
            System.out.println(CYAN + "└──────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘" + RESET);
        }
    }


    // 신청 내역을 출력하는 메서드
    public void displayWishBookHistory(List<WishBookDTO> wishBookList) {
        if (wishBookList.isEmpty()) {
            System.out.println(CYAN + "신청 내역이 없습니다." + RESET);
        } else {
            System.out.println(CYAN + "┌──────────────────────────────────────────── 희망 도서 신청 내역 ─────────────────────────────────────────────────┐" + RESET);
            for (WishBookDTO wishBook : wishBookList) {
                System.out.printf(CYAN + "  " + YELLOW + "도서 ID: " + RESET + "%d " +
                                YELLOW + "| 회원 번호: " + RESET + "%d " +
                                YELLOW + "| 제목: " + RESET + "%s " +
                                YELLOW + "| 저자: " + RESET + "%s " +
                                YELLOW + "| 출판사: " + RESET + "%s " +
                                YELLOW + "| 신청일: " + RESET + "%s " +
                                YELLOW + "| 상태: " + RESET + "%s",
                        wishBook.getWishBookId(),
                        wishBook.getMemberNo(),
                        wishBook.getTitle(),
                        wishBook.getAuthor(),
                        wishBook.getPublisher(),
                        wishBook.getRequestDate(),
                        wishBook.getStatus());

                // 관리자 응답이 있는 경우만 출력
                if (wishBook.getAdminResponse() != null && !wishBook.getAdminResponse().isEmpty()) {
                    System.out.printf(" " + YELLOW + "| 코멘트: " + RESET + "%s", wishBook.getAdminResponse());
                }
                System.out.println();
            }
            System.out.println(CYAN + "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘" + RESET);
        }
    }


}

