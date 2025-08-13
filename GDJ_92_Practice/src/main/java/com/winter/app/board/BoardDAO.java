package com.winter.app.board;

import java.util.List;
import com.winter.app.commons.Pager;

public interface BoardDAO {

	// 게시글 목록을 조회하는 기능의 명세입니다.
	// Pager 객체를 받아서 페이징 처리(예: 1페이지, 2페이지)와 검색을 수행합니다.
	// List<BoardVO>를 반환하여 여러 개의 게시글 정보를 전달합니다.
	public List<BoardVO> list(Pager pager) throws Exception;

	// 전체 게시글의 개수를 조회하는 기능의 명세입니다.
	// Pager 객체를 받아 검색 조건에 맞는 게시글의 총개수를 계산합니다.
	// Long 타입을 반환하여 매우 많은 수의 게시글도 처리할 수 있도록 합니다.
	public Long totalCount(Pager pager) throws Exception;

	// 특정 게시글 하나의 상세 정보를 조회하는 기능의 명세입니다.
	// 조회할 게시글의 번호(boardNum)가 담긴 BoardVO 객체를 받습니다.
	// 해당 게시글의 모든 정보가 담긴 BoardVO 객체 하나를 반환합니다.
	public BoardVO detail(BoardVO boardVO) throws Exception;

	// 새로운 게시글을 데이터베이스에 추가하는 기능의 명세입니다.
	// 제목, 내용, 작성자 등이 담긴 BoardVO 객체를 받습니다.
	// int를 반환하여 DB에 성공적으로 추가된 행(row)의 개수(보통 1)를 알려줍니다.
	public int insert(BoardVO boardVO) throws Exception;

	// 게시글에 첨부된 파일 정보를 데이터베이스에 추가하는 기능의 명세입니다.
	// 파일 정보(파일명, 원본명 등)가 담긴 BoardFileVO 객체를 받습니다.
	public int insertFile(BoardFileVO boardFileVO) throws Exception;

	// 기존 게시글의 내용을 수정하는 기능의 명세입니다.
	// 수정할 게시글의 번호와 새로운 내용이 담긴 BoardVO 객체를 받습니다.
	// int를 반환하여 성공적으로 수정된 행의 개수를 알려줍니다.
	public int update(BoardVO boardVO) throws Exception;

	// 특정 게시글을 삭제하는 기능의 명세입니다.
	// 삭제할 게시글의 번호가 담긴 BoardVO 객체를 받습니다.
	public int delete(BoardVO boardVO) throws Exception;

	// 특정 게시글에 첨부된 '모든' 파일 정보를 DB에서 삭제하는 기능의 명세입니다.
	// 게시글을 삭제하기 전에, 해당 글에 연결된 파일들을 먼저 정리할 때 사용됩니다.
	public int fileDelete(BoardVO boardVO) throws Exception;

	// 특정 첨부파일 하나의 상세 정보를 조회하는 기능의 명세입니다.
	// 조회할 파일의 번호(fileNum)가 담긴 BoardFileVO 객체를 받습니다.
	public BoardFileVO fileDetail(BoardFileVO boardFileVO) throws Exception;

	// 게시글에 첨부된 여러 파일 중 '특정 파일 하나만' 삭제하는 기능의 명세입니다.
	// 삭제할 파일의 번호가 담긴 BoardFileVO 객체를 받습니다.
	public int fileDeleteOne(BoardFileVO boardFileVO) throws Exception;

}