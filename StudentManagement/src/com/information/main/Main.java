package com.information.main;

import java.util.Scanner;

import com.information.common.DBManager;
import com.information.management.Management;
import com.information.student.Student;

public class Main {
	public static void main(String[] args) throws Exception{	
		//예외 던지기 (throws)-->throws구문으로 인해 Exception의 예외를 처리해야할 대상이 main문이 아니라 뒤로 미룸
		//-->예외가 발생한 메소드를 호출 한 곳으로 예외 객체를 넘김
		
		//DBManager 클래스의 객체를 생성후 객체의 주소를 참조변수dbManager에 저장한다.
		DBManager dbManager = new DBManager(); //DBManager 객체 생성 -->레퍼런스 변수 dbManager가 DBManger 객체를 가리킴
		
		try{	//예외 처리 
			while(true){
				Scanner scanner = new Scanner(System.in); // 문자 입력을 인자로 Scanner 생성
				System.out.println("");
				System.out.println("#########################");
				System.out.println("1)관리자메뉴");
				System.out.println("2)학생메뉴");
				System.out.println("3)종료");
				System.out.println("#########################");
				System.out.print("메뉴를 선택하세요.(1~3) : ");
				String mode = scanner.nextLine();	//키보드 문자 입력
				
				switch(mode){
				case "1":	//1을 입력했다면
					Management management = new Management(dbManager);	//레퍼런스 변수가 management인  Management타입 객체 생성
					management.login();	//login()메소드 실행
					break;
				case "2":	//2를 입력했다면
					System.out.println("1)학생성적추가");
					System.out.println("2)성적조회");
					System.out.print("작업을 선택하세요.(1~2) : ");
					mode = scanner.nextLine();	//키보드 입력
					Student student = new Student(dbManager);	//레퍼런스 변수가 student인  Student타입 객체 생성
					student.execution(mode);//매개변수를 입력값으로 받는 execution메소드 실행
					break;
				case "3":	//3을 입력했다면 
					System.out.println("프로그램을 종료합니다.");
	                System.exit(0); //프로그램 종료
	                
				default:	//나머지를 입력했다면
					System.out.println("잘못입력하셨습니다.");
					continue;	//while문 처음으로 다시 감
				}
			}
		} catch (Exception e) {	//예외가 발생하면 예외 상황을 처리한다.
			e.printStackTrace();	//printStackTrace는 가장 자세한 예외 정보를 제공
		} finally {	//예외 발생 여부와 상관업싱 무조건 실행됨
			dbManager.close();	//-->DB세션 종료 
		}
	}
}