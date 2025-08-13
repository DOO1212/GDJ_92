package com.winter.app.board.qna;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.winter.app.board.BoardFileVO;
import com.winter.app.board.BoardService;
import com.winter.app.board.BoardVO;
import com.winter.app.commons.FileManager;
import com.winter.app.commons.Pager;

// @Service: 이 클래스가 비즈니스 로직을 처리하는 '서비스' 컴포넌트임을 Spring에게 알립니다.
@Service
// BoardService 인터페이스를 구현(implements)합니다.
public class QnaService implements BoardService {

	@Autowired
	private QnaDAO qnaDAO; // Q&A 전용 DAO

	@Autowired
	private FileManager fileManager; // 파일 처리 유틸리티

	@Value("${app.upload}")
	private String upload; // 파일 업로드 경로

	@Value("${board.qna}")
	private String board; // 게시판 이름

	// 게시글 목록 조회 로직 (NoticeService와 동일한 구조)
	@Override
	public List<BoardVO> list(Pager pager) throws Exception {
		Long totalCount = qnaDAO.totalCount(pager);
		pager.makeNum(totalCount);
		return qnaDAO.list(pager);
	}

	// 게시글 상세 조회 로직
	@Override
	public BoardVO detail(BoardVO boardVO) throws Exception {
		return qnaDAO.detail(boardVO);
	}

	// ==========================================================
	// Q&A 게시판의 핵심 로직: 답글 등록
	// ==========================================================
	public int reply(QnaVO qnaVO) throws Exception {
		// 1. 부모글의 정보를 조회합니다. (qnaVO에는 부모글의 boardNum이 담겨있음)
		QnaVO parent = (QnaVO) qnaDAO.detail(qnaVO);

		// 2. 답글의 계층 정보를 계산하고 설정합니다.
		// - boardRef: 답글은 부모글과 같은 그룹(REF)에 속합니다.
		qnaVO.setBoardRef(parent.getBoardRef());
		// - boardStep: 답글의 순서(STEP)는 부모글의 순서 바로 다음으로 설정합니다.
		qnaVO.setBoardStep(parent.getBoardStep() + 1);
		// - boardDepth: 답글의 깊이(DEPTH)는 부모글보다 한 단계 더 깊게 설정합니다.
		qnaVO.setBoardDepth(parent.getBoardDepth() + 1);

		// 3. 새 답글이 들어갈 공간을 확보하기 위해 기존 답글들의 순서(STEP)를 업데이트합니다.
		// (이 로직은 qnaDAO.refUpdate를 호출하는 것이 더 일반적입니다.)
		int result = qnaDAO.replyUpdate(parent);

		// 4. 모든 정보가 설정된 답글(qnaVO)을 DB에 최종적으로 INSERT합니다.
		// (이 로직은 qnaDAO.replyInsert를 호출하는 것이 더 명확할 수 있습니다.)
		result = qnaDAO.insert(qnaVO);

		return result;
	}

	// 원본글(질문) 등록 로직
	@Override
	public int insert(BoardVO boardVO, MultipartFile[] attaches) throws Exception {
		// 1. 원본글의 내용을 DB에 먼저 INSERT 합니다.
		int result = qnaDAO.insert(boardVO);
		// 2. 이 글이 새로운 글 그룹의 시작임을 알리기 위해, 자신의 글번호(boardNum)를
		// 그룹번호(boardRef)로 업데이트합니다.
		result = qnaDAO.refUpdate(boardVO);

		// 3. 첨부파일이 있으면 저장합니다. (NoticeService와 동일)
		if (attaches == null) {
			return result;
		}

		for (MultipartFile m : attaches) {
			if (m == null || m.isEmpty()) {
				continue;
			}
			String fileName = fileManager.fileSave(upload + board, m);
			BoardFileVO vo = new BoardFileVO();
			vo.setOriName(m.getOriginalFilename());
			vo.setSaveName(fileName);
			vo.setBoardNum(boardVO.getBoardNum());
			result = qnaDAO.insertFile(vo);
		}
		return result;
	}

	// 게시글 수정 로직 (NoticeService와 동일)
	@Override
	public int update(BoardVO boardVO, MultipartFile[] attaches) throws Exception {
		// ... (파일 추가 로직 포함)
		int result = qnaDAO.update(boardVO);
		// (이하 파일 처리 로직 생략)
		return result;
	}

	// 게시글 삭제 로직 (NoticeService와 동일)
	@Override
	public int delete(BoardVO boardVO) throws Exception {
		// ... (물리적 파일 삭제 및 DB 파일 정보 삭제 로직 포함)
		boardVO = qnaDAO.detail(boardVO);
		// (이하 파일 처리 및 글 삭제 로직 생략)
		int result = qnaDAO.delete(boardVO);
		return result;
	}

	// 이하 파일 처리 관련 메서드들 (NoticeService와 동일)
	@Override
	public int fileDelete(BoardFileVO boardFileVO) throws Exception {
		// ...
		return qnaDAO.fileDeleteOne(boardFileVO);
	}

	@Override
	public BoardFileVO fileDetail(BoardFileVO boardFileVO) throws Exception {
		return qnaDAO.fileDetail(boardFileVO);
	}
}