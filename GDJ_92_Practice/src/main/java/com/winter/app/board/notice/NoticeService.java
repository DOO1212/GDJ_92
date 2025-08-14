package com.winter.app.board.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.winter.app.board.BoardDAO;
import com.winter.app.board.BoardFileVO;
import com.winter.app.board.BoardService;
import com.winter.app.board.BoardVO;
import com.winter.app.commons.FileManager;
import com.winter.app.commons.Pager;

// @Service : 이 클래스가 비즈니스 로직을 처리하는 서비스 계층의 컴포넌트임을 Spring에 알립니다.
@Service
public class NoticeService implements BoardService {
	
	// @Autowired : 의존성 주입(DI). Spring이 NoticeDAO 타입의 객체를 자동으로 연결해줍니다.
	@Autowired
	private NoticeDAO noticeDAO;
	
	// @Autowired : 의존성 주입(DI). Spring이 FileManager 타입의 객체를 자동으로 연결해줍니다.
	@Autowired
	private FileManager fileManager;
	
	// @Value("${app.upload}") : application.properties 파일의 'app.upload' 속성 값을 변수 'upload'에 주입합니다. (예: D:/upload/)
	@Value("${app.upload}")
	private String upload;
	
	// @Value("${board.notice}") : application.properties 파일의 'board.notice' 속성 값을 변수 'board'에 주입합니다. (예: notice/)
	@Value("${board.notice}")
	private String board;

	// 페이징 처리된 게시글 목록을 가져오는 메서드입니다.
	@Override
	public List<BoardVO> list(Pager pager) throws Exception {
		// DAO를 호출하여 전체 게시글 개수를 가져옵니다.
		Long totalCount = noticeDAO.totalCount(pager);
		// Pager 객체에 전체 개수를 전달하여 페이지네이션 관련 계산을 수행시킵니다.
		pager.makeNum(totalCount);
		// 계산이 완료된 Pager 정보를 이용해 현재 페이지에 맞는 게시글 목록을 DAO로부터 가져와 반환합니다.
		return noticeDAO.list(pager);
	}
	
	// 특정 게시글의 상세 정보를 가져오는 메서드입니다.
	@Override
	public BoardVO detail(BoardVO boardVO) throws Exception {
		// DAO에 게시글 번호를 전달하여 해당 게시글의 상세 정보를 조회하고 반환합니다.
		return noticeDAO.detail(boardVO);
	}
	
	// 새 게시글과 첨부파일을 DB와 서버에 저장하는 메서드입니다.
	@Override
	public int insert(BoardVO boardVO, MultipartFile [] attaches) throws Exception {
		// 먼저 게시글 내용(제목, 작성자 등)을 DB에 저장합니다. boardNum이 생성됩니다.
		int result = noticeDAO.insert(boardVO);
		
		// 첨부파일이 없으면 여기서 메서드를 종료합니다.
		if(attaches == null) {
			return result;
		}
	
		// 첨부파일 배열을 반복 처리합니다.
		for(MultipartFile m : attaches) {
			// 파일이 비어있으면 건너뜁니다.
			if(m == null || m.isEmpty()) {
				continue;
			}
			// 1. FileManager를 이용해 파일을 서버의 하드디스크에 저장합니다. (예: D:/upload/notice/ 폴더)
			String fileName = fileManager.fileSave(upload + board, m);
			
			// 2. 저장된 파일의 정보를 BoardFileVO 객체에 담아 DB에 저장합니다.
			BoardFileVO vo = new BoardFileVO();
			// 파일의 원본 이름을 저장합니다.
			vo.setOriName(m.getOriginalFilename());
			// 서버에 저장된 고유한 파일 이름을 저장합니다.
			vo.setSaveName(fileName);
			// 이 파일이 어떤 게시글에 속해있는지 알려주기 위해 게시글 번호를 저장합니다.
			vo.setBoardNum(boardVO.getBoardNum());
			result = noticeDAO.insertFile(vo);
		}
		// 최종 결과를 반환합니다.
		return result;
	}
	
