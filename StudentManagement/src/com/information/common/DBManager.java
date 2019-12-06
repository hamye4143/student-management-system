package com.information.common;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;	
import java.sql.SQLException;
import java.sql.Statement;	

/*
<JDBC 사용방법>
1) import java.sql.*;

2) 드라이버 로드

3) mysql 연결을 위한 Connection 객체 생성

4) Statement 객체를 생성하여 질의 수행(Statement란 명령 문장을 의미)

5) 질의 결과가 있다면 ResultSet 객체를 생성하여 결과 저장(ResultSet-->db명령에 대한 반환 값)

6) 프로그램 내에서 추가적인 로직이 있다면 로직을 수행한 후 JDBC 연결 과정에서 필요했던 객체들을 close

*/

public class DBManager {
	
	/*why public 접근 지정자?-->다른 패키지에 있는 Gui_change와 Gui2클래스에서도 사용 하기 위해*/
	public final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; //DB에 접속하기 위한  JDBC 드라이버
	public final static String DB_URL = "jdbc:mysql://localhost/study_db?&serverTimezone=UTC&userSSL=false";
	//로컬 DB의 접속정보(로컬에 생성된 study_db 데이터베이스에 serverTimezone=UTC로 로컬시간과 DB의 시간의 맞추어 접속)
	public final static String USER_NAME = "root"; //사용자 계정
	public final static String PASSWORD = "jh8553jh"; //사용자 계정의 패스워드
	
	// MySql에 사용하는여러 객체를 만든다
	Connection connection = null;	//mysql 연결을 위한 Connection 객체-->sql문장을 실행시키기 전에 우선 Connection객체가 필요
    Statement state = null;	//SQL 문장을 정의하고 실행시킬 수 있는 Statement 객체
	
