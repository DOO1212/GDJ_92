package com.winter.app.commons;

public class Pager {
	
	// limit의 시작 인덱스 번호
	private Long startIndex;
	
	// limit의 끝
	private Long endIndex;
	
	private Long perPage;
	
	private Long pageNum;
	
	public void makePage() {
		// SQL의 Limit값을 계산
		this.startIndex=(pageNum-1)*endIndex;
	}
	

}
