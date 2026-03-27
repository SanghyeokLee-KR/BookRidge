package BookRidgeMain;

import BookRidgeUtility.BookManageUtility;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static BookRidgeUtility.ColorUtility.*;

public class BookManageMenu {

    Scanner sc = new Scanner(System.in);

    // 도서 관리 메뉴 메서드
    protected void bookManageMenu() {
        BookManageUtility bookManageUtility = new BookManageUtility();

        while (true) {
            try {
                System.out.println(PURPLE + "\n╔═══════════════════════════ ⊱⋆⊰ ═══════════════════════════╗" + RESET);
                System.out.println("  [1] 전체 도서 조회    [2] 도서 검색     [3] 도서 추가");
                System.out.println("  [4] 도서 수정        [5] 도서 삭제     [6] 뒤로가기");
                System.out.println(PURPLE + "╚═══════════════════════════════════════════════════════════╝"  + RESET);
                System.out.print("선택 >> ");

                // 입력이 숫자가 아니거나 범위를 벗어나는 경우 처리
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        String selectedCategory = bookManageUtility.selectCategory(); // 카테고리 선택
                        if (selectedCategory != null) {
                            // 선택된 카테고리가 "전체"인 경우 전체 도서 목록을 출력
                            if ("전체".equals(selectedCategory)) {
                                bookManageUtility.displayAllBooksWithPaging(); // 전체 도서 목록 페이징
                            } else {
                                // 선택된 카테고리에 맞는 도서 목록을 페이징 처리하여 보여줌
                                bookManageUtility.displayBooksByCategoryWithPaging(selectedCategory);
                            }
                        }
                        break;

                    case 2:
                        bookManageUtility.searchBooks(); // 도서 검색 기능 호출
                        // 도서 검색 기능
                        break;

                    case 3:
                        // 도서 추가 기능
                        bookManageUtility.addBook(); // 도서 추가 기능 호출
                        break;

                    case 4:
                        // 도서 수정 기능
                        bookManageUtility.updateBook();
                        break;

                    case 5:
                        bookManageUtility.deleteBook(); // 도서 삭제 기능 호출
                        break;

                    case 6:
                        System.out.println("뒤로 가기");
                        return; // 상위 메뉴로 돌아가기

                    default:
                        System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                sc.nextLine(); // 잘못된 입력 버퍼 비우기
            } catch (NoSuchElementException e) {
                System.out.println("입력 중 문제가 발생했습니다. 다시 시도해주세요.");
            } catch (Exception e) {
                System.out.println("예상치 못한 오류가 발생했습니다: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
