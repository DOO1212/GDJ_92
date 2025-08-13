package com.winter.app.board.notice;

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

// @Service: 이 클래스가 비즈니스 로직을 처리하는 '서비스' 계층의 컴포넌트임을 Spring에게 알립니다.
@Service
// BoardService 인터페이스를 구현(implements)합니다.
// 즉, BoardService에 정의된 모든 메서드를 반드시 여기서 완성해야 합니다.
public class NoticeService implements BoardService {
	
	@Autowired
	private NoticeDAO noticeDAO;
	
	@Autowired
	private FileManager fileManager;
	
	// @Value: application.properties의 app.upload 값을 주입합니다. (파일 저장 기본 경로)
	@Value("${app.upload}")
	private String upload;
	
	// @Value: application.properties의 board.notice 값을 주입합니다. (게시판 이름)
	@Value("${board.notice}")
	private String board;

	// 게시글 목록 조회 로직
	@Override
	public List<BoardVO> list(Pager pager) throws Exception {
		// 1. DAO를 호출해 전체 데이터 개수를 가져옵니다.
		Long totalCount= noticeDAO.totalCount(pager);
		// 2. Pager 객체에 전체 데이터 개수를 전달해 페이지 번호 등을 계산시킵니다.
		pager.makeNum(totalCount);
		// 3. 페이지 계산이 완료된 Pager를 이용해 해당 페이지의 목록만 DAO에서 가져옵니다.
		return noticeDAO.list(pager);
	}
	
	// 게시글 상세 조회 로직
	@Override
	public BoardVO detail(BoardVO boardVO) throws Exception {
		return noticeDAO.detail(boardVO);
	}
	
	// 게시글 등록 로직 (파일 포함)
	@Override
	public int insert(BoardVO boardVO, MultipartFile [] attaches) throws Exception {
		// 1. DAO를 호출하여 게시글의 텍스트 내용부터 DB에 저장합니다.
		//    이때 boardVO에 auto-increment된 boardNum 값이 담기게 됩니다.
		int result = noticeDAO.insert(boardVO);
		
		// 2. 첨부된 파일이 없으면 여기서 로직을 종료합니다.
		if(attaches == null) {
			return result;
		}
	
		// 3. 첨부파일들을 하나씩 처리합니다.
		for(MultipartFile m:attaches) {
			// 파일이 없거나 비어있으면 건너뜁니다.
			if(m == null || m.isEmpty()) {
				continue;
			}
			// 3-1. FileManager를 이용해 파일을 하드디스크에 저장합니다. (예: D:/upload/notice/)
			String fileName = fileManager.fileSave(upload+board, m);
			
			// 3-2. 저장된 파일 정보를 담을 BoardFileVO 객체를 생성하고,
			BoardFileVO vo = new BoardFileVO();
			vo.setOriName(m.getOriginalFilename()); // 원본 파일명
			vo.setSaveName(fileName);              // 저장된 파일명
			vo.setBoardNum(boardVO.getBoardNum()); // 어떤 게시글에 속하는지 (FK)
			// DAO를 호출해 파일 정보를 DB에 저장합니다.
			result = noticeDAO.insertFile(vo);
		}
		return result;
	}
	
	// 게시글 수정 로직 (파일 추가)
	@Override
	public int update(BoardVO boardVO, MultipartFile [] attaches) throws Exception {
		// 1. 게시글의 텍스트 내용을 먼저 수정합니다.
		int result = noticeDAO.update(boardVO);
		
		if(attaches == null) {
			return result;
		}
		
		// 2. 수정 시 새로 추가된 파일들을 저장합니다. (insert 로직과 동일)
		for(MultipartFile m:attaches) {
			if(m == null || m.isEmpty()) {
				continue;
			}
			String fileName = fileManager.fileSave(upload+board, m);
			BoardFileVO vo = new BoardFileVO();
			vo.setOriName(m.getOriginalFilename());
			vo.setSaveName(fileName);
			vo.setBoardNum(boardVO.getBoardNum());
			result = noticeDAO.insertFile(vo);
		}
				
		return result;
	}
	
	// 게시글 삭제 로직
	@Override
	public int delete(BoardVO boardVO) throws Exception {
		// 1. DB에서 게시글 정보를 조회하여, 이 글에 첨부된 파일 목록을 가져옵니다.
		boardVO = noticeDAO.detail(boardVO);
		
		// 2. 첨부된 파일들을 순회하며 하드디스크에서 물리적으로 삭제합니다.
		for(BoardFileVO vo : boardVO.getBoardFileVOs()) {
			fileManager.fileDelete(upload+board, vo.getSaveName());
		}
		// 3. DB에서 해당 게시글에 속한 파일 정보들을 모두 삭제합니다.
		int result = noticeDAO.fileDelete(boardVO);
		// 4. 마지막으로 DB에서 게시글 자체를 삭제합니다.
		return noticeDAO.delete(boardVO);
	}
	
	// 첨부파일 하나만 삭제하는 로직
	@Override
	public int fileDelete(BoardFileVO boardFileVO) throws Exception {
		// 1. DB에서 파일 정보를 조회하여 실제 저장된 파일명을 알아냅니다.
		boardFileVO = noticeDAO.fileDetail(boardFileVO);
		// 2. FileManager를 이용해 하드디스크에서 파일을 삭제합니다.
		boolean result =fileManager.fileDelete(upload+board, boardFileVO.getSaveName());
		// 3. DB에서 해당 파일의 정보 한 줄을 삭제합니다.
		return noticeDAO.fileDeleteOne(boardFileVO);
	}
	
	// 첨부파일 상세 정보 조회
	@Override
	public BoardFileVO fileDetail(BoardFileVO boardFileVO) throws Exception {
		return noticeDAO.fileDetail(boardFileVO);
	}
	
	// AJAX용 임시 파일 업로드 로직
	@Override
	public String boardFile(MultipartFile multipartFile) throws Exception {
		if(multipartFile ==null || multipartFile.getSize() ==0) {
			return null;
		}
		// 파일을 하드디스크에 저장하고,
		String filename=fileManager.fileSave(upload+board, multipartFile);
		// 브라우저에서 접근 가능한 URL 경로를 만들어 반환합니다.
		return "/files/"+board+"/"+filename;
	}
	
	// AJAX용 임시 파일 삭제 로직
	@Override
	public boolean boardFileDelete(String fileName) throws Exception {
		// 전체 URL 경로에서 파일명 부분만 추출합니다.
		fileName = fileName.substring(fileName.lastIndexOf("/")+1);
		// 해당 파일을 하드디스크에서 삭제하고 성공 여부를 반환합니다.
		return fileManager.fileDelete(upload+board, fileName);
	}
}