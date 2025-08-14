package com.winter.app.board.qna;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.winter.app.board.BoardFileVO;
import com.winter.app.board.BoardService;
import com.winter.app.board.BoardVO;
import com.winter.app.board.notice.NoticeService;
import com.winter.app.commons.FileManager;
import com.winter.app.commons.Pager;

// @Service: 이 클래스가 비즈니스 로직을 처리하는 '서비스' 컴포넌트임을 Spring에게 알립니다.
@Service
public class QnaService implements BoardService {

	// final 키워드를 사용하여 noticeService가 변경 불가능하도록 설정합니다. (생성자를 통해서만 주입)
	private final NoticeService noticeService;

	// @Autowired: Spring 컨테이너에 등록된 QnaDAO 타입의 빈(Bean)을 자동으로 주입(DI)합니다.
	@Autowired
	private QnaDAO qnaDAO;

	// @Autowired: FileManager 타입의 빈을 자동으로 주입합니다.
	@Autowired
	private FileManager fileManager;

	// @Value: application.properties 파일의 'app.upload' 값을 찾아 변수에 주입합니다.
	@Value("${app.upload}")
	private String upload; // 파일 업로드 기본 경로

	// @Value: application.properties 파일의 'board.qna' 값을 찾아 변수에 주입합니다.
	@Value("${board.qna}")
	private String board; // 현재 게시판 이름 (파일 경로 생성에 사용)

	// 생성자 주입(Constructor Injection) 방식입니다.
	// QnaService 객체가 생성될 때 반드시 NoticeService 타입의 객체를 주입받아야 합니다.
	QnaService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	// 게시글 목록을 조회하는 메서드입니다.
	@Override
	public List<BoardVO> list(Pager pager) throws Exception {
		// 1. 전체 게시글 수를 조회하여 Pager 객체에 필요한 값(totalPage 등)을 계산합니다.
		Long totalCount = qnaDAO.totalCount(pager);
		pager.makeNum(totalCount);

		// 2. 계산된 Pager 정보를 바탕으로 현재 페이지에 맞는 게시글 목록을 DB에서 가져옵니다.
		// (정렬은 boardRef 내림차순, boardStep 오름차순으로 하여 계층 구조를 유지합니다.)
		return qnaDAO.list(pager);
	}

	// 게시글 상세 정보를 조회하는 메서드입니다.
	@Override
	public BoardVO detail(BoardVO boardVO) throws Exception {
		// 3. 주어진 boardNum으로 게시글의 상세 정보를 DB에서 조회하여 반환합니다.
		return qnaDAO.detail(boardVO);
	}

	// 답글을 등록하는 메서드입니다. (Q&A 게시판의 핵심 로직)
	public int reply(QnaVO qnaVO) throws Exception {
		// 1. 답글의 기준이 되는 부모글 정보를 DB에서 조회합니다. (qnaVO에는 부모글의 boardNum이 담겨있음)
		QnaVO parent = (QnaVO) qnaDAO.detail(qnaVO);
		// 2. 답글의 계층 정보를 부모글 기준으로 설정합니다.
		qnaVO.setBoardRef(parent.getBoardRef()); // 그룹 번호(Ref)는 부모와 동일하게 설정합니다.
		qnaVO.setBoardStep(parent.getBoardStep() + 1); // 순서(Step)는 부모보다 1 증가시킵니다.
		qnaVO.setBoardDepth(parent.getBoardDepth() + 1); // 깊이(Depth)는 부모보다 1 증가시킵니다.

		// 3. 새 답글이 들어갈 자리를 만들기 위해, 기존 답글들의 Step 값을 1씩 업데이트(증가)합니다.
		int result = qnaDAO.replyUpdate(parent);

		// 4. 계층 정보 설정이 완료된 답글을 DB에 최종 INSERT 합니다.
		result = qnaDAO.insert(qnaVO);

		return result;
	}

	// 원본글(질문)을 등록하는 메서드입니다.
	@Override
	public int insert(BoardVO boardVO, MultipartFile[] attaches) throws Exception {
		// 1. 게시글 정보를 DB의 BOARD 테이블에 INSERT 합니다.
		int result = qnaDAO.insert(boardVO);
		// 2. 글이 생성된 직후, 자신의 글 번호(boardNum)를 그룹 번호(boardRef)로 업데이트하여 새로운 그룹을 생성합니다.
		result = qnaDAO.refUpdate(boardVO);

		// 3. 첨부파일이 없으면 여기서 로직을 종료합니다.
		if (attaches == null) {
			return result;
		}

		// 4. 첨부된 파일들을 하나씩 처리합니다.
		for (MultipartFile m : attaches) {
			// 5. 파일이 없거나 비어있으면 건너뜁니다.
			if (m == null || m.isEmpty()) {
				continue;
			}
			// 6. FileManager를 통해 파일을 서버 HDD에 저장하고, 저장된 파일명을 반환받습니다.
			String fileName = fileManager.fileSave(upload + board, m);

			// 7. 파일 정보를 DB의 FILE 테이블에 저장하기 위해 BoardFileVO 객체를 생성하고 값을 설정합니다.
			BoardFileVO vo = new BoardFileVO();
			vo.setOriName(m.getOriginalFilename()); // 원본 파일명
			vo.setSaveName(fileName); // 서버에 저장된 파일명
			vo.setBoardNum(boardVO.getBoardNum()); // 부모 게시글 번호
			result = qnaDAO.insertFile(vo);
		}
		return result;
	}

