package com.winter.app.board;

import java.util.List;

import com.winter.app.commons.Pager;

public interface BoardDAO {
	
	public List<BoardVO> list(Pager pager)throws Exception;
	
	public Long totalCount(Pager pager)throws Exception;
	
	//detail
	public BoardVO detail(BoardVO boardVO)throws Exception;
	
	//insert
	public int insert(BoardVO boardVO)throws Exception;
	
	public int insertFile(BoardFileVO boardFileVO) throws Exception;
	
	//update
	public int update(BoardVO boardVO)throws Exception;
	
	//delete
	public int delete(BoardVO boardVO)throws Exception;
	
	public int fileDelete(BoardVO boardVO) throws Exception;
	
	//
	public BoardFileVO fileDetail(BoardFileVO boardFileVO)throws Exception;
	
	
	public int fileDeleteOne(BoardFileVO boardFileVO) throws Exception;

}
