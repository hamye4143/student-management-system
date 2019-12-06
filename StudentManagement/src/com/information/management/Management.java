package com.information.management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//다른 패키지에있는 클래스들 import
import com.information.common.Common;
import com.information.common.DBManager;
import com.information.common.ExcelWriter;
import com.information.gui.Gui_change;


public class Management implements Common{	//Management 클래스는 Common 인터페이스에 정의된 추상메서드를 구현한다
	/* 해당 클래스에서만 접근이 가능하도록 접근 제어자 private로 */
	private DBManager db = null; //레퍼런스 변수가 db인  DBManager타입 객체 생성
	private Scanner scanner = null; 	
	
	public Management(DBManager db){  //매개변수가 있는 생성자
		this.db = db;	//위에있는 db = 매개변수 db 연결 (this는 현재 클래스의 인스턴스 의미)
		scanner = new Scanner(System.in);	// 문자 입력을 인자로 Scanner 생성
	}
	
	@Override
	public void execution(String mode){ //추상 메소드 구현
		//1.추가 2.수정 3.삭제 4.조회 5.전체 조회 6.Gui로 보기
		switch(mode){
		case "1":
			insert();	//insert()함수 수행
			break;
		case "2":
			update();	//update()함수 수행
			break;
		case "3":
			delete();	//delete()함수 수행
			break;
		case "4":
			select();	//select()함수 수행
			break;
		case "5":
			selectAll();	//selectAll()함수 수행
			break;
		case "6":
			//레퍼런스 변수가 frame인 Gui_change 타입 객체를 생성 -->스윙 프레임 생성
			 Gui_change frame = new Gui_change(); //이 코드를 통해 Gui_change객체는 스윙 프레임의 역할을 하게 됨
			 frame.pack();	//pack()은 프레임내에 윈도우의 사이즈를 맞추는 작업
			 frame.setVisible(true); //프레임 보이기
			 
			 break;
		default:	//다른값을 입력했다면 
			System.out.println("잘못 입력하셨습니다.");
			break;
		}
		
	}
	
	public void menu() {	//menu를 보여주는 함수
		System.out.println("===========학생 관리 프로그램==============");
		System.out.println("1)학생추가");
		System.out.println("2)학생정보수정");
		System.out.println("3)학생삭제");
		System.out.println("4)학생조회");
		System.out.println("5)학생 전체목록");
		System.out.println("6)Gui로 보기");
		System.out.println("====================================");
		System.out.print("작업을 선택하세요.(1~6) : ");
		String mode = scanner.nextLine(); //키보드 문자 입력
		execution(mode); //매개변수가 문자열인 execution() 함수 부르기
		
	}
	
