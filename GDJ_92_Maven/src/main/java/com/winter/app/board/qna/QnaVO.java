package com.winter.app.board.qna;

import java.time.LocalDate;

import com.winter.app.board.BoardVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaVO extends BoardVO {
	
	private Long BoardRef;
	private Long BoardStep;
	private Long BoardDepth;

}
