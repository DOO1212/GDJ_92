package com.winter.app.board;

import java.time.LocalDateTime;
import java.util.List; // List를 사용하기 위해 java.util.List를 import 합니다.

import com.winter.app.commons.BoardFileVO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardVO {
	
	private Long boardNum;
	@NotBlank
	private String boardTitle;
	private String boardContents;
	private String boardWriter;
	private LocalDateTime boardDate;
	private Long boardHit;
	
	// 한 개의 게시글(BoardVO)이 여러 개의 첨부 파일(BoardFileVO)을 가질 수 있도록 List로 선언합니다.
	// 변수 이름은 'boardFileVOs'처럼 복수형으로 짓는 것이 일반적입니다.
	private List<BoardFileVO> boardFileVOs;
	
}