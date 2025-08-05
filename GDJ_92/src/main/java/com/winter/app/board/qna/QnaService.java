package com.winter.app.board.qna;

import java.util.List;
import com.winter.app.board.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winter.app.board.BoardService;
import com.winter.app.board.BoardVO;

@Service
public class QnaService implements BoardService{

    private final NoticeService noticeService;

	@Autowired
	private QnaDAO qnaDAO;

    QnaService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

	@Override
	public List<BoardVO> list() throws Exception {
		// TODO Auto-generated method stub
		return qnaDAO.list();
	}

	@Override
	public BoardVO detail(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		return qnaDAO.detail(boardVO);
	}
	
	public int reply(QnaVO qnaVO)throws Exception{
		QnaVO parent= (QnaVO)qnaDAO.detail(qnaVO);
		qnaVO.setBoardRef(parent.getBoardRef());
		qnaVO.setBoardStep(parent.getBoardStep()+1);
		qnaVO.setBoardDepth(parent.getBoardDepth()+1);
		int result = qnaDAO.replyUpdate(parent);
		
		result = qnaDAO.insert(qnaVO);
		
		return result;
	}

	@Override
	public int insert(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		int result = qnaDAO.insert(boardVO);
		//ref값을 update
		result = qnaDAO.refUpdate(boardVO);
		return result;
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
