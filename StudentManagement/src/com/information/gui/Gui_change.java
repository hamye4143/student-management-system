package com.information.gui;
import com.information.common.DBManager;
import java.sql.*;
import javax.swing.*;			//JButton, JFrame, JLabel,JTextField 등을 사용하기 위해 javax.swing 패키지의 클래스들 import
import javax.swing.table.*;		//JTable 을 사용하기 위해 import
import java.awt.*;				//awt는 그래픽 처리를 위해 사용/TextField,Label,Button,CheckBox,Container 등 을 사용하기 위해 import
import java.awt.event.*;		//이벤트 클래스나 이벤트 리스너를 사용하려면 java.awt.event 패키지의 클래스들 import
import java.util.Vector;		//Vector클래스를 사용하므로 import		

//JFrame을 상속받아야한다 (JFrame-->스윙에서 프레임 역할을 하는 클래스/일종의 화면 창 )
public class Gui_change extends JFrame implements ActionListener{ //ActionListener은 이벤트 처리 해주는 리스너 인터페이스 
	//Gui_change 클래스가  ActionListener 인터페이스를 상속받고 추상메소드를 통해 구현한다.
	Vector out,title;	//Vector클래스-->자바에서는 동적인 길이로 여러 데이터형을 저장하기 위해 Vector 클래스를 제공
	/*Vector 클래스는 객체에 대한 참조값을 저장하는 배열이므로 다양한 객체들이 하나의 Vector에 저장될 수 있고 길이도 필요에 따라 증감 가능*/
	JTable table; //JTable클래스는 표 형식으로 데이터 보여줌(테이블 구성)
	DefaultTableModel model; //DefaultTableModel 생성 후에 JTable에 넣어줄 수 있다(테이블에 행 단위로 쓰기 용이)
	JButton add, del, update, clear; //JButton클래스는  윈도우 버튼을 만들어주는 클래스
	JTextField tid, tdepart,tphone,tname, taddress; //JTextField클래스는 윈도우에 텍스트 필드 넣어주는 클래스
	JLabel lid, ldepart,lphone,lname, laddress; //JLabel클래스는 윈도우에 라벨 넣어주는 클래스

	//DB 관련변수
	Connection con;		//mysql 연결을 위한 Connection 객체 생성//쿼리를 수행하기 전에 Connection 객체를 얻어야함
	Statement stat;		//Statement객체를 생성하여 질의 수행
	
	
	PreparedStatement pin,pdel,pup; //db사용하기 위해 //JDBC를 사용하여 테이블에 쿼리를 실행하기 위해서 PreparedStatement 객체 사용

	/*PreparedStatment는 sql문을 db가 이해하기 쉬운 형태로 해석해 놓은 것/SQL Injection(외부의 원치않은 접근)에 대한 방어 효과.안전성
 	PreparedStatement 객체는 SQL 문장이 미리 컴파일되고, 실행시간동안 인수 값을 위한 공간을 확보할 수 있다는 점에서 Statement 객체와 다름*/

