package com.winter.app.commons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pager {

	// --- DB 쿼리(SQL)에서 직접 사용하는 변수들 ---

	// MySQL의 LIMIT 구문에서 사용할 시작 행(row)의 인덱스 번호입니다.
	// makePage() 메서드에 의해 계산됩니다.
	private Long startIndex;

	// 한 페이지에 보여줄 데이터(게시글)의 개수입니다.
	// getPerPage() 메서드에서 기본값을 10으로 설정합니다.
	private Long perPage;

	// --- 클라이언트(브라우저)로부터 전달받는 변수들 ---

	// 사용자가 요청한 현재 페이지 번호입니다.
	// getPageNum() 메서드에서 기본값을 1로 설정하고 유효성을 검증합니다.
	private Long pageNum;

	// 검색할 컬럼의 종류를 나타냅니다. (예: "title", "contents", "writer")
	private String kind;

	// 사용자가 입력한 검색어입니다.
	private String keyword;

	// --- 계산을 통해 만들어져서 View(JSP)로 전달되는 변수들 ---

	// 전체 페이지의 총 개수입니다.
	// makeNum() 메서드에서 전체 게시글 수를 기반으로 계산됩니다.
	private Long totalPage;

	// JSP 화면에 표시할 페이지네이션의 시작 번호입니다.
	// 예: 현재 페이지가 7일 때, 블럭당 5개씩 보여준다면 시작 번호는 6이 됩니다.
	private Long startNum;

	// JSP 화면에 표시할 페이지네이션의 끝 번호입니다.
	// 예: 현재 페이지가 7일 때, 블럭당 5개씩 보여준다면 끝 번호는 10이 됩니다.
	private Long endNum;

	// 내부적으로 startIndex 값을 계산하는 메서드입니다.
	private void makePage() {
		// SQL의 LIMIT 구문에 사용할 값을 계산합니다.
		// 공식 : 시작 인덱스 = (요청 페이지 번호 - 1) * 페이지당 보여줄 개수
		this.startIndex = (this.getPageNum() - 1) * this.getPerPage();
	}

	// Service 계층에서 전체 데이터 개수(totalCount)를 받아 페이징에 필요한 모든 값을 계산하는 핵심 메서드입니다.
	public void makeNum(Long totalCount) {

		// 1. totalPage (총 페이지 수) 계산
		// 전체 데이터 수를 페이지당 보여줄 개수로 나눈 몫을 구합니다.
		this.totalPage = totalCount / this.getPerPage();
		// 만약 나머지가 0이 아니라면, 남은 데이터들을 보여주기 위해 페이지 수를 1 증가시킵니다.
		if (totalCount % this.getPerPage() != 0) {
			this.totalPage++;
		}

		// 2. totalBlock (총 페이지 블럭 수) 계산
		// 한 블럭 당 보여줄 페이지 번호의 개수를 5개로 고정합니다. (예: [1][2][3][4][5])
		Long perBlock = 5L;
		// 총 페이지 수를 블럭당 개수로 나누어 총 블럭 수를 계산합니다.
		Long totalBlock = this.totalPage / perBlock;
		// 만약 나머지가 있다면, 남은 페이지들을 보여주기 위해 블럭 수를 1 증가시킵니다.
		if (this.totalPage % perBlock != 0) {
			totalBlock++;
		}

		// 3. curBlock (현재 페이지가 속한 블럭 번호) 계산
		// 현재 페이지 번호를 블럭당 개수로 나누어 현재 블럭 번호를 구합니다.
		Long curBlock = this.getPageNum() / perBlock;
		// 만약 나머지가 있다면, 다음 블럭에 속하므로 블럭 번호를 1 증가시킵니다.
		if (this.getPageNum() % perBlock != 0) {
			curBlock++;
		}

		// 4. startNum, endNum (현재 블럭의 시작/끝 페이지 번호) 계산
		// 현재 블럭 번호를 이용해 화면에 표시할 시작 번호와 끝 번호를 계산합니다.
		this.startNum = (curBlock - 1) * perBlock + 1;
		this.endNum = curBlock * perBlock;

		// 5. 마지막 블럭일 경우의 예외 처리
		// 계산된 끝 번호가 실제 총 페이지 수보다 클 수 없습니다.
		// 따라서, 현재 블럭이 마지막 블럭이라면 끝 번호를 총 페이지 수로 맞춰줍니다.
		if (curBlock == totalBlock) {
			this.endNum = this.totalPage;
		}

		// 페이지네이션 번호 계산이 모두 끝난 후, DB 조회를 위한 startIndex 값을 계산합니다.
		this.makePage();
	}

	// --- Getter 재정의(Override) : 안정적인 값 반환을 위함 ---

	// 검색어가 null일 경우, MyBatis에서 에러가 발생할 수 있으므로 빈 문자열("")을 반환하도록 합니다.
	public String getKeyword() {
		if (this.keyword == null) {
			this.keyword = "";
		}
		return this.keyword;
	}

	// 페이지당 보여줄 개수가 지정되지 않았을 경우, 기본값 10을 사용하도록 합니다.
	public Long getPerPage() {
		if (this.perPage == null || this.perPage < 1) {
			this.perPage = 10L;
		}
		return this.perPage;
	}

	// 요청된 페이지 번호가 없거나 1보다 작을 경우, 무조건 1페이지를 보여주도록 합니다.
	public Long getPageNum() {
		if (this.pageNum == null || this.pageNum < 1) {
			this.pageNum = 1L;
		}

		// 만약 계산된 총 페이지 수보다 큰 페이지를 요청하면, 마지막 페이지를 보여주도록 합니다.
		// 이 로직은 totalPage가 계산된 이후(makeNum() 호출 후)에 의미가 있습니다.
		if (this.totalPage != null && this.pageNum > this.totalPage) {
			this.pageNum = this.totalPage;
		}

		return this.pageNum;
	}
}