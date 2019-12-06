package com.information.common;

/*Manager class 와 Student class가 구현*/
public interface Common {	//인터페이스-->구현된 것이 없는 기본 설계도
	
	public void execution(String mode);//추상메서드
	public void update();	//추상메서드
	public void select();	//추상메서드
	public void delete();	//추상메서드
	public void insert();	//추상메서드
	
	
}
