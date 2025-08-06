package com.winter.app.commons;

public class Pager {
	
	// limit의 시작 인덱스 번호
	private Long startIndex;
	
	// limit의 끝
	private Long endIndex;
	
	public Long getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Long startIndex) {
		this.startIndex = startIndex;
	}

	public Long getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Long endIndex) {
		this.endIndex = endIndex;
	}

	public Long getPerPage() {
		return perPage;
	}

	public void setPerPage(Long perPage) {
		this.perPage = perPage;
	}

	public Long getPageNum() {
		return pageNum;
	}

	public void setPageNum(Long pageNum) {
		this.pageNum = pageNum;
	}

	private Long perPage;
	
	private Long pageNum;
	
	public void makePage() {
		// SQL의 Limit값을 계산
		this.startIndex=(pageNum-1)*endIndex;
	}
	

}
