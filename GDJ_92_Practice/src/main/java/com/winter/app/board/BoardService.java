package com.winter.app.board;

// 다른 패키지의 클래스들을 가져와서 사용합니다.
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.winter.app.commons.Pager;

// BoardService 라는 이름의 인터페이스를 선언합니다.
// 게시판과 관련된 비즈니스 로직(실제 서비스 기능)들의 명세를 정의합니다.
public interface BoardService {
	
	// 게시글 목록을 가져오는 기능의 명세입니다. (DAO와 유사)
	// Controller로부터 Pager 객체를 받아 DAO에 그대로 전달하는 역할을 합니다.
	public List<BoardVO> list(Pager pager)throws Exception;
	
	// 게시글 상세 정보를 가져오는 기능의 명세입니다. (DAO와 유사)
	public BoardVO detail(BoardVO boardVO)throws Exception;
	
	// 게시글과 '첨부파일들'을 함께 등록하는 기능의 명세입니다.
	// Controller로부터 게시글 정보(boardVO)와 여러 개의 파일(attaches)을 받습니다.
	// 이 메서드 내부에서 파일 저장, DB 저장 등 복잡한 과정이 처리됩니다.
	public int insert(BoardVO boardVO, MultipartFile [] attaches)throws Exception;
	
	// 게시글과 '첨부파일들'을 함께 수정하는 기능의 명세입니다.
	// 글 내용 수정과 함께, 새로운 파일을 추가하는 로직이 포함될 수 있습니다.
	public int update(BoardVO boardVO, MultipartFile [] attaches)throws Exception;
	
	// 게시글을 삭제하는 기능의 명세입니다.
	// 이 메서드는 단순히 글만 지우는 게 아니라, 해당 글에 첨부된 '파일들'까지
	// 서버와 DB에서 모두 삭제하는 과정을 포함해야 합니다.
	public int delete(BoardVO boardVO)throws Exception;
	
	// 게시글 수정 중, 특정 첨부파일 '하나만' 삭제하는 기능의 명세입니다.
	// 삭제할 파일 정보(boardFileVO)를 받아 파일 시스템과 DB에서 해당 파일을 제거합니다.
	public int fileDelete(BoardFileVO boardFileVO)throws Exception;

	// 특정 첨부파일 하나의 상세 정보를 조회하는 기능의 명세입니다. (DAO와 유사)
	// 파일 다운로드 시 파일 정보를 조회하기 위해 사용됩니다.
	public BoardFileVO fileDetail(BoardFileVO boardFileVO)throws Exception;
	
	// (아마도 AJAX용) 임시 파일을 서버에 업로드하는 기능의 명세입니다.
	// 파일(multipartFile)을 받아 서버에 저장한 후, 저장된 파일명을 반환합니다.
	// 사용자가 글쓰기 폼에서 파일을 추가하면, 임시로 서버에 먼저 올려둘 때 사용됩니다.
	public String boardFile(MultipartFile multipartFile) throws Exception;
	
	// (아마도 AJAX용) 임시 업로드된 파일을 삭제하는 기능의 명세입니다.
	// 사용자가 임시 업로드했던 파일을 취소했을 때, 서버에서 해당 파일을 지우기 위해 사용됩니다.
	public boolean boardFileDelete(String fileName)throws Exception;
}