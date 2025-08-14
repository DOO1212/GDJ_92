package com.winter.app.commons;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시판의 페이징 처리와 검색에 필요한 데이터와 연산을 담당하는 클래스입니다.
 * 이 객체 하나로 페이징과 관련된 모든 정보를 관리하여 Controller, Service, DAO 간의 데이터 전달을 용이하게 합니다.
 * @author winter
 */
@Getter
@Setter
public class Pager {
	
	//------------- DB에서 데이터를 가져올 때 사용하는 값들 -------------
	
	/**
	 * DB의 LIMIT 구문에서 사용할 시작 인덱스 번호입니다.
	 * (예: 1페이지는 0, 2페이지는 10부터 시작)
	 * makePage() 메서드에 의해 계산됩니다.
	 */
	private Long startIndex;
	
	/**
	 * 한 페이지에 보여줄 데이터(row)의 개수입니다.
	 * getPerPage()에서 기본값(10)이 설정됩니다.
	 */
	private Long perPage;
	
	//------------- 클라이언트(브라우저)에서 전달받는 값들 -------------

	/**
	 * 사용자가 현재 보고 있는 페이지 번호입니다.
	 * (예: 1, 2, 3...)
	 * getPageNum()에서 기본값(1)이 설정됩니다.
	 */
	private Long pageNum;
	
	/**
	 * 검색 종류를 나타냅니다. (예: "title", "contents", "writer")
	 * 이 값에 따라 Mapper(XML)에서 다른 검색 쿼리를 수행합니다.
	 */
	private String kind;
	
	/**
	 * 사용자가 입력한 검색어입니다.
	 */
	private String keyword;
	
	//------------- DB 조회 후 계산해서 View(JSP)로 보내는 값들 -------------
	
	/**
	 * 전체 페이지의 총개수입니다.
	 * makeNum() 메서드에서 계산됩니다.
	 */
	private Long totalPage;
	
	/**
	 * 현재 페이지 블럭의 시작 번호입니다.
	 * (예: 현재 3페이지고, 한 블럭에 5개씩 보여준다면 시작 번호는 1)
	 * makeNum() 메서드에서 계산됩니다.
	 */
	private Long startNum;
	
	/**
	 * 현재 페이지 블럭의 끝 번호입니다.
	 * (예: 현재 3페이지고, 한 블럭에 5개씩 보여준다면 끝 번호는 5)
	 * makeNum() 메서드에서 계산됩니다.
	 */
	private Long endNum;

	
	/**
	 * pageNum(현재 페이지 번호)과 perPage(페이지당 데이터 수)를 이용해
	 * DB에서 데이터를 조회할 때 필요한 startIndex 값을 계산합니다.
	 * 이 메서드는 makeNum() 내부에서 마지막에 호출됩니다.
	 */
	private void makePage() {
		// SQL의 LIMIT 구문에 사용할 값을 계산합니다.
		// 공식: 시작 인덱스 = (현재 페이지 번호 - 1) * 페이지당 데이터 수
		this.startIndex = (this.getPageNum() - 1) * this.getPerPage();
	}
	
	/**
	 * 전체 데이터 개수(totalCount)를 받아서 페이징에 필요한 모든 값들을 계산하고 멤버 변수에 세팅합니다.
	 * 이 메서드는 Service 계층에서 호출됩니다.
	 * @param totalCount DB에 있는 전체 데이터의 총개수
	 */
	public void makeNum(Long totalCount) {
		
		// 1. totalPage (전체 페이지 수) 계산
		// 전체 데이터 수를 페이지당 보여줄 데이터 수로 나눈 후, 나머지가 있으면 1페이지를 더합니다.
		this.totalPage = totalCount / this.getPerPage();
		if(totalCount % this.getPerPage() != 0) {
			this.totalPage++;
		}
		
		// 2. totalBlock (전체 페이지 블럭 수) 계산
		Long perBlock = 5L; // 한 번에 보여줄 페이지 번호의 개수 (예: [1][2][3][4][5])
		Long totalBlock = this.totalPage / perBlock;
		if(this.totalPage % perBlock != 0) {
			totalBlock++;
		}
		
		// 3. curBlock (현재 페이지 블럭 번호) 계산
		// 현재 페이지 번호를 블럭당 페이지 수로 나누어 현재 몇 번째 블럭에 있는지 계산합니다.
		Long curBlock = this.getPageNum() / perBlock;
		if(this.getPageNum() % perBlock != 0) {
			curBlock++;
		}
		
		// 4. startNum, endNum (현재 블럭의 시작/끝 페이지 번호) 계산
		this.startNum = (curBlock - 1) * perBlock + 1;
		this.endNum = curBlock * perBlock;
		
		// 5. 마지막 블럭 처리
		// 현재 블럭이 마지막 블럭이라면, endNum을 전체 페이지 수(totalPage)로 맞춥니다.
		// (예: 전체 12페이지일 때, 마지막 블럭은 [11][12]가 되어야 하므로 endNum은 15가 아닌 12가 됩니다.)
		if(curBlock == totalBlock) {
			this.endNum = this.totalPage;
		}
		
		// --- 페이징 계산이 끝난 후, DB 조회를 위한 값 계산 ---
		this.makePage();
	}
	
	//------------- Getter 재정의(Override) -------------
	// Lombok의 @Getter가 있더라도, 특정 로직을 추가하고 싶을 때 직접 만들 수 있습니다.

	/**
	 * 검색어가 null일 경우, 쿼리 에러를 방지하기 위해 빈 문자열("")을 반환합니다.
	 * @return 검색어 문자열 (null이 아님을 보장)
	 */
	public String getKeyword() {
		if(this.keyword == null) {
			this.keyword = "";
		}
		return this.keyword;
	}

	/**
	 * 페이지당 보여줄 데이터 개수(perPage)가 설정되지 않았을 경우, 기본값 10을 반환합니다.
	 * @return 페이지당 데이터 수 (null이 아님을 보장)
	 */
	public Long getPerPage() {
		if(this.perPage == null || this.perPage < 1) {
			this.perPage = 10L;
		}
		return perPage;
	}

	/**
	 * 요청된 페이지 번호(pageNum)가 없거나 1보다 작을 경우, 기본값 1을 반환합니다.
	 * 만약 계산된 전체 페이지 수보다 큰 페이지를 요청하면, 마지막 페이지 번호를 반환합니다.
	 * @return 현재 페이지 번호 (유효한 범위 내의 값임을 보장)
	 */
	public Long getPageNum() {
		if(this.pageNum == null || this.pageNum < 1) {
			this.pageNum = 1L;
		}
		
		// totalPage는 makeNum() 실행 전에는 0 또는 null일 수 있으므로,
		// totalPage가 계산된 후에만 이 로직이 의미를 가집니다.
		if(this.totalPage != null && this.pageNum > this.totalPage) {
			this.pageNum = this.totalPage;
		}
		
		return pageNum;
	}
}