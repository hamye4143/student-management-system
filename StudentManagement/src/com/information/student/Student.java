package com.information.student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.information.common.Common;	//다른 패키지에있는 클래스들 import
import com.information.common.DBManager;
import com.information.common.ExcelWriter; 
import com.information.gui.Gui2; //Gui2의 class를 import

public class Student implements Common{	//Student 클래스는 Common 인터페이스에 정의된 추상메서드를 구현한다
	/* 해당 클래스에서만 접근이 가능하도록 접근 제어자 private로 */
	private DBManager db = null	;//레퍼런스 변수가 db인  DBManager타입 객체 생성
	private Scanner scanner = null; 
	
	public Student(DBManager db){	//매개변수가 있는 생성자
		this.db = db;	//위에있는 db = 매개변수 db 연결 (this는 현재 클래스의 인스턴스 의미)
		scanner = new Scanner(System.in);	// 문자 입력을 인자로 Scanner 생성
	}
	@Override
	public void execution(String mode){	//추상 메소드 구현
		//1.성적입력 2.성적조회
		switch(mode){
		case "1":
			insert();	//insert()함수 수행
			break;
		case "2":
			System.out.println("1)학생선택조회");
			System.out.println("2)전체학생조회");
			System.out.print("메뉴를 선택하세요.(1~2) : ");
			String mode2 = scanner.nextLine(); //키보드 입력
			if(mode2.contains("1")){	//contains()는 문자열이 포함되어 있는지 확인하는 기능-->1이 포함되어있으면
				select();	//select()함수 수행
			}
			else if(mode2.contains("2")){//-->2가 포함되어있으면
				selectAll();	//selectAll()함수 수행
			}
			else{//다른 값을 입력했을 때
				System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
			}
			break;
		default: //다른 값을 입력했을 때
			System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
		}
	}
	@Override
	public void insert(){	//학생 성적 추가
		System.out.print("▶이름 : ");
		String name = scanner.nextLine();	//키보드 입력
		while(true) {	//해당 학생의 여러과목 성적을 입력할 경우를 고려->무한 루프
			System.out.print("▶과목 : ");
			String subject = scanner.nextLine();//키보드 입력
			System.out.print("▶성적 : ");
			String score = scanner.nextLine();//키보드 입력
			String grade = getGrade( Integer.parseInt(score));	//Integer.parseInt()-->String을 int형으로 바꾸는 형 변환 함수
			//getGrade("int")함수 실행 후 결과 값을 grade변수에 넣기
			db.insertScoreInfo(name, subject, score, grade);	//DBManager 클래스의 insertScoreInfo 메서드 호출-->성적 DB INSERT
			
			//메뉴선택
			System.out.println("1)과목추가입력");
			System.out.println("2)메인메뉴로 돌아가기");
			System.out.print("메뉴를 선택하세요(1~2) : ");
			String menu = scanner.nextLine();//키보드 입력
			if(menu.contains("1")) {//-->1이 포함되어있으면
				continue; //while문 시작부분으로 간다
			}else if(menu.contains("2")) {	//-->2가 포함되어있으면
				break;	//while문 나가기
			}else{	//다른 값을 입력했을 때
				System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
				break;//while문 나가기
			}
		}
	}
	//private로 메소드 정의 -->해당 클래스에서만 접근이 가능
	private String getGrade(int score){	//학점을 알려주는 함수
		String grade = "F";
		if(95 <= score){	//점수가 95보다 높으면
			grade = "A+"; //학점은 A+
		}else if(90 <= score)	//점수가 90보다 높으면
		{
			grade = "A";	//학점은 A
		}else if(85 <= score)//점수가 85보다 높으면
		{
			grade = "B+";	//학점은 B+
		}else if(80 <= score)	//점수가 80보다 높으면
		{
			grade = "B";	//학점은 B
		}else if(75 <= score)	//점수가 75보다 높으면
		{
			grade = "C+";	//학점은 C+
		}else if(70 <= score)//점수가 70보다 높으면
		{
			grade = "C";//학점은 C
		}else if(65 <= score)//점수가 65보다 높으면
		{
			grade = "D+";//학점은 D+
		}else if(60 <= score)//점수가 60보다 높으면
		{
			grade = "D";//학점은 D
		}
		return grade; //String형인 변수 grade를 리턴
	}
	@Override
	public void select(){	//학생 선택조회
		String resultMode = "";
		while(true) {
			System.out.print("학생 이름 : ");
			String name = scanner.nextLine();	//키보드 입력
			//ResultSet-->db명령에 대한 반환 값
			ResultSet result = db.selectScoreInfo(name);	//db조회 호출 후 반환 값을 result에 넣기
			if(result == null ){	//db내의 데이터 값이 존재하지 않는다면
				System.out.println("등록되어 있지 않은 학생입니다.");
				System.out.println("1)다시 입력");
				System.out.println("2)메인메뉴로 돌아가기");
				System.out.print("메뉴를 선택하세요(1~2) : ");
				String menu = scanner.nextLine(); 	//키보드 입력
				if(menu == "1") {	//menu가 1이라면
					continue;	//while문 시작부분으로 간다
				}else if(menu == "2") {	//menu가 2라면
					break;	//while문 나가기
				}else{//menu가 다른 값이라면 
					System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
					break;	//while문 나가기
				}
			}
			
			ExcelWriter writer = new ExcelWriter();	//레퍼런스 변수가 writer인 ExcelWriter()타입 객체를 생성
			try {
				int index = 0;//row를 알려주는 변수
				System.out.println("-----------------------------");
				String titleList = "이름\t과목\t성적\t학점";	//타이틀
				writer.setTitle(titleList);//setTitle(문자열)메서드를 호출하여 titleList문자열을 엑셀에 값 입력하도록 한다
				System.out.println(titleList);	//titleList 문자열 출력
				while(true){//무한반복
					if(result.next()){	//row 반복문-->ResultSet 결과가 있다면 true를 리턴해주고 커서를 다음  레코드로 이동
						String context = null;
						if(index == 0) {	//첫번째 row일 경우 학생의 이름도 출력
							context = result.getString("name")+"\t"+result.getString("subject")+"\t"+result.getString("score")
							+"\t"+result.getString("grade");
						}else {	//첫번째 row 아닐 경우 학생의 이름 출력 제외
							context = "\t"+result.getString("subject")+"\t"+result.getString("score")+"\t"+result.getString("grade");
						}
						
						System.out.println(context);
						writer.setContext(context, index+1);	//setContext(context문자열, index+1값)메소드 불러오기
						//setContext()메서드를 호출하여 context문자열을 엑셀에 값 입력하도록 한다.두번째 행 부터(1)~
						index += 1;	//1증가(다음 행에 셀 값을 쓰기 위해)
						
					}
					else{	//result.next()==false라면
						System.out.println("-----------------------------");
						if(index == 0){	//아무것도 조회되지 않았기 때문에 첫번째 row그대로 내려옴
							System.out.println("등록되어 있지 않은 학생입니다.");
							System.out.println("1)다시 입력");
							System.out.println("2)메인메뉴로 돌아가기");
							System.out.print("메뉴를 선택하세요(1~2) : ");
							resultMode = scanner.nextLine();//키보드 입력
						
						}else{	//조회된 row 모두 출력 완료
							
							writer.saveFile(name);	//saveFile()함수 호출-->excel파일 저장(이름으로)
							System.out.println("1)다른학생 조회");
							System.out.println("2)메인메뉴로 돌아가기");
							System.out.print("메뉴를 선택하세요(1~2) : ");
							resultMode = scanner.nextLine();	//키보드 입력
						}
						break;
					}
				}
				
			} catch (SQLException e) {	//SQL 관련해서 오류가 발생하면 처리
				// TODO Auto-generated catch block
				e.printStackTrace();	//에러 메세지의 발생 근원지를 찾아 단계별로 에러 출력
			}finally{
				//예외 발생 여부와 상관업싱 무조건 실행됨
				//writer.saveFile(name); // 조회가 되든 안되든 파일에 쓰기
			}
			
			if(resultMode.contains("1")) {	//다른 학생을 조회하기 위해 반복문 처음으로 돌아감
				continue;	
			}else if(resultMode.contains("2")) {	//반복문 종료
				break;
			}else{	//다른 값을 입력했다면
				System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
				break;	//반복문 종료
			}
		}
	}
	