	// 게시글 정보를 수정하는 메서드입니다.
	@Override
	public int update(BoardVO boardVO, MultipartFile[] attaches) throws Exception {
		// 1. 게시글의 제목, 내용을 DB에서 UPDATE 합니다.
		int result = qnaDAO.update(boardVO);

		// 2. 수정 시 새로 추가된 파일이 없다면 여기서 로직을 종료합니다.
		if (attaches == null) {
			return result;
		}

		// 3. (insert 로직과 동일) 새로 추가된 파일들을 서버에 저장하고 DB에도 정보를 저장합니다.
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

	// 게시글을 삭제하는 메서드입니다.
	@Override
	public int delete(BoardVO boardVO) throws Exception {
		// 1. 삭제할 게시글의 전체 정보(첨부파일 목록 포함)를 DB에서 조회합니다.
		boardVO = qnaDAO.detail(boardVO);

		// 2. HDD의 물리적 파일 삭제: 조회된 첨부파일 목록을 순회하며 서버에 저장된 실제 파일을 삭제합니다.
		for (BoardFileVO vo : boardVO.getBoardFileVOs()) {
			fileManager.fileDelete(upload + board, vo.getSaveName());
		}

		// 3. DB의 파일 정보 삭제: 해당 게시글에 연결된 모든 파일 정보를 FILE 테이블에서 삭제합니다.
		int result = qnaDAO.fileDelete(boardVO);

		// 4. DB의 게시글 삭제: 파일 정보가 모두 정리된 후, BOARD 테이블에서 해당 게시글을 최종적으로 삭제합니다.
		result = qnaDAO.delete(boardVO);

		return result;
	}

	// 첨부파일 하나만 개별적으로 삭제하는 메서드입니다. (수정 페이지에서 사용)
	@Override
	public int fileDelete(BoardFileVO boardFileVO) throws Exception {
		// 1. 삭제할 파일의 전체 정보(저장된 이름 등)를 DB에서 조회합니다.
		boardFileVO = qnaDAO.fileDetail(boardFileVO);
		// 2. FileManager를 이용해 서버 HDD에 저장된 물리적 파일을 삭제합니다.
		boolean result = fileManager.fileDelete(upload + board, boardFileVO.getSaveName());

		// 3. 물리적 파일 삭제 성공 여부와 관계없이 DB의 FILE 테이블에서 해당 파일 정보를 삭제합니다.
		return qnaDAO.fileDeleteOne(boardFileVO);
	}

	// 파일 번호(fileNum)로 파일의 상세 정보를 조회하는 메서드입니다.
	@Override
	public BoardFileVO fileDetail(BoardFileVO boardFileVO) throws Exception {
		// DAO를 통해 DB에서 파일 정보를 조회하여 반환합니다.
		return qnaDAO.fileDetail(boardFileVO);
	}

	// Summernote 등 웹 에디터에서 이미지 파일을 비동기(AJAX)로 업로드할 때 사용하는 메서드입니다.
	@Override
	public String boardFile(MultipartFile multipartFile) throws Exception {
		// 1. 전송된 파일이 비어있는지 확인합니다.
		if (multipartFile == null || multipartFile.isEmpty()) {
			return null;
		}
		// 2. FileManager를 통해 파일을 서버에 저장하고, 저장된 고유한 파일명을 반환받습니다.
		String fileName = fileManager.fileSave(upload + board, multipartFile);
		// 3. 에디터가 이미지를 표시할 수 있도록 접근 가능한 URL 경로를 만들어 반환합니다.
		return "/fileDown/" + board + "/" + fileName;
	}

	// Summernote 등 웹 에디터에서 업로드된 이미지를 삭제할 때 사용하는 메서드입니다.
	@Override
	public boolean boardFileDelete(String fileName) throws Exception {
		// 1. 파일명을 받아 FileManager를 통해 서버에 저장된 물리적 파일을 삭제합니다.
		// 2. 삭제 성공 여부를 boolean 타입으로 반환합니다.
		return fileManager.fileDelete(upload + board, fileName);
	}

}