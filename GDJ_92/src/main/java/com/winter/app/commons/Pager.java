package com.winter.app.commons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pager {
	
	// limit의 시작 인덱스 번호
	private Long  startIndex;
	
	// Limit의 끝
	private Long endIndex;
	
	//페이지당 보여줄 row의 갯수
	private Long perPage;

	//page 번호
	private Long pageNum;
	
	//검색종류
	private String kind;
	
	//검색어
	private String keyword;
	
	private Long totalPage;
	
	private Long startNum;
	private Long endNum;
	
	private void makePage() {
		//SQL의 Limit값을 계산
		
		this.startIndex=(this.getPageNum()-1)*this.getPerPage();
		this.endIndex = perPage;
		
		
	}
	
	public void makeNum(Long totalCount) {
		
		//1. totalPage : 전체 페이지 갯수
		//totalPage = (int)Math.ceil((double)totalData / numPerPage);
		this.totalPage = totalCount/this.getPerPage();
		if(totalCount%perPage != 0) {
			this.totalPage++;
		}
		
		//2. totalBlock : 전체 블럭의 갯수
		Long perBlock=5L;//블럭당 출력할 번호의 갯수
		Long totalBlock = totalPage/perBlock;
		if(totalPage%perBlock != 0) {
			totalBlock++;
		}
		
		//3. 현재 페이지 번호로 현재 블럭 번호를 계산
		Long curBlock = this.getPageNum()/perBlock;
		if(this.pageNum%5 != 0) {
			curBlock++;
		}
		
		//4. 현재 블럭 번호로 시작번호와 끝번호 계산
		this.startNum = (curBlock-1)*perBlock+1;
		this.endNum = curBlock*perBlock;
		
		//5. 마지막 블럭일 경우
		if(curBlock==totalBlock) {
			this.endNum=totalPage;
		}
		
		
		//-----------------------
		this.makePage();
		
	}
	
	public String getKeyword() {
		if(this.keyword == null) {
			this.keyword="";
		}
		
		return this.keyword;
	}

	public Long getStartIndex() {
		return startIndex;
	}

	public Long getEndIndex() {
		return endIndex;
	}

	public Long getPerPage() {
		if(this.perPage==null) {
			this.perPage=10L;
		}
		return perPage;
	}

	public Long getPageNum() {
		if(this.pageNum == null || this.pageNum<1) {
			this.pageNum=1L;
		}
		
		if(this.pageNum>totalPage) {
			this.pageNum=totalPage;
		}
	
		return pageNum;
	}
	
	
	

}
