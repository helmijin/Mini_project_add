package ex01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Movie {
	
	public static void main(String[] args) {
		ContactManager cm = new ContactManager();
		DataDAO dao = DataDAO.newInstance();
		dao.DeleteTable();
		dao.CreateTable();
		dao.DropSequence();
		dao.CreateSequence();
		cm.main(null);
		
	}
	
//	List<MovieVO> list = new ArrayList<>();
//	
//	public static void main(String[] args) {
//		
//		List<MovieVO> list = new ArrayList<>();
//		
//		Movie se = new Movie();
//		se.Movie();
//		se.MovieShow();
//		
//		ContactManager cm = new ContactManager();
//		cm.main(null);
//		
//	}
	
	void MovieShow(){
		
		for(MovieVO mv : list) {
			
		 System.out.println("순서 : "+ mv.getNum());
		 System.out.println("영화제목 : "+ mv.getTitle());
		 System.out.println("평점 : "+ mv.getRate());
		 System.out.println("장르 : "+ mv.getGenre());
		 System.out.println("감독 : "+ mv.getDir());
		 System.out.println("배우 : "+ mv.getAct());
		 System.out.println("상영시간 : "+ mv.getTimes());
		 System.out.println("개봉일 : "+ mv.getDates());
		 System.out.println();
	}
		
}
	void Movie(){
		
		int k=1;
		DataDAO dao = DataDAO.newInstance(); // 변경
		try {
		for(int i=0;i<=10;i++) {
			
			 Document doc = Jsoup.connect("https://movie.naver.com/movie/running/current.naver").get();
			
			 Elements link=doc.select("div.thumb a");
//			 Elements lin=doc.select("div.star_t1 a");
			 
//			 System.out.println(link.get(i).attr("href"));
			 
			 Document doc2=Jsoup.connect("https://movie.naver.com/movie/running/current.naver"+link.get(i).attr("href")).get();
//			 Document doc3=Jsoup.connect("https://movie.naver.com/movie/running/current.naver"+lin.get(i).attr("href")).get();
//			 System.out.println(doc3);
			 Elements review = doc2.select("div.score_result li:eq(0) div.score_reple p");
	          System.out.println(review.text());
	          
			 Elements title1 = doc2.select("h3.h_movie a");
			 Element title2 = title1.get(0);
			 
			 Elements rate1 = doc2.select("span.st_off span.st_on");
			 Element rate2 = rate1.get(0);
		
			 Elements genre1 = doc2.select("dl.info_spec dd span:eq(0)");
			 Element genre2 = genre1.get(0);
			
			 
			 Elements dir1	 = doc2.select("dl.info_spec dd:eq(3)");
			 Element dir2 = dir1.get(0);
			 
			 Elements act1 = doc2.select("dl.info_spec dd:eq(5) p");
			 Element act2 = act1.get(0);
			 String act3 = act2.text();
			 
			 String act4 = "[";
			 
			 Elements time1 = doc2.select("dl.info_spec dd span:eq(2)");
			 Element time2 = time1.get(0);
			 
			 Elements date1 = doc2.select("dl.info_spec dd span:eq(3)");
			 
			 System.out.println();
			 
			 MovieVO mvo = new MovieVO();
			 
			 mvo.setNum(k); //번호
			 mvo.setTitle(title2.text()); // 영화 제목
			 mvo.setRate(rate2.text()); // 네티즌 평점
			 mvo.setGenre(genre2.text());  // 개요 
			 mvo.setDir(dir2.text()); // 감독
			 if(!act3.contains(act4))
			 mvo.setAct(act2.text()); //출연
			 
			 mvo.setTimes(time2.text()); // 영화 시간
			 mvo.setDates(date1.text()); // 영화 개봉일
		   
			 ist.add(mvo);
			 
			 dao.MovieInsert(mvo);
			 
			 k++;
			} 
		}catch (IOException e) {
			e.printStackTrace();
		} 		
	}
}