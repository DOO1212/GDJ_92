package com.winter.app.board;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardVO {
	
	private Long boardNum;
	private String boardTitle;
	private String boardWriter;
	private String boardContenets;
	private LocalDateTime boardDate;
	private Long boardHit;

}