 	public Gui_change() { //화면 생성하고 listener 등록	//생성자
 		super("<학생정보Table>");// JFrame("타이틀 문자열")생성자 호출
 		prepareDB();//준비 작업을 시킨다.
 		/*Vector()--> 10개의 데이터를 저장할 수 있는 길이의 객체 생성*/
 		out=new Vector(); //레퍼런스 변수가 out인  Vector타입 객체 생성(Vector 클래스는 가변 길이의 배열)
 		title = new Vector(); //레퍼런스 변수가 title인  Vector타입 객체 생성
 		
 		/*title에 추가*/
 		title.add("이름");	
 		title.add("학번");
 		title.add("학과");
 		title.add("주소");
 		title.add("전화번호");
 		
 		model = new DefaultTableModel(); //DefaultTableModel을 선언하기
 		table = new JTable(model); //JTable에 DefaultTableModel담기
 		//JScrollPane에 JTable 담기
 		JScrollPane scrollPane = new JScrollPane(table); //스크롤바 만들기
 		/*Vector 클래스는 가변 길이의 배열*/
 		Vector result = selectAll();	//selectAll()함수 결과값()을 result에 넣기
 		//System.out.println(result);//쿼리 수행 결과값이 출력됨
 		
 		
 		model.setDataVector(result,title); //setDataVector()-->테이블 그릴 때 쓰는 함수,result는 2차원 함수
 		
 		//addMouseListener 이벤트 리스너 등록
 		table.addMouseListener(new MouseAdapter() {	//익명 클래스 -->클래스 이름없이, 클래스 선언과 인스턴스 생성을 하나로 합침//클래스 파일을 만드는 귀찮음을 줄임
 			/*Adapter 클래스 --> 추상메소드를 단순 리턴하도록 미리 구현해 놓은 클래스를 제공하는 클래스
 			따라서 Adapter클래스를 사용하면 리스너인터페이스와 달리 관계없는 메소드들을 구현 할 필요가 X*/
 			public void mouseClicked(MouseEvent m) {
 				
 				//클릭한 행의 인텍스 알아내기-->JTable의 메소드
 				int index=table.getSelectedRow();
 				
 				//인텍스 이용해서 out안의 작은 벡터 in 꺼내기-->out은 JTable로 생성한 테이블을 마우스로 클릭된 상태를 확인
 				//out에는 table의 행을 클릭시 그 정보가 담겨져있다
 				Vector in = (Vector)out.get(index);   //Vector 형으로 형 변환 시켜줘야함
 				
 				/*in안에 들어 있는 이름,학번,학과,주소,전화번호 알아내서 변수에 저장 후 텍스트 필드에 넣어주기*/
 				
 				String name =(String)in.get(0);	//in에 들어있는 이름값을 가져와서 변수에 저장 
 				String id =(String)in.get(1);
 				String depart =(String)in.get(2);
 				String address =(String)in.get(3);
 				String phone =(String)in.get(4);
 				
 				/*하단에 있는 텍스트 필드*/
 				tname.setText(name);	
 				tid.setText(id);
 				tdepart.setText(depart);
 				taddress.setText(address);
 				tphone.setText(phone);
 				
 				//학번이 들어가는 텍스트 필드는 편집 불가 상태로 변경
 				tid.setEditable(false); 
 			}
 		}); //무명클래스 형식-->부모클래스 인스턴스 = new 부모클래스(){};
 		
 		JPanel panel = new JPanel(); //JPanel클래스의 객체 생성, 패널 객체화
 		//일반적으로 Frame에 컴포넌트들을 직접 붙이지 않고 Panel 이용하여 컴포넌트들을 붙임
 		
 		/*테이블의 하단부분*/
 		//JTextField는 윈도우에 텍스트 필드 넣어주는 클래스
 		tname=new JTextField(8); //행 크기 8
 		tid=new JTextField(10); //행 크기 10
 		tdepart=new JTextField(10);
 		taddress=new JTextField(10);
 		tphone=new JTextField(10);
 		
 		//JLabel은 윈도우에 라벨 넣어주는 클래스
 		lname=new JLabel("name");
 		lid=new JLabel("id");
 		ldepart=new JLabel("department");
 		laddress=new JLabel("address");
 		lphone=new JLabel("phone");
 		
 		//JButton은 윈도우 버튼을 만들어주는 클래스
 		add=new JButton("add");
 		del=new JButton("del");
 		update=new JButton("update");
 		clear=new JButton("clear");
 		
 		
 		add.addActionListener(this); // 리스너 등록	-->이벤트 걸어줌
 		del.addActionListener(this); // 리스너 등록
 		update.addActionListener(this); // 리스너 등록
 		clear.addActionListener(this); // 리스너 등록
 		
 		/*panel에 라벨 추가와 텍스트필드 추가*/
 		panel.add(lname);	//panel에 라벨(lname) 추가
 		panel.add(tname);	//panel에 텍스트필드(tname) 추가
 		panel.add(lid);		
 		panel.add(tid);
 		panel.add(ldepart);
 		panel.add(tdepart);
 		panel.add(laddress);
 		panel.add(taddress);
 		panel.add(lphone);
 		panel.add(tphone);
 		
 		/*panel에 버튼 추가*/
 		panel.add(add);		
 		panel.add(del);
 		panel.add(update);
 		panel.add(clear);
 		
 		/*Container란 --> 창의 역할. GUI 프로그램을 만들 때 컴포넌트들을 담을 장소. Container 위에 Component들이 올려짐*/
 		Container c = getContentPane();	//프레임에 연결된 컨텐트팬을 알아낸다 //여기에 패널달기
 		c.add(new JLabel("학생정보Table",JLabel.CENTER),"North");//패널을 컨테이너에 각각 삽입하여 배치관리하여 설정
 		//컨테이너에 scrollPane추가
 		c.add(scrollPane,BorderLayout.CENTER);	//BorderLayout(배치관리자)->컨테이너 공간을 나누고 지정역역에 컴포넌트 배치
 		//컨테이너에 패널 추가
 		c.add(panel,BorderLayout.SOUTH);	//SOUTH위치
 		//Window를 상속받는 모든 컴포넌트에 대해 윈도우 활성화, 비활성화, 아이콘화, 아이콘에서 복구, 윈도우 종료 등
 		
 		addWindowListener(new WindowAdapter() {
 			/*Adapter 클래스 --> 추상메소드를 단순 리턴하도록 미리 구현해 놓은 클래스를 제공하는 클래스
 			따라서 Adapter클래스를 사용하면 리스너인터페이스와 달리 관계없는 메소드들을 구현 할 필요가 X*/
 			
 			@Override //ActionListener인터페이스에 있는 추상 메소드를 구현한다.
 			public void windowClosing(WindowEvent w) {
 				//windowClosing(WindowEvent)는 윈도우의 시스템 메뉴에서 윈도우 닫기를 시도할 때 이 메소드가 호출됨
 				try {
 					stat.close();//statement 객체 close
 					con.close();//connection 객체 close
 					
 					setVisible(false); //화면에 보이지 않게 하기
 					dispose();	//화면 띄우는데 사용했던 자원 반납
 					System.exit(0);	//프로세스 종료
 
 				}catch(Exception e) {
 				}
 			}
 		});
 }
 	

