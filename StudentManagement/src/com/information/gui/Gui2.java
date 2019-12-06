package com.information.gui;

import java.sql.*;

import javax.swing.*;	//JButton, JFrame, JLabel,JTextField 등을 사용하기 위해 javax.swing 패키지의 클래스들 import
import com.information.common.DBManager;

import java.awt.*;//TextField,Label,Button,CheckBox 등 을 사용하기 위해 import,
import java.awt.event.*;//이벤트 클래스나 이벤트 리스너를 사용하려면 java.awt.event 패키지의 클래스들 import
import java.util.Vector;//Vector클래스를 사용하므로 import

/*JFrame을 상속받아야한다 (JFrame-->스윙에서 프레임 역할을 하는 클래스/일종의 화면 창 )*/
public class Gui2 extends JFrame {	//JFrame 클래스 상속
	//Vector클래스-->(가변 길이의 배열)자바에서는 동적인 길이로 여러 데이터형을 저장하기 위해 Vector 클래스를 제공
	Vector<Vector> out;	//벡터를 요소로 사용할 생성
	Vector<String> in, title;	//문자열 벡터 생성
	
	//DB작업 관련 변수...
	Connection conn;	//mysql 연결을 위한 Connection 객체 생성//쿼리를 수행하기 전에 Connection 객체를 얻어야함
	Statement stat;		//Statement객체를 생성하여 질의 수행(Statement란 명령 문장을 의미)
	
 	
 	public Gui2() {	//생성자
 		//JFrame 생성자를 부른다
 		super("학생성적Table");	//문자열을 매개변수로 받는 부모클래스(JFrame)의 생성자를 호출-->프레임 타이틀바 텍스트 지정
 		title=new Vector<String>();	//문자열 벡터 생성
 		out=new Vector<Vector>();	//벡터를 요소로 사용할 생성
 		
 		/*title에 텍스트추가*/
 		title.add("이름");
 		title.add("과목");
 		title.add("점수");
 		title.add("학점");
 		title.add("총점");
 		
 		
 		getData();//getData()함수 불러오기
 		
 		/*JTable클래스는 표 형식으로 데이터 보여줌(테이블 구성)*/
 		JTable table= new JTable(out, title);	//테이블 객체 생성
 		//JScrollPane에 JTable 담기
 		JScrollPane scrollPane = new JScrollPane(table);	//스크롤바 만들기
 		
 		/*Container란 --> 창의 역할. GUI 프로그램을 만들 때 컴포넌트들을 담을 장소. Container 위에 Component들이 올려짐*/
 		Container c = getContentPane(); //내용이 들어가는 판(창 역할) //컨텐트팬 알아내기
 		//컨테이너에 scrollPane추가
 		c.add(scrollPane, BorderLayout.CENTER);	//BorderLayout(배치관리자)->컨테이너 공간을 나누고 지정역역에 컴포넌트 배치
 		
 		setDefaultCloseOperation(EXIT_ON_CLOSE);	//윈도우창 종료시 프로세스 닫기
 	
 		addWindowListener(new WindowAdapter() {
 			/*Adapter 클래스 --> 추상메소드를 단순 리턴하도록 미리 구현해 놓은 클래스를 제공하는 클래스
 			따라서 Adapter클래스를 사용하면 리스너인터페이스와 달리 관계없는 메소드들을 구현 할 필요가 X*/
 			
 			@Override //ActionListener인터페이스에 있는 추상 메소드를 구현한다.
 			public void windowClosing(WindowEvent w) {
 				//windowClosing(WindowEvent)는 윈도우의 시스템 메뉴에서 윈도우 닫기를 시도할 때 이 메소드가 호출됨
 				try {
 					stat.close();	//statement객체 close
 					conn.close();	//connection 객체 close
 					
 					setVisible(false); //화면에 보이지 않게 하기
 					dispose(); //화면 띄우는데 사용했던 자원 반납
 					System.exit(0); //프로세스 종료
 
 				}catch(Exception e) {	//예외가 발생하면 처리
 					
 				}
 			}
 		});
 		pack();//pack()은 프레임내에 윈도우의 사이즈를 맞추는 작업
 		setVisible(true);	//프레임 보이기
 	}
 	//DB에서 데이터를 가져와서 out벡터에 넣어주기
 	public void getData() {
 		//1~6
 		
 		
 		try {
 			//1. driver 등록
 			Class.forName(DBManager.JDBC_DRIVER);//DBManger클래스에 있는 변수 사용 
 			
 			//2.connection 얻기(네트워크 연결)
 			conn=DriverManager.getConnection(DBManager.DB_URL, DBManager.USER_NAME, DBManager.PASSWORD);
 			//3. statement 얻기
 			stat = conn.createStatement();
 			
 	
 			//이중 select 문으로 점수의 합을 구하여 기존 학생성적 테이블과 함께 조회되도록 함
			//n 테이블의 셀렉트 문: 먼저 이름으로 그룹지어진 점수의 합을 계산하고 s테이블에 이름이 같은 행에 계산된 합을 같이 조회될수 있도록 /내부 select문이 먼저 수행
			//ORDER BY-->sum을  기준으로 내림차순 
 			String query="SELECT s.name, s.subject, s.score, s.grade, n.sum \r\n" + 
			"FROM student_score s, \r\n" + 
			"(SELECT name, SUM(score) as sum FROM student_score group by name) n\r\n" + 
			"WHERE s.name = n.name\r\n" + 
			"ORDER BY n.sum DESC";
 			
 			//4.query 실행 후 결과 집합 얻기
 			ResultSet rs= stat.executeQuery(query);
 			
 			ResultSetMetaData rsmd = rs.getMetaData();	//ResultSetMetaDatas는 SQL로 받아온 데이터 정보를 조회/출력하는 용도로 사용됨
 			//ResultSet 인터페이스 객체의 getMetaDat()를 호출하면 해당 REsultSet과 관련된 메타 데이터 얻는다
 			
		
 			
 			
 			//5. 결과집합 처리
 			
 			//Gui2 설명 듣기
 			while(rs.next()) {	//db내의 데이터 값이 존재하는 동안
 				in = new Vector<String>();	//문자열 벡터 생성
 				
 				/*getString("컬럼명")-->현재 행에서 지정된 열 이름의 값을 검색하여 Java 프로그래밍 언어의 문자열로 반환
				매개변수-->db테이블의 열 이름 */
 				
 				for(int i=1; i<=rsmd.getColumnCount(); i++) {	//컬럼 수 까지 
 					in.add(rs.getString(i));	 //컬럼 값  출력 후 add
			
 				}
 				
 				out.add(in);	//out출력
 				
 				
 			}//while
 			}catch(Exception e) {//예외가 발생하면 예외 상황을 처리한다.
 				e.printStackTrace();//printStackTrace는 가장 자세한 예외 정보를 제공
 			}
 		}
 		
 	}
 	