	// 기존 게시글 내용과 첨부파일을 수정하는 메서드입니다.
	@Override
	public int update(BoardVO boardVO, MultipartFile [] attaches) throws Exception {
		// 먼저 수정된 게시글 내용을 DB에 업데이트합니다.
		int result = noticeDAO.update(boardVO);
		
		// 새로 추가된 첨부파일이 없으면 여기서 메서드를 종료합니다.
		if(attaches == null) {
			return result;
		}
		
		// 새로 추가된 파일들을 저장하는 로직은 insert와 동일합니다.
		for(MultipartFile m : attaches) {
			if(m == null || m.isEmpty()) {
				continue;
			}
			// 1. 파일을 HDD에 저장합니다.
			String fileName = fileManager.fileSave(upload + board, m);
			
			// 2. 저장된 파일 정보를 DB에 저장합니다.
			BoardFileVO vo = new BoardFileVO();
			vo.setOriName(m.getOriginalFilename());
			vo.setSaveName(fileName);
			vo.setBoardNum(boardVO.getBoardNum());
			result = noticeDAO.insertFile(vo);
		}
				
		return result;
	}
	
	// 게시글과 관련된 모든 파일 및 DB 정보를 삭제하는 메서드입니다.
	@Override
	public int delete(BoardVO boardVO) throws Exception {
		// 삭제할 파일들의 정보를 얻기 위해 게시글 상세 정보를 먼저 조회합니다.
		boardVO = noticeDAO.detail(boardVO);
		
		// 게시글에 첨부된 파일 목록을 반복 처리합니다.
		for(BoardFileVO vo : boardVO.getBoardFileVOs()) {
			// FileManager를 이용해 서버에 저장된 실제 파일을 삭제합니다.
			fileManager.fileDelete(upload + board, vo.getSaveName());
		}
		// DB에서 이 게시물에 첨부된 모든 파일 정보를 삭제합니다. (ON DELETE CASCADE로 대체 가능)
		// int result = noticeDAO.fileDelete(boardVO);
		// 최종적으로 게시글 자체를 DB에서 삭제하고 결과를 반환합니다.
		return noticeDAO.delete(boardVO);
	}
	
	// 첨부파일 한 개를 삭제하는 메서드입니다. (수정 페이지에서 파일 개별 삭제 시 사용)
	@Override
	public int fileDelete(BoardFileVO boardFileVO) throws Exception {
		// 1. DB에서 파일 번호(fileNum)로 파일의 상세 정보(saveName)를 조회합니다.
		boardFileVO = noticeDAO.fileDetail(boardFileVO);
		// 2. FileManager를 이용해 서버에 저장된 실제 파일을 삭제합니다.
		boolean result = fileManager.fileDelete(upload + board, boardFileVO.getSaveName());
		
		// 3. 파일이 성공적으로 삭제되었다면, DB에서도 해당 파일 정보를 삭제하고 결과를 반환합니다.
		return noticeDAO.fileDeleteOne(boardFileVO);
	}
	
	// 첨부파일 한 개의 상세 정보를 조회하는 메서드입니다. (파일 다운로드 시 사용)
	@Override
	public BoardFileVO fileDetail(BoardFileVO boardFileVO) throws Exception {
		// DAO에 파일 번호를 전달하여 상세 정보를 조회하고 반환합니다.
		return noticeDAO.fileDetail(boardFileVO);
	}

	// Summernote와 같은 에디터에서 이미지를 업로드 처리하는 메서드입니다.
	@Override
	public String boardFile(MultipartFile multipartFile) throws Exception {
		// 업로드된 파일이 없으면 null을 반환합니다.
		if(multipartFile == null || multipartFile.getSize() == 0) {
			return null;
		}
		// FileManager를 이용해 파일을 서버에 저장하고, 저장된 파일명을 받아옵니다.
		String filename = fileManager.fileSave(upload + board, multipartFile);
		
		// 에디터가 이미지를 표시할 수 있도록 웹 접근 경로(URL)를 만들어 반환합니다.
		return "/files/" + board + "/" + filename;
	}

	// Summernote 에디터에서 이미지를 삭제 처리하는 메서드입니다.
	@Override
	public boolean boardFileDelete(String fileName) throws Exception {
		// 전달받은 전체 URL 경로(예: /files/notice/image.jpg)에서 마지막 파일 이름 부분만 추출합니다.
		fileName = fileName.substring(fileName.lastIndexOf("/"));
		
		// FileManager를 이용해 서버에서 해당 파일을 삭제하고 성공 여부를 반환합니다.
		return fileManager.fileDelete(upload + board, fileName);
	}
}