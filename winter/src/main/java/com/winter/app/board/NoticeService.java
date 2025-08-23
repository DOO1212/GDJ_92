package com.winter.app.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.winter.app.commons.BoardFileVO;
import com.winter.app.commons.FileManager;

// @Service: 이 클래스는 비즈니스 로직을 담당하는 Service Bean임을 Spring에게 알려줍니다.
@Service
// @Transactional: 이 클래스의 public 메서드 내에서 일어나는 DB 작업들이 실패할 경우,
// 모든 작업을 없었던 일처럼 되돌려(롤백) 데이터의 일관성을 보장합니다.
@Transactional(rollbackFor = Exception.class)
public class NoticeService {
	
	// @Autowired: Spring이 관리하는 Bean 중에서 NoticeDAO 타입의 객체를 찾아 자동으로 연결(주입)해줍니다.
	@Autowired
	private NoticeDAO noticeDAO;
	
	// @Autowired: FileManager 타입의 객체를 자동으로 주입해줍니다.
	@Autowired
	private FileManager fileManager;
	
	// @Value: application.properties 파일의 'app.upload' 속성 값을 찾아 이 변수에 주입합니다.
	@Value("${app.upload}")
	private String upload;
	
	// @Value: application.properties 파일의 'board.notice' 속성 값을 찾아 이 변수에 주입합니다.
	@Value("${board.notice}")
	private String board;

	// 게시글과 '파일들'을 함께 받아 등록하는 메서드
	public int insert(BoardVO boardVO, MultipartFile [] attaches) throws Exception {
		// 1. DAO를 호출하여 게시글 내용(제목, 작성자 등)을 NOTICE 테이블에 먼저 저장합니다.
		int result = noticeDAO.insert(boardVO);
		// 이 때, NoticeDAO.xml의 useGeneratedKeys="true" 설정 덕분에
		// 파라미터로 받은 boardVO 객체의 boardNum 변수에는 DB가 방금 생성한 게시글 번호가 담기게 됩니다.
		
		// 2. 첨부파일이 없으면, 여기서 메서드를 종료합니다.
		if(attaches == null) {
			return result;
		}
	
		// 3. 첨부파일이 있다면, 반복문을 통해 각 파일을 하나씩 처리합니다.
		for(MultipartFile multipartFile : attaches) {
			// 3-1. 사용자가 파일을 첨부하지 않은 <input> 태그가 있을 수 있으므로, 파일이 비어있는지 확인합니다.
			if(multipartFile == null || multipartFile.isEmpty()) {
				continue; // 비어있으면 건너뛰고 다음 파일로 넘어갑니다.
			}
			
			// 3-2. FileManager를 이용해 파일을 서버의 물리적인 경로(예: D:/upload/notice/)에 저장합니다.
			// 저장 후, 중복되지 않게 생성된 파일명(예: UUID_원본파일명.jpg)을 반환받습니다.
			String fileName = fileManager.fileSave(upload + board, multipartFile);
			
			// 3-3. 파일 정보를 DB(NOTICEFILES 테이블)에 저장하기 위해 BoardFileVO 객체를 준비합니다.
			BoardFileVO boardFileVO = new BoardFileVO();
			boardFileVO.setOriName(multipartFile.getOriginalFilename()); // 사용자가 올린 원래 파일명
			boardFileVO.setSaveName(fileName);                           // 서버에 저장된 고유 파일명
			boardFileVO.setBoardNum(boardVO.getBoardNum());              // 1번 과정에서 얻은 게시글 번호
			
            // 3-4. DAO를 호출하여 파일 정보를 NOTICEFILES 테이블에 최종 저장합니다.
			result = noticeDAO.insertFile(boardFileVO);