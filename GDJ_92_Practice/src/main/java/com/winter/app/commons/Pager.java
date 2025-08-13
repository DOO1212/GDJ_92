package com.winter.app.commons;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pager {

	// ----- DB에서 데이터를 가져올 때 필요한 값들 -----

	// DB의 limit 절에서 사용할 시작 인덱스 번호 (예: 0, 10, 20...)
	private Long startIndex;

	// DB의 limit 절에서 사용할 가져올 데이터의 개수 (MySQL/MariaDB 기준)
	private Long perPage;

	// ----- 사용자가 요청한 값 (파라미터) -----

	// 현재 페이지 번호 (예: 1, 2, 3...)
	private Long pageNum;

	// 검색 종류 (예: 'k1'은 제목, 'k2'는 내용)
	private String kind;

	// 검색어
	private String keyword;

	// ----- 계산을 통해 만들어지는 값들 (View에서 사용) -----

	// 전체 페이지의 총 개수
	private Long totalPage;

	// 페이지네이션 블럭의 시작 번호 (예: 1, 6, 11...)
	private Long startNum;
	// 페이지네이션 블럭의 끝 번호 (예: 5, 10, 15...)
	private Long endNum;

	// SQL의 LIMIT 값을 계산하는 내부 메서드
	private void makePage() {
		// 시작 인덱스 계산: (현재 페이지 번호 - 1) * 페이지당 보여줄 개수
		this.startIndex = (this.getPageNum() - 1) * this.getPerPage();
	}

	// 페이징에 필요한 모든 숫자를 계산하는 핵심 메서드
	public void makeNum(Long totalCount) {

		// 1. totalPage : 전체 페이지 개수 계산
		// 전체 데이터 개수(totalCount)를 페이지당 보여줄 개수(perPage)로 나눕니다.
		this.totalPage = totalCount / this.getPerPage();
		// 만약 나머지가 있다면(데이터가 하나라도 더 있다면), 페이지를 하나 더 추가합니다.
		if (totalCount % this.getPerPage() != 0) {
			this.totalPage++;
		}

		// 2. totalBlock : 전체 블럭의 개수 계산
		// 블럭당 보여줄 페이지 번호의 개수 (예: 5개씩 -> [1][2][3][4][5])
		Long perBlock = 5L;
		// 전체 페이지 개수를 블럭당 개수로 나누어 총 몇 개의 블럭이 필요한지 계산합니다.
		Long totalBlock = this.totalPage / perBlock;
		if (this.totalPage % perBlock != 0) {
			totalBlock++;
		}

		// 3. 현재 페이지 번호로 현재 블럭 번호를 계산
		Long curBlock = this.getPageNum() / perBlock;
		if (this.getPageNum() % perBlock != 0) {
			curBlock++;
		}

		// 4. 현재 블럭 번호로 시작 번호와 끝 번호를 계산
		this.startNum = (curBlock - 1) * perBlock + 1;
		this.endNum = curBlock * perBlock;

		// 5. 현재 블럭이 마지막 블럭일 경우, 끝 번호를 전체 페이지 번호로 맞춥니다.
		// (예: 총 페이지가 13일 때, 마지막 블럭은 [11][12][13]이 되어야 함)
		if (curBlock == totalBlock) {
			this.endNum = this.totalPage;
		}

		// 6. DB에서 사용할 limit의 시작 인덱스를 계산합니다.
		this.makePage();
	}

	// --- 안정성을 높여주는 커스텀 Getter 메서드들 ---

	// keyword 값이 null일 경우, 빈 문자열("")을 반환하여 NullPointerException을 방지합니다.
	public String getKeyword() {
		if (this.keyword == null) {
			this.keyword = "";
		}
		return this.keyword;
	}

	// perPage 값이 null일 경우, 기본값으로 10을 설정합니다.
	public Long getPerPage() {
		if (this.perPage == null || this.perPage < 1) {
			this.perPage = 10L;
		}
		return perPage;
	}

	// pageNum 값이 null이거나 1보다 작을 경우, 기본값으로 1을 설정합니다.
	public Long getPageNum() {
		if (this.pageNum == null || this.pageNum < 1) {
			this.pageNum = 1L;
		}
		return pageNum;
	}

}