	public void login(){	//로그인 함수
		System.out.println("로그인이 필요합니다. 아이디랑 패스워드를 입력해주세요.");
		while(true) {
			System.out.print("▶아이디 : ");
			String id = scanner.nextLine();//키보드 문자 입력
			System.out.print("▶패스워드 : ");
			String pw = scanner.nextLine();//키보드 문자 입력
			ResultSet result = db.selectManagerInfo(id, pw);//매개변수 id,pw인 selectManagerInfo 함수를 호출하고 리턴값 result에 저장
			try {	
				if(result == null ){	//db내의 데이터 값이 존재하지 않는다면
					System.out.println("아이디 또는 패스워드를 확인해 주세요.");
					System.out.println("1)다시 입력");
					System.out.println("2)메인메뉴로 돌아가기");
					System.out.print("메뉴를 선택하세요(1~2) : ");
					String menu = scanner.nextLine();	//키보드 문자 입력
					if(menu.contains("1")) {	//contains()는 문자열이 포함되어 있는지 확인하는 기능-->1이 포함되어있으면
						continue;	//while문 처음으로 가서 실행(다시입력)
					}else if(menu.contains("2")) {	//-->2가 포함되어있으면
						break;	//while문 나가기(메인메뉴로)
					}else{	//다른 값을 입력했을 때
						System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
						break;	//while문 나가기
					}
				}else{	//db내의 데이터 값이 존재한다면
					if(result.next()){	//아이디 또는 패스워드가 일치한다면(select결과 값과 같다면 )
						//ResultSet 결과가 있다면(true)라면 //결과가 있다면 true를 리턴해주고 커서를 다음  레코드로 이동
						menu();	//menu()함수 호출
						return;	//함수를 마침
					}else{	//result.next()==false라면 (결과가 없다면)
						//아이디 또는 패스워드가 일치하지 않다면(db검색을 했는데 검색이 안됐다면/값이 없다면)
						System.out.println("아이디 또는 패스워드를 확인해 주세요.");
						System.out.println("1)다시 입력!");
						System.out.println("2)메인메뉴로 돌아가기");
						System.out.print("메뉴를 선택하세요(1~2) : ");
						String menu = scanner.nextLine();
						if(menu.contains("1")) {	//문자열이 1을 포함한다면 
							continue;	//while문 처음으로
						}else if(menu.contains("2")) {//문자열이 2를 포함한다면 
							break;	//while문 나가기
						}else{	//위에 해당 되지 않는다면 
							System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
							break;	//while문 나가기
						}
					}
				}
			} catch (SQLException e) {	//SQL 관련해서 오류가 발생하면 처리
				// TODO Auto-generated catch block
				e.printStackTrace();	//에러 메세지의 발생 근원지를 찾아 단계별로 에러 출력
			}
				
		}
	}
	@Override
	public void update(){	//학생 정보 수정
		System.out.println("");
		while(true) {	
			System.out.print("▶정보를 수정 할 학생의 학번을 입력하세요. : ");
			String studentId = scanner.nextLine();	//키보드 문자 입력
			System.out.println("▶수정 할 항목을 선택하세요.");
			System.out.println("이름, 학번, 학과, 주소, 핸드폰(콤마(,)로 구분하여 입력) : ");
			String keys = scanner.nextLine().trim();	//trim()함수-->선행 공백 문자와 후행 공백 문자를 제거
			if(0 < keys.length()) {	//문자열 길이가 0보다 크다면
				String[] keyList = keys.split(",");	//문자열 keys를 ',' 으로 분리하여 keyList 리스트 에 넣기 
				String parms = "";
				for(String key : keyList){	//향상된 for문 사용하여 list값 출력 
					System.out.print(key + " : ");	//ex)이름, 학번 같은 값들 출력
					String item = scanner.nextLine();	//수정할 문자열 입력하기
					
					String col = "";
					if(key.contains("이름")){	//contains()는 문자열이 포함되어 있는지 확인하는 기능-->"이름"이 포함되어있다면
						col = "name"; //col 값 "name"으로 
					}else if(key.contains("학번")){
						col = "studentid";	//col 값 "studentid"으로 
					}else if(key.contains("학과")){
						col = "department";	//col 값 "department"으로 
					}else if(key.contains("주소")){
						col = "address";	//col 값 "address"으로 
					}else if(key.contains("핸드폰")){
						col = "phone";	//col 값 "phone"으로 
					}else{
						continue;
					}
					parms = parms + String.format("%s='%s' ", col,item);//업데이트하고 싶은 컬럼이랑 변수를 붙여서 보냄
					
				}
				
				parms = parms.trim();	//trim()함수-->선행 공백 문자와 후행 공백 문자를 제거
				parms = parms.replaceAll(" ", ", "); //replaceAll([기존문자],[바꿀문자]) //공백을->', '으로 바꿈
				//System.out.println(parms); //ex)name='김나래', address='천안'
				db.updateStudentInfo(studentId, parms);	//DBManager 클래스의 updateStudentInfo 메서드 호출
				System.out.println("수정이 완료되었습니다.");
				System.out.println("1)다른학생 수정");
			}
			else {//입력한 문자열 길이가 0보다 같거나 작다면
				System.out.println("잘못 입력하셨습니다.");
				System.out.println("1)다시입력");
			}
			System.out.println("2)메인메뉴로 돌아가기");
			System.out.println("3)선택메뉴로 돌아가기");
			System.out.print("메뉴를 선택하세요(1~3) : ");
			String menu = scanner.nextLine();	//키보드 입력
			if(menu.contains("1")) {
				continue;
			}
			else if(menu.contains("2")) {
				break;
			}
			else if(menu.contains("3")) {
				menu();
				break;
			}
			else{
				System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
				break;
			}
		}
	}
	@Override
	public void select(){	//학생 조회
		System.out.println("");
		String resultMode = "";
		while(true) {
			System.out.println("조회 할 학생의 학번을 입력하세요.");
			System.out.print("▶학번 : ");
			String studentId = scanner.nextLine();//키보드 입력
			
			ResultSet result = db.selectStudentInfo(studentId);//db조회 호출 그 값을 result에 넣기
			try {
				if(result == null ){//db내의 데이터 값이 존재하지 않는다면
					System.out.println("등록되어 있지 않은 학생입니다.");
					System.out.println("1)다시 입력");
					System.out.println("2)메인메뉴로 돌아가기");
					System.out.println("3)선택메뉴로 돌아가기");
					System.out.print("메뉴를 선택하세요(1~3) : ");
					String menu = scanner.nextLine();	//키보드 입력
					if(menu == "1") {
						continue;	//while문 처음으로 가서 수행
					}
					else if(menu == "2") {
						break;	//while문 나가기
					}
					else if(menu == "3") {
						menu();	//menu()함수 호출
						break;
					}
					else{
						System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
						break;
					}
				}
				
				ExcelWriter writer = new ExcelWriter();//레퍼런스 변수가 writer인 ExcelWriter()타입 객체를 생성

				int index = 0;
				System.out.println("------------------------------------------");
				String titleList = "이름\t학번\t학과\t주소\t핸드폰";
				writer.setTitle(titleList);//setTitle()메서드를 호출하여 titleList문자열을 엑셀에 값 입력하도록 한다
				System.out.println(titleList);//titleList 문자열 출력
				while(true){	//무한 반복
					if(result.next()){	//ResultSet 결과가 있다면(true)라면 //결과가 있다면 true를 리턴해주고 커서를 다음  레코드로 이동
						String context = result.getString("name")+"\t"+result.getString("studentid")+"\t"+
								result.getString("department")+"\t"+result.getString("address")+"\t"+result.getString("phone");
						
						System.out.println(context);
						writer.setContext(context, index+1);//setContext()메서드를 호출하여 context문자열을 엑셀에 값 입력하도록 한다.두번째 행 부터(1)~
						index += 1;	//1증가(다음 행에 셀 값을 쓰기 위해)

					}else{	//result.next()==false라면 (결과가 없다면)
						System.out.println("------------------------------------------");
						if(index == 0){	//아무것도 조회되지 않았기 때문에 첫번째 row그대로 내려옴
							System.out.println("등록되어 있지 않은 학생입니다.");
							System.out.println("1)다시 입력");
							System.out.println("2)메인메뉴로 돌아가기");
							System.out.println("3)선택메뉴로 돌아가기");
							System.out.print("메뉴를 선택하세요(1~3) : ");
							resultMode = scanner.nextLine();
						}
						else{	//조회된 row 모두 출력 완료
							//saveFile호출-->excel파일 저장
							writer.saveFile(studentId); //엑셀 file이름 지정할 때 studentId 값 들어감 
							System.out.println("1)다른학생 조회");
							System.out.println("2)메인메뉴로 돌아가기");
							System.out.println("3)선택메뉴로 돌아가기");
							System.out.print("메뉴를 선택하세요(1~3) : ");
							resultMode = scanner.nextLine();	//키보드 입력
						}
						break;//while문 나가기
					}
				}
			} catch (SQLException e) {//SQL 관련해서 오류가 발생하면 처리
				// TODO Auto-generated catch block
				e.printStackTrace();//에러 메세지의 발생 근원지를 찾아 단계별로 에러 출력
			}
			
			if(resultMode.contains("1")) {	//다른 학생을 조회하기 위해 반복문 처음으로 돌아감
				continue;
			}
			else if(resultMode.contains("2")) {	//반복문 종료
				break;
			}
			else if(resultMode.contains("3")) {	//menu()함수 호출
				menu();
				break;
			}
			else{	//다른 값을 입력했다면
				System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
				break;
			}
		}
	}
	@Override
	public void delete(){	//학생 정보 삭제
		System.out.println("");
		while(true) { //무한반복
			System.out.println("삭제 할 학생의 학번을 입력하세요.");
			System.out.print("▶학번 : ");
			String studentId = scanner.nextLine();	//키보드 입력
			
			db.deleteStudentInfo(studentId);	//db 데이터 삭제
			System.out.println("삭제가 완료되었습니다.");
			System.out.println("1)다른 학생 삭제");
			System.out.println("2)메인메뉴로 돌아가기");
			System.out.println("3)선택메뉴로 돌아가기");
			System.out.print("메뉴를 선택하세요(1~3) : ");
			String menu = scanner.nextLine(); //키보드 입력
			if(menu.contains("1")) {	//다른 학생을 조회하기 위해 반복문 처음으로 돌아감
				continue;
			}
			else if(menu.contains("2")) {	//반복문 종료
				break;
			}
			else if(menu.contains("3")) {	
				menu();//menu()함수 호출
				break;
			}
			else{	//다른 값을 입력했다면
				System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
				break;
			}
		}
	}
	@Override
	public void insert(){	//학생 정보 추가
		System.out.println("");
		while(true) { //무한 반복
			System.out.println("추가할 학생의 정보를 입력하세요.");
			System.out.print("▶이름 : ");
			String name = scanner.nextLine();	//키보드 문자 입력
			System.out.print("▶학번 : ");
			String id = scanner.nextLine();		
			System.out.print("▶학과 : ");
			String dep = scanner.nextLine();	 
			System.out.print("▶주소 : ");
			String address = scanner.nextLine();
			System.out.print("▶핸드폰 : ");
			String phone = scanner.nextLine();
	
			
			db.insertStudentInfo(name, id, dep, address, phone);//DBManager 클래스의 insertStudentInfo 메서드 호출-->db에 데이터 값을 삽입
			System.out.println("추가가 완료되었습니다.");
			System.out.println("1)다른 학생 추가");
			System.out.println("2)메인메뉴로 돌아가기");
			System.out.println("3)선택메뉴으로 돌아가기");
			System.out.print("메뉴를 선택하세요(1~3) : ");
			String menu = scanner.nextLine();	//키보드 문자 입력
			if(menu.contains("1")) {	
				//contains()는 문자열이 포함되어 있는지 확인하는 기능-->1이 포함되어있으면
				continue;	//다른 학생을 조회하기 위해 반복문 처음으로 돌아감
			}
			else if(menu.contains("2")) {	
				//2가 포함되어있으면
				break;	//반복문 종료
			}
			else if(menu.contains("3")) {
				//3이 포함되어있으면
				menu();//menu()메서드 호출
				break;
			}
			else{	//다른 값들 입력했다면 
				System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
				break;
			}
		}
	}
	
