package com.winter.app.board;

public interface BoardDAO {
	
	// ... 다른 메서드 생략 ...
	
	// 게시글 정보를 DB에 저장
	public int insert(BoardVO boardVO)throws Exception;
	
	// 파일 정보를 DB에 저장 (BoardFileVO 객체를 파라미터로 받음)
	public int insertFile(BoardFileVO boardFileVO) throws Exception;
	
	// ... 다른 메서드 생략 ...
}