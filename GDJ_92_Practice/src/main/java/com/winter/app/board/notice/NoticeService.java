package com.winter.app.board.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winter.app.board.BoardService;
import com.winter.app.board.BoardVO;

@Service
public class NoticeService implements BoardService {
	
	private final NoticeDAO noticeDAO;
	
	public NoticeService(NoticeDAO noticeDAO) {
		// TODO Auto-generated constructor stub
		this.noticeDAO = noticeDAO;
	}

	@Override
	public List<BoardVO> list() throws Exception {
		// TODO Auto-generated method stub
		return noticeDAO.list();
	}

	@Override
	public BoardVO detail(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		return noticeDAO.detail(boardVO);
	}

	@Override
	public int insert(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		return noticeDAO.insert(boardVO);
	}

	@Override
	public int update(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		return noticeDAO.update(boardVO);
	}

	@Override
	public int delete(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		return noticeDAO.delete(boardVO);
	}
	
	

}