	public void selectAll(){	//전체목록보기
			ResultSet result = db.selectAllStudentInfo();//db조회 호출 그 값을 result에 넣기
		
			try {//예외 처리 
				if(result == null ){	//db내의 데이터 값이 존재하지 않는다면
					System.out.println("등록되어 있는 정보가 없습니다.");	
					System.out.println("1)메인메뉴로 돌아가기");
					System.out.print("메뉴를 선택하세요(1) : ");
					String menu = scanner.nextLine();	//키보드 문자 입력
					if(menu.contains("1")) {   //문자열이 1을 포함하고 있다면
						return;	
					}else{	//문자열이 1을 포함하지 않는다면
						System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
						return;	//함수를 마침
					}
				}
				
				ExcelWriter writer = new ExcelWriter(); //ExcelWriter()타입 객체를 생성-->엑셀에 출력되게 함
				System.out.println("======================Student List======================");
				
				String titleList = "이름\t학번\t학과\t주소\t전화번호";
				System.out.println(titleList);//titleList 문자열 출력
				System.out.println("========================================================");
				writer.setTitle(titleList);//setTitle()메서드를 호출하여 titleList문자열을 엑셀에 값 입력하도록 한다

				boolean isFirst = false;	//isFirst==>첫번째 row인것 확인하기 위해
				
				int excelIndex = 0;	//excelIndex=>excel의 행
				while(true){ //무한 반복
					if(result.next()){	//row 반복문-->ResultSet 결과가 있다면 true를 리턴해주고 커서를 다음  레코드로 이동
				
						String context = null;
						
						/*getString("컬럼명")-->현재 행에서 지정된 열 이름의 값을 검색하여 Java 프로그래밍 언어의 문자열로 반환
						매개변수-->db테이블의 열 이름 */
						//컬럼명이 name, studentid, department, address, phone인 값을 검색하여 출력해준다
						context=result.getString("name")+"\t"+result.getString("studentid")+"\t"+result.getString("department")+"\t"+
								result.getString("address")+"\t"+result.getString("phone");
						isFirst = true;	//isFirst값을 true로 바꾸어 '첫줄이 아니다' 라는 뜻
					
					
						System.out.println(context); //context출력
						writer.setContext(context, excelIndex+1);//setContext()메서드를 호출하여 context문자열을 엑셀에 값 입력하도록 한다.두번째 행 부터(1)~
						excelIndex += 1;	//1증가(다음 행에 셀 값을 쓰기 위해)
					}
					else{//result.next()==false라면 (결과가 없다면)
						System.out.println("========================================================");
						if(isFirst == false){	//아무것도 조회되지 않았기 때문에(반환 값 없다) 첫번째 row그대로 내려옴
							System.out.println("등록되어 있는 정보가 없습니다.");	
						}
					
						else{	//isFirst==true(첫번째 줄이 아니다)
							
							//saveFile호출-->excel파일 저장
							writer.saveFile("ALL");	//file이름 지정할때 All도 들어감
							
						}
					
						
						System.out.println("1)메인메뉴로 돌아가기");
						System.out.println("2)선택메뉴로 돌아가기");
						System.out.print("메뉴를 선택하세요(1)(2) : ");
						String menu = scanner.nextLine();	//키보드 문자 입력
						if(menu.contains("1")) {	//contains()는 문자열이 포함되어 있는지 확인하는 기능-->1이 포함되어있으면
							return;	//함수를 마침
						}
						else if(menu.contains("2")) {	//2가 포함되어있으면
							menu();//menu()함수 호출
							return;	
						}
						else{	//다른 값을 입력했을 때
							System.out.println("잘못입력하셨습니다. 메인메뉴로 돌아갑니다.");
							return;	//함수를 마침
						}
					}
				}
			} catch (SQLException e) {//SQL 관련해서 오류가 발생하면 처리
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}