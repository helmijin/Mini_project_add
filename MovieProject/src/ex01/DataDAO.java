package ex01;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataDAO {

	private Connection conn = null;
	private PreparedStatement pstmt =null;
	private ResultSet rs = null;
	private static DataDAO dao;
	private final String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private final String user = "scott";
	private final String pwd  = "1234";

	private DataDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//			System.out.println("-------Class.forName-----------");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url,user,pwd);
			//			System.out.println("-----------DriverManager---------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void disConnection() {
		try {
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
			//			System.out.println("-----------DriverManager disConnection---------------");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static DataDAO newInstance() {
		if(dao == null) 
			dao = new DataDAO();

		return dao;
	}

	//DB에 해당 테이블 생성
	public void CreateTable() {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "CREATE TABLE MOVIETABLE ("
					+ "NO NUMBER(4) PRIMARY KEY,"
					+ "TITLE NVARCHAR2 (50),"
					+ "RATE NVARCHAR2 (300),"
					+ "GENRE NVARCHAR2 (100),"
					+ "DIR NVARCHAR2 (50),"
					+ "ACT NVARCHAR2 (100),"
					+ "TIMES NVARCHAR2 (100),"
					+ "DATES NVARCHAR2 (100),"
					+ "ADDS NVARCHAR2 (20),"
					+ "REVIEW NVARCHAR2 (2000),"
					+ "SUMMARY NVARCHAR2 (2000)"
					+ ")";

			//3. 오라클로 sql 문장 전송
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		}catch (SQLException e) {
			System.out.println();
		}finally {
			disConnection();
		}
	}

	public void DeleteTable() {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "DROP TABLE MOVIETABLE";

			//3. 오라클로 sql 문장 전송
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		}catch (SQLException e) {
			System.out.println();
		}finally {
			disConnection();
		}
	}

	public void CreateSequence() {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "CREATE SEQUENCE MOVIETABLE_SQL INCREMENT BY 1 START WITH 1";

			//3. 오라클로 sql 문장 전송
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		}catch (SQLException e) {
			System.out.println();
		}finally {
			disConnection();
		}
	}

	public void DropSequence() {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "DROP TABLE MOVIETABLE_SQL";

			//3. 오라클로 sql 문장 전송
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

		}catch (SQLException e) {
			System.out.println();
		}finally {
			disConnection();
		}
	}

	public void CrawlingMovie() {
		System.out.println("<크롤링 하는 중입니다..>");
		List<MovieVO> list = new ArrayList<>();
		DataDAO dao = DataDAO.newInstance(); 
		try {
			for(int i=0; i<1; i++) {

				//네이버 영화 홈페이지
				Document doc = Jsoup.connect("https://movie.naver.com/movie/running/current.naver").get();

				//썸네일
				Elements link2 = doc.select("div.thumb a"); 

				for(int j=0; j<20; j++) { 

					//상세 주소
					Document doc2 = Jsoup.connect("https://movie.naver.com/movie/running/current.naver" + link2.get(j).attr("href")).get(); 

					//줄거리
					Elements link4 = doc2.select("p con_tx");
					Element link5 = link4.get(0);

					//리뷰
					Elements link3 = doc2.select("div.score_result li:eq(0) div.score_reple p"); 

					//영화 제목
					Elements title1 = doc2.select("h3.h_movie a");
					Element title2 = title1.get(0);

					//평점
					Elements rate1 = doc2.select("span.st_off span.st_on");
					Element rate2 = rate1.get(0);

					//장르(개요)
					Elements genre1 = doc2.select("span.st_off span.st_on");
					Element genre2 = genre1.get(0);

					//감독
					Elements dir1 = doc2.select("dl.info_spec dd:eq(3)");
					Element dir2 = dir1.get(0);

					//배우
					Elements act1 = doc2.select("dl.info_spec dd:eq(5) p");
					Element act2 = act1.get(0);				
					String act3 = act2.text();

					//상영시간
					Elements time1 = doc2.select("dl.info_spec dd span:eq(2)");
					Element time2 = time1.get(0);

					//개봉 날짜
					Elements date1 = doc2.select("dl.info_spec dd span:eq(3)");
					Element date2 = date1.get(0);

					//MovieVO class 생성
					MovieVO mvo = new MovieVO(); 

					//번호
					mvo.setNum(j+1);
					//영화 제목
					mvo.setTitle(title2.text());
					//네티즌 평점
					mvo.setRate(rate2.text());
					//개요
					mvo.setGenre(genre2.text());
					//감독
					mvo.setDir(dir2.text());
					if(!act3.contains("["));
					//출연
					mvo.setAct(act2.text());
					//영화 시간
					mvo.setTimes(time2.text());
					//영화 개봉일
					mvo.setDates(date2.text());
					//직접 추가한 내용 아니기 때문에 공백
					mvo.setAdds("");
					//관람객 리뷰
					mvo.setReview(link3.text());
					//줄거리 요약
					mvo.setSummary(link4.text());

					list.add(mvo);

					dao.MovieInsert(mvo);
				}
				System.out.println("<크롤링 하는 중입니다...>");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void ListMovie() {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "select * from Movietable order by no";

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);

			//4. 호출
			ResultSet rs = pstmt.executeQuery(); 

			//5. 데이터를 읽기
			while(rs.next()) {
				MovieVO mvo = new MovieVO();

				System.out.println("번    호 : " + rs.getInt("no"));
				System.out.println("영화 제목 : " + "《" + rs.getString("title")+"》");
				System.out.println("평    점 : " + rs.getString("rate"));
				System.out.println("장    르 : " + rs.getString("genre"));
				System.out.println("감    독 : " + rs.getString("dir"));
				System.out.println("배    우 : " + rs.getString("act"));
				System.out.println("상영 시간 : " + rs.getString("times"));
				System.out.println("개봉 날짜 : " + rs.getString("dates"));
				System.out.println();
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}

	public void AddMovie() {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "insert into Movietable values(Movietable_sql.nextval, ?,?,?,?,?,?,?)";

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);
			//4. ? 값 저장
			MovieVO mvo = new MovieVO();

			Scanner sc1 = new Scanner(System.in);
			//영화 제목
			System.out.print(">영화 제목   : ");
			mvo.setTitle(sc1.next());
			// 네티즌 평점
			System.out.print(">평점 : ");
			mvo.setRate(sc1.next()); 
			// 개요 
			System.out.print(">장르   : ");
			mvo.setGenre(sc1.next()); 
			// 감독
			System.out.print(">감독   : ");
			mvo.setDir(sc1.next()); 
			//출연 배우
			System.out.print(">배우   : ");
			mvo.setAct(sc1.next()); 
			// 영화 상영 시간
			System.out.print(">상영시간   : ");
			mvo.setTimes(sc1.next()); 
			// 영화 개봉일
			System.out.print(">개봉일  : ");
			mvo.setDates(sc1.next()); 
			mvo.setAdds("추가");

			dao.MovieInsert(mvo);

			//5. 전송된 값을 커밋 또는 업데이트
			//pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}

	}

	public void MovieInsert(MovieVO se) {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "insert into Movietable values(Movietable_sql.nextval, ?,?,?,?,?,?,?)";

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);

			//4. ? 값 저장
			pstmt.setString(1, se.getTitle());
			pstmt.setString(2, se.getRate());
			pstmt.setString(3, se.getGenre());
			pstmt.setString(4, se.getDir());
			pstmt.setString(5, se.getAct());
			pstmt.setString(6, se.getTimes());
			pstmt.setString(7, se.getDates());
			pstmt.setString(8, se.getAdds());
			pstmt.setString(9, se.getReview());
			pstmt.setString(10, se.getSummary());

			//5. 전송된 값을 커밋 또는 업데이트
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}
	public void DeleteMovie() {

		try {
			String scP;
			String scY = "Y";
			String scN = "N";

			ViewMovie();

			System.out.println("전체 삭제 하시겠습니까? Y / N");
			do {
				Scanner sc = new Scanner(System.in);
				scP = sc.next();

				if(!scP.equals(scY) && !scP.equals(scN))
					System.out.println("잘못 입력했습니다 다시 입력해주세요 Y / N");
			} while(!scP.equals(scY) && !scP.equals(scN));

			if(scP.equals(scY)) {
				System.out.println("정말 전체 삭제 하시겠습니까? Y / N");
				Scanner sc2 = new Scanner(System.in);
				scP = sc2.next();
				if(scP.equals(scY))
					AllDeleteMovie();
			}

			else if(scP.equals(scN)) {

				//1. DB연결
				getConnection();

				//2. sql문 작성
				String sql = "Delete from Movietable where no = ?";

				//3. 오라클로 sql문장 전송
				pstmt = conn.prepareStatement(sql);
				//4. ? 값 저장

				Scanner sc2 = new Scanner(System.in);
				System.out.println("삭제할 목록의 번호 입력>>");
				int bno = Integer.parseInt(sc2.next());

				pstmt.setInt(1,bno);
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}

	public void AllDeleteMovie() {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "Delete from Movietable where adds = '추가'";

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);

			//4. 업데이트
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}

	public void ViewMovie() {
		try {
			getConnection();

			String sql = "select * from MovieTable where adds = '추가'";

			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();

			ResultSet rs = pstmt.executeQuery();

			while(rs.next()) { 
				System.out.println("번호    영화      평점     장르     감독      배우      상영시간     개봉날짜");
				System.out.println("번    호 : " + rs.getInt("no") + "\t");
				System.out.println("영화 제목 : " + rs.getString("title") + "\t");
				System.out.println("평    점 : " + rs.getString("rate") + "\t");
				System.out.println("장    르 : " + rs.getString("genre") + "\t");
				System.out.println("감    독 : " + rs.getString("dir") + "\t");
				System.out.println("배    우 : " + rs.getString("act") + "\t");
				System.out.println("상영시간 : " + rs.getString("times") + "\t");
				System.out.println("개봉날짜 : " + rs.getString("dates") + "\t");
				System.out.println("추가항목 : " + rs.getString("adds") + "\t");
				System.out.println();
				System.out.println();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}

	public void SearchMovie() {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "select * from Movietable where (title || genre || Act || dir) like ?";
			System.out.println("제목이나 장르 혹은 배우나 감독의 이름을 입력해주세요.");

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);

			//4. 호출
			Scanner sc = new Scanner(System.in);
			String bno = '%' + sc.next() + '%';
			pstmt.setString(1,bno);

			ResultSet rs = pstmt.executeQuery();

			//5. 데이터를 읽기
			while(rs.next()) {
				System.out.println("번호	  영화	평점	   장르	   감독	  배우				상영시간 			개봉날짜");
				System.out.print(rs.getInt("no") + "\t");
				System.out.print(rs.getString("title") + "\t");
				System.out.print(rs.getString("rate") + "\t");
				System.out.print(rs.getString("genre") + "\t");
				System.out.print(rs.getString("dir") + "\t");
				System.out.print(rs.getString("act") + "\t");
				System.out.print(rs.getString("times") + "\t");
				System.out.print(rs.getString("dates") + "\t");
				System.out.print(rs.getString("adds") + "\t");
				System.out.println();
				System.out.println();
			}
			System.out.println("선택한 장르를 상세보기 하실 수 있습니다.");
			System.out.println("종료를 하고 싶으시면 0번을 눌러 주세요");
			sc=new Scanner(System.in);
			int num = sc.nextInt();
			if(num !=0) {
				Search2Movie();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}

	public void Search2Movie() {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			System.out.println("상세보기 할 영화의 번호를 입력해 주세요.");
			String sql = "select no,title,review,summary from MovieTable where no = ?";

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);

			//4. 호출
			Scanner sc = new Scanner(System.in);
			int bno = sc.nextInt();
			pstmt.setInt(1, bno);
			ResultSet rs = pstmt.executeQuery();

			//5. 데이터를 읽기
			while(rs.next()) {
				System.out.println("번호  영화제목   리뷰                줄거리");
				System.out.println(rs.getInt("no") + "\t");
				System.out.println(rs.getString("title") + "\t");
				System.out.println(rs.getString("review") + "\t");
				System.out.println(rs.getString("summary") + "\t");
				System.out.println();
				System.out.println();
			}
			System.out.println("선택한 장르를 상세보기 하실 수 있습니다.");
			sc = new Scanner(System.in);
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}

	public void RandomMovie() {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "select * from MovieTable where RATE > '관람객 평점 7.00'"
					+ "AND RATE != '관람객 평점 없음' order by DBMS_RANDOM.RANDOM";

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);

			//4. 호출
			ResultSet rs = pstmt.executeQuery();

			//5. 데이터를 읽기
			while(rs.next()) {

				MovieVO mvo = new MovieVO();

				mvo.setNum(rs.getInt("no"));
				mvo.setTitle(rs.getString("title"));
				mvo.setRate(rs.getString("rate"));
				mvo.setGenre(rs.getString("genre"));
				mvo.setDir(rs.getString("dir"));
				mvo.setAct(rs.getString("act"));
				mvo.setTimes(rs.getString("times"));
				mvo.setDates(rs.getString("dates"));
				mvo.setAdds(rs.getString("adds"));

				System.out.println("번    호 : " + mvo.getNum());
				System.out.println("영화 제목 : " + mvo.getTitle());
				System.out.println("평    점 : " + mvo.getRate());
				System.out.println("장    르 : " + mvo.getGenre());
				System.out.println("감    독 : " + mvo.getDir());
				System.out.println("배    우 : " + mvo.getAct());
				System.out.println("상영 시간 : " + mvo.getTimes());
				System.out.println("개봉 날짜 : " + mvo.getDates());
				System.out.println("추가 됨 항목 : " + mvo.getAdds());
				System.out.println();
				break;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}

	public void ChoiceMyList() {
		System.out.println("========================================================");
		System.out.println("          ★마        이       리       스      트★            ");
		System.out.println();
		System.out.println("   1.리스트 생성        2.리스트      3.영화 추가    4.검색    ");
		System.out.println("   5.삭제             6.리스트 삭제   7.이전 목록으로 돌아가기    ");
		System.out.println();
		System.out.println("========================================================");
		Scanner sc = new Scanner(System.in);
		int num = sc.nextInt();

		if(num==1)
			CreateMyList();
		else if (num==2)
			ViewMyList();
		else if (num==3)
			InsertMyList();
		else if (num==4)
			SearchMyList();
		else if (num==5)
			DeleteMyList();					
		else if (num==6)
			AllDeleteMyList();						
		else if (num==7);
	}

	public void CreateMyList() {
		try {

			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "create table Movie_MyList as select * from MovieTable where 1<>1";

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}

	public void ViewMyList() {
		try {

			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "select * from Movie_MyList order by no";

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);

			//4. 호출
			ResultSet rs = pstmt.executeQuery();

			//5. 데이터를 읽기
			while(rs.next()) { 
				System.out.println("번    호 : " + rs.getInt("no"));
				System.out.println("영화 제목 : " + "《" + rs.getString("title") + "》");
				System.out.println("평    점 : " + rs.getString("rate"));
				System.out.println("장    르 : " + rs.getString("genre"));
				System.out.println("감    독 : " + rs.getString("dir"));
				System.out.println("배    우 : " + rs.getString("act"));
				System.out.println("상영 시간 : " + rs.getString("times"));
				System.out.println("개봉 날짜 : " + rs.getString("dates"));
				System.out.println();
				System.out.println();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}
	public void InsertMyList() {
		try {

			ListMovie();

			//1. DB연결
			getConnection();

			//2. sql문 작성
			System.out.println("☞ 추가할 영화의 번호를 입력해주세요 ☜");
			String sql = "insert into Movie_MyList\r\n"
					+ "select * from MovieTable\r\n"
					+ "where no = ?";

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);

			Scanner sc = new Scanner(System.in);
			int bno = sc.nextInt();
			pstmt.setInt(1, bno);

			pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}

	public void SearchMyList() {
		try {

			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "select * from Movietable where (title || genre || Act || dir) like ?";
			System.out.println("제목이나 장르 혹은 배우나 감독의 이름을 입력해주세요");

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);

			//4. 호출
			Scanner sc = new Scanner(System.in);
			String bno = '%' + sc.next() + '%';
			pstmt.setString(1, bno);
			ResultSet rs = pstmt.executeQuery();

			//5. 데이터를 읽기
			while(rs.next()) {
				System.out.println("번호	  영화	평점	   장르	   감독	  배우				상영시간 			개봉날짜");
				System.out.print(rs.getInt("no") + "\t");
				System.out.print(rs.getString("title") + "\t");
				System.out.print(rs.getString("rate") + "\t");
				System.out.print(rs.getString("genre") + "\t");
				System.out.print(rs.getString("dir") + "\t");
				System.out.print(rs.getString("act") + "\t");
				System.out.print(rs.getString("times") + "\t");
				System.out.print(rs.getString("dates") + "\t");
				System.out.print(rs.getString("adds") + "\t");
				System.out.println();
				System.out.println(); }
			System.out.println("선택한 장르를 상세보기 하실 수 있습니다.");
			System.out.println("종료를 하고 싶으시면 0번을 눌러주세요.");
			sc = new Scanner(System.in);
			if(sc.nextInt() !=0) {
				Search2MyList();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}

	public void Search2MyList() {
		try {

			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = null;
			sql = "select no,title,review,summary from Movie_MyList where no = ?";

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);

			//4. 호출
			Scanner sc = new Scanner(System.in);
			int bno = sc.nextInt();
			pstmt.setInt(1, bno);
			
			ResultSet rs = pstmt.executeQuery();

			//5. 데이터를 읽기
			while(rs.next()) {
				System.out.println("번호  영화제목   리뷰                줄거리");
				System.out.println(rs.getInt("no") + "\t");
				System.out.println(rs.getString("title") + "\t");
				System.out.println(rs.getString("review") + "\t");
				System.out.println(rs.getString("summary") + "\t");
				System.out.println();
				System.out.println();
			}
			System.out.println("선택한 장르를 상세보기 하실 수 있습니다.");
			sc = new Scanner(System.in);
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}

	public void DeleteMyList() {
		try {
			String scP;
			String scY = "Y";
			String scN = "N";

			ViewMovie();

			System.out.println("전체 삭제 하시겠습니까? Y / N");
			do {
				Scanner sc = new Scanner(System.in);
				scP = sc.next();

				if(!scP.equals(scY) && !scP.equals(scN))
					System.out.println("잘못 입력했습니다 다시 입력해주세요 Y / N");
			} while(!scP.equals(scY) && !scP.equals(scN));

			if(scP.equals(scY)) {
				System.out.println("정말 전체 삭제 하시겠습니까? Y / N");
				Scanner sc2 = new Scanner(System.in);
				scP = sc2.next();
				if(scP.equals(scY))
					AllDeleteMovie();
			}

			else if(scP.equals(scN)) {

				//1. DB연결
				getConnection();

				//2. sql문 작성
				String sql = "Delete from Movie_MyList where no = ?";

				//3. 오라클로 sql문장 전송
				pstmt = conn.prepareStatement(sql);
				
				//4. ? 값 저장
				Scanner sc2 = new Scanner(System.in);
				System.out.println("삭제할 목록의 번호 입력>>");
				int bno = Integer.parseInt(sc2.next());
				pstmt.setInt(1,bno);
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}
	public void AllDeleteMyList() {
		try {
			//1. DB연결
			getConnection();

			//2. sql문 작성
			String sql = "Delete from Movie_MyList where adds = '추가'";

			//3. 오라클로 sql문장 전송
			pstmt = conn.prepareStatement(sql);

			//4. 업데이트
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			disConnection();
		}
	}
}