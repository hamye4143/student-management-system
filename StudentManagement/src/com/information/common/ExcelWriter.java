package com.information.common;
/*엑셀로 파일 쓰기*/

/*
 * 아파치 POI(Apache POI)는 아파치 소프트웨어 재단에서 만든 라이브러리로서 마이크로소프트 오피스 파일 포맷을 순수 자바 언어로서 읽고 쓰는 기능을 제공한다.
 * (자바에서 MS파일을 읽고 쓸수있도록 지원)

 * jar 파일을 다운받았다
 * */


import java.io.File;
import java.io.FileOutputStream; //파일을 출력하기 위해 import
//바이트 스트림-->바이트를 단위로 다루는 스트림/ 문자이든 이미지 바이트든 상과없이 바이너리로 다룸
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//아파치 POI를 이용하여 엑셀에 출력하기 위해 import
import org.apache.poi.hssf.usermodel.HSSFCell;//이것을 import 하기위해 jar파일을 다운받았다
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelWriter {
	public final String PATH = "./excel/"; //엑셀파일 저장경로
	
	HSSFWorkbook writer = null;	//레퍼런스 변수가 writer인 HSSFWorkbook 타입 객체를 생성
	HSSFSheet sheet = null;		//레퍼런스 변수가 sheet인 HSSFSheet 타입 객체를 생성
	
	public ExcelWriter(){//기본 생성자
		//1차로 writer생성
		writer = new HSSFWorkbook(); //엑셀파일 생성 라이브러리
		//2차로 sheet생성
		sheet = writer.createSheet("Sheet1");  //엑셀 워크북에 엑셀시트 하나 생성(Sheet1이라는 시트)
	}
	
	public void setTitle(String titleVal){	//엑셀의 title
		//엑셀의 Row(행) 생성
		HSSFRow row = sheet.createRow((short)0); 	//출력 Row 생성 -->첫 번째 행(0)	//x행에 만들기
		String[] titleList = titleVal.split("\t"); //입력받은 스트링을 제목 리스트로 생성-->titleList에는 엑셀의 첫번째줄(타이틀 내용이 저장됨)
		//System.out.println(titleList[0]);
		//titleVal의 문자열을 \t로 분리해서 리스트안에 저장한다
		for(int i=0; i<titleList.length;i++){//titleList길이만큼 반복문을 돌려서 엑셀의 cell 생성한다
			//엑셀의 cell 생성-->첫번째 행에 Cell 설정하기
			HSSFCell cell = row.createCell((short)i );  // 해당 로우에 셀을 가로로 추가해 제목 입력//short형 255 Max
			cell.setCellValue(titleList[i]);	//접근한 셀에 값을 준다(엑셀에 값 입력) 
		}
	}
	
	public void setContext(String contextVal, int index){	//엑셀의 context
		//엑셀의 Row(행) 생성
		HSSFRow row = sheet.createRow((short)index); //출력 Row 생성-->두번째 행 부터(1)~
		String[] contextList = contextVal.split("\t");//contextVal의 문자열을 \t로 분리해서 리스트안에 저장한다
		
		for(int i=0; i<contextList.length;i++){//contextList의 길이 만큼
			//System.out.println(contextList[i]); //이러면 contextList 내용들 나 다옴
			
			//엑셀의 cell 생성-->두번째 행부터~ Cell 설정하기
			HSSFCell cell = row.createCell((short)i );    // 해당 로우에 셀을 가로로 추가해 내용 입력
			cell.setCellValue(contextList[i]);//셀 값을 준다(엑셀에 값 입력) 
		}
	}
	
	public void saveFile(String id){	//excel 파일 저장
		String fileName = getCurrentTime()+"_"+id+".xls"; //파일명=현재시간값_조회한구분값.xls
		
		//File 클래스의 생성자
		File file = new File(PATH, fileName); //엑셀파일 생성 -->PATH경로의 fileName이라는 파일에 대한 File 객체를 생성한다.
		
		try {		//예외처리 
			//내용파일 저장
			//파일 출력
			//FileOutputStream 생성자는 파일 출력 스트림 생성자//스트림을 생성한 후 file이 지정하는 파일을 생성하여 스트림 자신에 연결한다
			FileOutputStream fileOutput = new FileOutputStream(file);//file이 지정하는 파일에 출력하는 FileoutputStream생성 
			
			writer.write(fileOutput); //write()메소드를 이용하여 출력스트림으로 출력 ->파일출력
			fileOutput.close(); //출력 스트림 닫고 관련된 시스템 자원 해제
			
		} catch (IOException e) {	//예외처리-->try문안의 문장들을 수행 중 해당예외가 발생하면 예외에 해당되는 catch문이 수행된다.
			//file입출력 오류났을 때
			// TODO Auto-generated catch block
			e.printStackTrace();//printStackTrace는 가장 자세한 예외 정보를 제공
		} 
		
		System.out.println("excel 저장 경로 : "+PATH+fileName);
	
}
	
	private String getCurrentTime() {//getCurrentTime()->현재의 정확한 시간을 가지고 오는 함수
		//현재 시간을 정해진 포맷에 맞게 스트링으로 리턴
		//SimpleDateFormat 클래스는 date형식의 객체를 포멧형식을 설정하여 출력할 수 있게 도움
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");	
								//년도,월,일,시간,분-초까지-->SimpleDataFormat타입의 sdfDate를 생성하면서 포멧형식을 설정
		Date now = new Date();	//Date()-->현재 날짜와 시간을 저장할 객체 생성
		String strDate = sdfDate.format(now);	//format()으로 포매팅 하면 현재 날짜 시간으로 지정한 포맷으로 데이터를 변환한다.
		return strDate;//strDate (String형으로 )리턴
	}
}