	public void selectAll(){	//전체 학생 조회
		ResultSet result = db.selectAllScoreInfo();	//db조회 호출 그 값을 result에 넣기
		Gui2 frm = new Gui2();	//레퍼런스 변수가 frm인 Gui2타입 객체 생성 -->GUI도 구현시킨다
		try {
			if(result == null ){	//db내의 데이터 값이 존재하지 않는다면
				System.out.println("등록되어 있는 정보가 없습니다.");
				System.out.println("1)메인메뉴로 돌아가기");
				System.out.print("메뉴를 선택하세요(1) : ");
				String menu = scanner.nextLine();//키보드 입력
				if(menu.contains("1")) {	//menu문자열이 1을 포함하고 있다면
					return;	//함수 종료
				}else{
					System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
					return;	//함수 종료
				}
			}
			
			ExcelWriter writer = new ExcelWriter();//ExcelWriter()타입 객체를 생성-->엑셀에 출력되게 함
			System.out.println("=====================Student Score List=====================");
			String titleList = "이름\t과목\t성적\t학점\t총점\t등수";
			writer.setTitle(titleList);//setTitle()메서드를 호출하여 titleList문자열을 엑셀에 값 입력하도록 한다
			System.out.println(titleList);//titleList 문자열 출력
			System.out.println("============================================================");
			boolean isFirst = false;//첫번째 row인것 확인하기 위해
			String name = "";
			int index = 1; //등수
			int excelIndex = 0; //excelIndex변수 =>excel의 행
			while(true){
				if(result.next()){	//row 반복문-->ResultSet 결과가 있다면 true를 리턴해주고 커서를 다음  레코드로 이동
					String context = null;
					/*getString("컬럼명")-->현재 행에서 지정된 열 이름의 값을 검색하여 Java 프로그래밍 언어의 문자열로 반환
					매개변수-->db테이블의 열 이름 */
					if(!name.equals(result.getString("name"))) { //컬럼명이 name인 값 검색하여 문자열로 반환한 값이  ""와 같지 않으면
						isFirst = false;	//첫번째 행이다
					}
					
					if(!isFirst) { //isFirst값이 false 일 때 실행(첫번째 줄일 때) //첫번째 행일 때 등수까지 같이 출력
						name = result.getString("name");//컬럼명이 name인 값 검색하여 변수 name에 넣기
						context = name+"\t"+result.getString("subject")+"\t"+result.getString("score")+"\t"+
								result.getString("grade")+"\t"+result.getString("sum")+"\t"+index; //등수 포함
						//컬럼명이 name, subject, score, grade, sum인 값을 검색하여 출력해준다
						isFirst = true;//isFirst값을 true로 바꾸어 '첫줄이 아니다' 라는 뜻
						index += 1; //등수 1증가 (sum을  기준으로 내림차순했기 때문에 순차적으로 1증가시킴)
					}else { //isFirst의 값이 true일 때 (첫 줄이 아닐 때)
						//컬럼명이 subject, score, grade인  값을 검색하여 출력해준다
						context = "\t"+result.getString("subject")+"\t"+result.getString("score")+"\t"+
								result.getString("grade");//getString은 문자열을 가져오는 함수 
					}
					
					System.out.println(context);//context문자열 출력
					writer.setContext(context, excelIndex+1);//setContext()메서드를 호출하여 context문자열을 엑셀에 값 입력하도록 한다.두번째 행 부터(1)~
					excelIndex += 1;	//1증가(다음 행에 셀 값을 쓰기 위해)
					
				}else{	//result.next()==false라면 (결과가 없다면)
					System.out.println("============================================================");
					if(isFirst == false){	//아무것도 조회되지 않았기 때문에(반환 값 없다) 첫번째 row그대로 내려옴
						System.out.println("등록되어 있는 정보가 없습니다.");
					}else{//isFirst ==true(첫번째 줄이 아니다)
						
						//saveFile호출-->excel파일 저장
						writer.saveFile("ALL");//file이름 지정할때 All도 들어감
					}
					System.out.println("1)메인메뉴로 돌아가기");
					System.out.print("메뉴를 선택하세요(1) : ");
					String menu = scanner.nextLine();	//키보드 입력	
					if(menu.contains("1")) {
						return;
					}else{
						System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
						return;
					}
				}
			}
		} catch (SQLException e) {	//SQL 관련해서 오류가 발생하면 처리
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//쓰지 않을 인터페이스의 추상메소드
	public void update(){
		return;
	}
	
	public void delete(){
		return;
	}
	
}
