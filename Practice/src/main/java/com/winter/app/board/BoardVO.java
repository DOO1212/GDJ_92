package com.winter.app.board;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BoardVO {
	
	private Long boardNum;
	private String boardTitle;
	private String boardContents;
	private String boardWriter;
	private LocalDateTime boardDate;
	private Long boardHit;

}