 //db 준비작업: 드라이버 등록, Connection,Statement,PreparedStatement만들기
 /**/
 public void prepareDB() {
	 try {
		 
		 Class.forName(DBManager.JDBC_DRIVER);//DBManger클래스에 있는 변수 사용 //드라이버 등록
		 //DriverManager 객체로부터 Connection객체를 얻어온다
		 con = DriverManager.getConnection(DBManager.DB_URL, DBManager.USER_NAME, DBManager.PASSWORD);
	
		 //Connection 객체의 createStatement()메서드를 호출해서 쿼리를 실행할 수 있는 Statement객체(stat)를 얻는다
		 stat=con.createStatement();//select 때 사용
		 
		 
		 //insert문(데이터삽입) --> insert into 테이블이름 values(속성값_리스트); 
		 //?에 해당하는 파라미터에 들어갈 만한 외부의 입
		 //결과값을 받고 결과값 리던
		 pin = con.prepareStatement("insert into student_info values(?,?,?,?,?)"); //""안에 있는 것: sql쿼리
		 //prepareStatement에서 해당 sql을 미리 컴파일한다.
		 
		 //delete문(데이터삭제)-->delete from 데이블이름 where 조건;
		 pdel = con.prepareStatement("delete from student_info where studentid=?");//name,id,depart,address,phone
		 
		 //update문(데이터수정)-->update 데이블이름 set 속성_이름1=값1, 속성_이름2=값2.... where 조건;
		 pup=con.prepareStatement("update student_info set name=?,department=?,address=?,phone=? where studentid=?");
	 }catch(Exception e) {
		 
	 }
 }
 //db에서 데이터 가져와서 out에 넣은 후에 return해 줌
 public Vector selectAll() {
	 out.clear();//Java.util.Vector.clear() method는 Vector에 있는 모든 요소들을 지울 때 사용
	
	 try {
		 //쿼리 수행
		 //레코드들은 ResultSet 객체에 추가된다
		 ResultSet rs = stat.executeQuery("select * from student_info order by studentid");//쿼리 실행
		 //실행 결과 출력
		 while(rs.next()) {
			 Vector in = new Vector<String>();
			 //db에서 가져오는 데이터타입에 맞게 getString으로 호출
			 String name = rs.getString(1); //첫번째 컬럼 출력 
				/*getString("컬럼명")-->현재 행에서 지정된 열 이름의 값을 검색하여 Java 프로그래밍 언어의 문자열로 반환
				매개변수-->db테이블의 열 이름 */
			 String id = rs.getString(2);	//두번째 컬럼 출력
			 
			 String depart = rs.getString(3);//세번째 컬럼 출력
			 String address = rs.getString(4);//네번째 컬럼 출력
			 String phone = rs.getString(5);//다섯번째 컬럼 출력
			 in.add(name); //프레임에 name 추가
			 in.add(id);	//프레임에 id 추가
			 in.add(depart);	//프레임에 depart 추가
			 in.add(address);	//프레임에 address 추가
			 in.add(phone);	//프레임에 phone 추가
			 
			 out.add(in);	//out출력
			 
		 }
	 }catch(Exception e) {//예외가 발생하면 예외 상황을 처리한다.
		 e.printStackTrace();
	 }
	 return out; //out 리턴(쿼리 수행한 결과값 리턴한다)
 }
 
 
 //이벤트 처리
 //ActionListener인터페이스를 상속받은 Gui 클래스를 선언하고 
 //actionPerformed(ActionEvent e)가 리스너의 추상 메소드이다.
 //ActionEvent는 이벤트 객체

