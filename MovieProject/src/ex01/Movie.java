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
		dao.CreateSequence();
		dao.DropSequence();
		cm.main(null);
		
	}
}