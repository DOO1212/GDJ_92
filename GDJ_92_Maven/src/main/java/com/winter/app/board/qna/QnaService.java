package com.winter.app.board.qna;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winter.app.board.BoardService;
import com.winter.app.board.BoardVO;

@Service
public class QnaService implements BoardService {
	
	@Autowired
	private QnaDAO qnaDao;

	@Override
	public List<BoardVO> list() throws Exception {

		return qnaDao.list();
	}

	@Override
	public BoardVO detail(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