 @Override //ActionListener인터페이스에 있는 추상 메소드를 구현한다.
 public void actionPerformed(ActionEvent e) { //Action이벤트가 발생하는 경우 이 메소드가 호출됨(버튼이 클릭될 때 호출되는 메소드)
	 //추상 메소드 actionPerformed를 여기서 구현한다.
	 //Object 클래스-->java.lang패키지에 속한 클래스,모든 클래스는 강제로 Object를 상속받음/ 
	 //Object만이 유일하게 아무 클래스도 상속받지 않은 최상위 클래스
	 Object o = e.getSource(); //사용자가 클릭한 버튼 알아내기
	 if(o !=clear) {	//clear 칸 안에 있던 글자만 지우는 역할
		 if(o==add) { 	//db에 insert
		 String name=tname.getText(); //getText() 를 이용해 입력된 값을 추출 
		 String id=tid.getText();
		 String depart=tdepart.getText();
		 String address=taddress.getText();
		 String phone=tphone.getText();
		 
		 insert(name,id,depart,address,phone);
	 }
	 if(o==del) {	//db에서 delete
		 String id = tid.getText();//getText() 를 이용해 입력된 값을 추출 
		 delete(id);	//입력된 값을 매개변수에 집어넣고서 delete 함수 실행
	 }
	 if(o==update) {  //수정
		 String name=tname.getText();//getText() 를 이용해 입력된 값을 추출 
		 String id=tid.getText();
		 String depart=tdepart.getText();
		 String address=taddress.getText();
		 String phone=tphone.getText();
		 
		 update(name,depart,address,phone,id); //입력값을 매개변수로 집어넣고서 update함수 실행
	 }
	 //화면에 JTable 다시 그리기
	 Vector result = selectAll();	//selectAll()함수의 결과값 저장
	 model.setDataVector(result,title);	////setDataVector()-->테이블 그릴 때 쓰는 함수
 }
//setText는 텍스트 놓기
 tname.setText(""); //텍스트 박스 내에 문자열 미리 ""로 설정
 tid.setText("");
 tdepart.setText("");
 taddress.setText("");
 tphone.setText("");
 tid.setEditable(true);//setEditable 편집 가능하게 
 
 
}
 
private void update(String name,String depart, String address, String phone,String id) {//수정 함수
	try {
		//데이터 binding
		/*setString(int index, String x)함수-->지정한 인덱스의 파라미터 값을 x로 지정한다*/
		pup.setString(1,name);	//첫번째 물음표의 값 지정
		pup.setString(2,depart);	//두번째 물음표의 값 지정
		pup.setString(3,address);	//세번째 물음표의 값 지정
		pup.setString(4,phone);	//네번째 물음표의 값 지정
		pup.setString(5,id);	//다섯번째 물음표의 값 지정
		
		//쿼리 실행 및 결과 처리
		//select문과 달리 update는 반환되는 데이터들이 없으므로 ResultSet 객체 필요 없고
		pup.executeUpdate(); //바로 pup.executeUpdate() 메서드 호출하면 된다
	}catch(Exception e) {	//예외가 발생하면 예외 상황을 처리한다.
		e.printStackTrace(); // printStackTrace는 가장 자세한 예외 정보를 제공
	}
	
}
 private void delete(String id) { //delete함수
	 try {
		 /*setString(int index, String x)함수-->지정한 인덱스의 파라미터 값을 x로 지정한다*/
		 pdel.setString(1,id); //첫번째 물음표의 값 지정
		 //select문과 달리 delete문은 반환되는 데이터들이 없으므로 ResultSet 객체 필요 없고 executeUpdate()메서드 호출
		 pdel.executeUpdate(); // 쿼리를 실행한다->삭제
	 }catch(Exception e) {	//예외가 발생하면 예외 상황을 처리한다.
		 e.printStackTrace();// printStackTrace는 가장 자세한 예외 정보를 제공
	 }
 }
 private void insert(String name, String id, String depart, String address, String phone) { //삽입 함수
	 try {
		 
		 /*setString(int index, String x)함수-->지정한 인덱스의 파라미터 값을 x로 지정한다*/
		 pin.setString(1,name);	//첫번째 물음표의 값 지정
		 pin.setString(2,id);	//두번째 물음표의 값 지정
		 pin.setString(3,depart);	//세번째 물음표의 값 지정
		 pin.setString(4,address);	//네번째 물음표의 값 지정
		 pin.setString(5,phone);	//다섯번째 물음표의 값 지정
		 //select문과 달리 insert문은 반환되는 데이터들이 없으므로 ResultSet 객체 필요 없고 executeUpdate()메서드 호출
		 pin.executeUpdate(); // 쿼리를 실행한다
	 }catch(Exception e) { //예외가 발생하면 예외 상황을 처리한다.
		 e.printStackTrace();// printStackTrace는 가장 자세한 예외 정보를 제공
	 }
 }
 }