	public DBManager(){	//생성자
		//예외처리
		//try문안의 문장들을 수행 중 해당예외가 발생하면 예외에 해당되는 catch문이 수행된다.
		try {
			
			//DB 초기화
			//1. 드라이브 로딩
			Class.forName(JDBC_DRIVER);//Class 클래스의 forName()함수를 이용해서 해당 클래스를 메모리로 로드 하는 것
			//2. 연결하기 
			//드라이버 매니저에게 Connection객체 달라고 요청
			connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD); //URL, ID, password를 입력하여 데이터베이스에 접속
			//3. 쿼리 수행을 위한 Statement 객체 생성
			state = connection.createStatement(); //세션 생성  
			//Connection 클래스의 createStatment()메소드를 호출함으로써 Statement객체 얻어짐
			System.out.println("MYSQL CONNECT SUCCESS");//연결 성공
	        
		} catch (ClassNotFoundException e) {//DB 접속 오류시 정상적으로 접속 종료할 수 있도록 처리
			// TODO Auto-generated catch block
			e.printStackTrace(); //printStackTrace는 가장 자세한 예외 정보를 제공
			close();
		} catch (SQLException e) { //SQL 관련해서 오류가 발생하면 처리
			// TODO Auto-generated catch block
			e.printStackTrace(); //에러 메세지의 발생 근원지를 찾아 단계별로 에러 출력
			close();
		} 
	}
	
	/*관리자 모드 */
	public ResultSet selectStudentInfo(String studentId){ //학생정보 조회 함수
		
		ResultSet result = null; //질의 결과가 있다면 ResultSet 객체를 생성하여 결과 저장(쿼리의 결과값)
		try {
			//학생정보 테이블에서 학번으로 조회
			//4. SQL 쿼리 작성
			String sql = String.format("SELECT * FROM student_info WHERE studentid='%s'", studentId);
			//특정조건: student_info 테이블에서 studentid가 입력한 값인 레코드를 가져온다.
			
			//5.쿼리 수행
			//레코드들은 ResultSet객체에 추가된다
			result = state.executeQuery(sql); //쿼리 실행후 값을 result에 넣기	
		} catch (SQLException e) { //SQL 관련해서 오류가 발생하면 처리
			// TODO Auto-generated catch block
			e.printStackTrace();//에러 메세지의 발생 근원지를 찾아 단계별로 에러 출력
		}
		return result; //result반환(쿼리의 결과값=반환값이 있으므로)
	}
	
	public ResultSet selectAllStudentInfo(){ //학생 정보 전체 조회
		//질의 결과가 있다면 ResultSet 객체를 생성하여 결과 저장
			ResultSet result = null;
			try {
				//4. SQL 쿼리 작성
				String sql = String.format("SELECT name, studentid, department, address, phone FROM student_info");
				//student_info 테이블에서  모든 데이터(컬럼)의 요소들을 가져온다.
				
				//5. 쿼리 수행
				//레코드들은 ResultSet 객체에 추가된다.
				result = state.executeQuery(sql);//쿼리 실행
				
			} catch (SQLException e) {//SQL 관련해서 오류가 발생하면 처리
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;//result반환(반환값이 있으므로)
		}
	
	public void deleteStudentInfo(String studentId){	//학생정보 삭제함수
		try {
			//학생정보테이블에서 해당 학번의 학생 삭제
			
			//4. SQL 쿼리 작성-->특정조건: student_info 테이블에서 studentid가 입력한 값인 레코드(데이터)를 삭제한다.
			String sql = String.format("DELETE FROM student_info WHERE studentid='%s'", studentId);
			
			//5. 쿼리 실행 및 결과 처리
			//select와 달리 delete는 반환되는 데이터들이 없으므로
			state.executeUpdate(sql);//ResultSet 객체 필요없음, 바로 state.executeUpdate()메서드를 호출하면된다//아무 것도 반환하지 않는 sql문 실행할 때 이 메서드 사용
		} catch (SQLException e) {	//SQL 관련해서 오류가 발생하면 처리
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertStudentInfo(String name, String id, String dep, String address, String phone){
		try {
			//학생정보테이블에 학생 추가
			
			//4. SQL 쿼리 작성-->student_info 테이블에서 values값(입력한 값)들의  데이터를 삽입한다.
			String sql = String.format("INSERT INTO student_info (name, studentid, department, address, phone) VALUES "
					+ "('%s', '%s', '%s', '%s', '%s')",name, id, dep, address, phone);
			//5. 쿼리 실행 및 결과 처리
			//select와 달리 insert는 반환되는 데이터들이 없으므로
			state.executeUpdate(sql);//ResultSet 객체 필요없음, 바로 state.executeUpdate()메서드를 호출하면된다 //아무 것도 반환하지 않는 sql문 실행할 때 이 메서드 사용
			//결과는 db에 새로운 데이터가 추가됨
		} catch (SQLException e) {	//SQL 관련해서 오류가 발생하면 처리
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateStudentInfo(String studentId, String parms){
		try {
			//학생정보테이블에 해당학번의 학생정보 수정
			
			//4. SQL 쿼리 작성-->student_info 테이블에서 studentid가 입력한 값에 해당되는 레코드를 parms으로 수정한다.
			String sql = String.format("UPDATE student_info set %s WHERE studentid = '%s'", parms, studentId);
			//student_info 테이블에서 where조건에 해당되는 레코드를 parms으로 수정한다. 
			
			//5. 쿼리 실행 및 결과 처리
			//select와 달리 update는 반환되는 데이터들이 없으므로
			state.executeUpdate(sql);//ResultSet 객체 필요없음, 바로 state.executeUpdate()메서드를 호출하면된다 //아무 것도 반환하지 않는 sql문 실행할 때 이 메서드 사용
		} catch (SQLException e) {//SQL 관련해서 오류가 발생하면 처리
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*학생 모드 */
	
	public ResultSet selectScoreInfo(String name){	//학생성적 조회 함수
		ResultSet result = null;//질의 결과가 있다면 ResultSet 객체를 생성하여 결과 저장
		try {
			//학생성적 테이블에 이름으로 정보 조회
			
			//4. SQL 쿼리 작성-->student_score 테이블에서 name이 입력한 값인 레코드를 가져온다.
			String sql = String.format("SELECT name, subject, score, grade FROM student_score WHERE name='%s'", name);
			//5.쿼리 수행
			//레코드들은 ResultSet객체에 추가된다
			result = state.executeQuery(sql);	//쿼리 실행
		} catch (SQLException e) {	//SQL 관련해서 오류가 발생하면 처리
			// TODO Auto-generated catch block
			e.printStackTrace();//에러 메세지의 발생 근원지를 찾아 단계별로 에러 출력
		}
		return result;//result반환(반환값이 있으므로)
	}
	
	public ResultSet selectAllScoreInfo(){//학생 성적 전체 조회
		ResultSet result = null;//질의 결과가 있다면 ResultSet 객체를 생성하여 결과 저장
		try {
			//4. SQL 쿼리 작성
			
			//이중 select 문으로 점수의 합을 구하여 기존 학생성적 테이블과 함께 조회되도록 함
			//n 테이블의 셀렉트 문: 먼저 이름으로 그룹지어진 점수의 합을 계산하고 s테이블에 이름이 같은 행에 계산된 합을 같이 조회될수 있도록 /내부 select문이 먼저 수행
			//ORDER BY-->sum을  기준으로 내림차순 
			String sql = String.format("SELECT s.name, s.subject, s.score, s.grade, n.sum \r\n" + 
					"FROM student_score s, \r\n" + 
					"(SELECT name, SUM(score) as sum FROM student_score group by name) n \r\n" + 
					"WHERE s.name = n.name\r\n" + 
					"ORDER BY n.sum DESC"); 
			
			/*내부 셀렉트문-->(SELECT name, SUM(score) as sum FROM student_score group by name) 
			 * -->stuent_scroe 테이블로부터 이름 별 점수의 합을 검색 (이름으로 그룹지어서)--> n테이블이라고 정의*/
			/*외부 select문 --> s 테이블의 이름과 n 테이블의 이름이 같다면 s테이블로부터 이름, 과목, 점수, 학점, n테이블의 합계를 검색한다.
			 * s테이블에 이름이 같은 행에 계산된 합을 같이 조회될수 있도록*/
	

			//5.쿼리 수행
			//레코드들은 ResultSet객체에 추가된다
			result = state.executeQuery(sql);//쿼리 실행
		} catch (SQLException e) {//SQL 관련해서 오류가 발생하면 처리
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public void insertScoreInfo(String name, String subject, String score, String grade){
		try {
			//학생성적테이블에 성적 추가
			//4. SQL 쿼리 작성-->student_score 테이블에서 values값(입력한 값)들의  데이터를 삽입한다.
			String sql = String.format("INSERT INTO student_score (name, subject, score, grade) VALUES ('%s', '%s', '%s', '%s')",
					name, subject, score, grade);
			
			//5. 쿼리 실행 및 결과 처리
			//select와 달리 insert는 반환되는 데이터들이 없으므로
			state.executeUpdate(sql);//ResultSet 객체 필요없음, 바로 state.executeUpdate()메서드를 호출하면된다 //아무 것도 반환하지 않는 sql문 실행할 때 이 메서드 사용
		} catch (SQLException e) {//SQL 관련해서 오류가 발생하면 처리
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*관리자 로그인 */
	public ResultSet selectManagerInfo(String id, String pw){
		ResultSet result = null;//질의 결과가 있다면 ResultSet 객체를 생성하여 결과 저장(ResultSet-->db명령에 대한 반환 값)
		try {
			//관리자 로그인을 위해 해당정보가 있는지 테이블 조회
			//쿼리 작성-->입력한 id값과 password값이 테이블의 값과 동일하다면 아이디와 ,비밀번호를 조회한다
			String sql = String.format("SELECT id, password FROM manager_info WHERE id='%s' and password='%s'", id, pw);
			//쿼리 실행	
			//레코드들은 ResultSet객체에 추가된다
			result = state.executeQuery(sql);//쿼리 수행
		} catch (SQLException e) {//SQL 관련해서 오류가 발생하면 처리
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;//result반환(반환값이 있으므로)
	}
	
	
	public void close(){ //DB세션 종료 
		try {
			//DB세션 종료
			if (state != null){
				state.close();//statement객체 close
			}
			if (connection != null){
				connection.close();//connection 객체 close
			}
			System.out.println("MYSQL CONNECT CLOSE"); //close
		} catch (SQLException e) {	//SQL 관련해서 오류가 발생하면 처리
			// TODO Auto-generated catch block
			e.printStackTrace();//에러 메세지의 발생 근원지를 찾아 단계별로 에러 출력
		}
	}
}
