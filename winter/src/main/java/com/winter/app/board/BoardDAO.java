package com.winter.app.board;

import java.util.List;

import com.winter.app.commons.BoardFileVO;

public interface BoardDAO {
	
	// list: 페이징 처리된 게시글 목록을 가져오는 기능의 규칙
	public List<BoardVO> list(Pager pager) throws Exception;
	
	// totalCount: 전체 게시글의 개수를 가져오는 기능의 규칙
	public Long totalCount(Pager pager) throws Exception;
	
	// detail: 특정 게시글 하나의 정보를 가져오는 기능의 규칙
	public BoardVO detail(BoardVO boardVO) throws Exception;
	
	// insert: 게시글(BoardVO) 정보를 DB에 추가하는 기능의 규칙
	public int insert(BoardVO boardVO) throws Exception;
	
	// insertFile: 첨부파일(BoardFileVO) 정보를 DB에 추가하는 기능의 규칙
	public int insertFile(BoardFileVO boardFileVO) throws Exception;
	
	// update: 게시글 정보를 수정하는 기능의 규칙
	public int update(BoardVO boardVO) throws Exception;
	
	// delete: 게시글 정보를 삭제하는 기능의 규칙
	public int delete(BoardVO boardVO) throws Exception;
}