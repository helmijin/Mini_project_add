package ex01;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ContactManager {
	
	public static void main(String[] args) {
		run();
	}
	
	private static void DeleteMovie(Scanner sc) {
		DataDAO dd = DataDAO.newInstance();
		dd.DeleteMovie();
		run();
	}
 
	private static void ListMovie() {
		DataDAO dd = DataDAO.newInstance();
		dd.ListMovie();
		run();
	}
	
 
	private static void AddMovie() {
		 DataDAO dd = DataDAO.newInstance();
		 dd.AddMovie();
		 run();
	}

	private static void SearchMovie(Scanner sc) {
		DataDAO dd = DataDAO.newInstance();
		dd.SearchMovie();
		run();
	}
	
	private static void RandomMovie(Scanner sc) {
		DataDAO dd = DataDAO.newInstance();
		dd.RandomMovie();
		run();
	}
	
	private static void CrawlingMovie(Scanner sc) {
		DataDAO dd = DataDAO.newInstance();
		dd.CrawlingMovie();
		run();
	}
	
	private static void ChoiceMyList(Scanner sc) {
		DataDAO dd = DataDAO.newInstance();
		dd.ChoiceMyList();
		run();
	}
	
	private static void run()  {
		boolean runx = true;
		int num = 0;
		String serchstr = "";
		System.out.println("===============================================");
		System.out.println("            ★메             뉴★               ");
		System.out.println();
		while (runx) {
			Scanner sc = new Scanner(System.in);
			System.out.println("   1.리스트   2.등록      3.삭제        4.검색    ");
			System.out.println("   5.크롤링   6.랜덤추천   7.마이리스트    8.종료    ");
			System.out.println("===============================================");
			System.out.print("메뉴번호 >> ");
			num = sc.nextInt();
 
			switch (num) {
			case 1:
				System.out.println();
				System.out.println("<1. 리스트>");
				ListMovie();
				break;
			case 2:
				System.out.println();
				System.out.println("<2. 등록>");
				AddMovie();
				break;
			case 3:
				System.out.println();
				System.out.println("<3. 삭제>");
				DeleteMovie(sc);
				break;
			case 4:
				System.out.println();
				System.out.println("<4. 검색>");
				SearchMovie(sc);
				System.out.println();
				break;
			case 5:
				System.out.println();
				System.out.println("<5. 크롤링>");
				CrawlingMovie(sc);
				System.out.println();
				break;
			case 6:
				System.out.println();
				System.out.println("<6. 랜덤>");
				RandomMovie(sc);
				break;
			case 7:
				System.out.println();
				System.out.println("<7. 마이리스트>");
				ChoiceMyList(sc);
				break;
			case 8:
				System.out.println();
				System.out.println("***************************************");
				System.out.println("*              감사합니다                *");
				System.out.println("***************************************");
				sc.close();
				runx = false;
				break;
			default:
				System.out.println("[다시 입력해 주세요]");
				System.out.println();
			}
		}
	}
}