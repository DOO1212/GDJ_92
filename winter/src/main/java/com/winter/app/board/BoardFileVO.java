package com.winter.app.board;

import com.winter.app.commons.FileVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardFileVO extends FileVO { // FileVO의 모든 것을 물려받음

	// 이 파일이 어떤 게시글에 속하는지 식별하기 위한 번호 (FK)
	private Long boardNum;
}