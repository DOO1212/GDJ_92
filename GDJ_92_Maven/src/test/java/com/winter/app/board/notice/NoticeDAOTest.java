package com.winter.app.board.notice;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.winter.app.board.BoardVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class NoticeDAOTest {
	
	@Autowired
	private NoticeDAO noticeDAO;	
	
	@Test
	void listTest() throws Exception {
		// noticeDAO의 list() 메서드를 호출하여 DB의 모든 공지사항을 List 형태로 가져옵니다.
		List<BoardVO> list = noticeDAO.list();

		// assertNotNull(검증할_객체): 해당 객체가 null이 아니면 테스트 성공, null이면 실패로 처리합니다.
		// 데이터베이스 조회 결과가 없더라도 DAO는 null 대신 비어있는 List(size가 0인)를 반환하는 것이 좋습니다.
		assertNotNull(list);
	}
	
//	@Test
	void detailTest() throws Exception {
		NoticeVO noticeVO = new NoticeVO();
		noticeVO.setBoardNum(1L);
		BoardVO boardVO = noticeDAO.detail(noticeVO);
		log.info("result : {}", boardVO);
		assertNotNull(boardVO);
		
	}

//	@Test
//	void insertTest() throws Exception {
//		NoticeVO noticeVO = new NoticeVO();
//		noticeVO.setBoardTitle("title3");
//		noticeVO.setBoardContents("contents3");
//		noticeVO.setBoardWriter("writer3");
//		int result = noticeDAO.insert(noticeVO);
		
//	단정문
//	assertEquals(1, result);
//	}
	
//	@Test
//	void updateTest() throws Exception {
//		NoticeVO noticeVO = new NoticeVO();
//	    noticeVO.setBoardNum(3L);
//		noticeVO.setBoardTitle("title5");
//		noticeVO.setBoardContents("contents5");
//		noticeVO.setBoardWriter("writer5");
//		int result = noticeDAO.update(noticeVO);
		
//		단정문
//		assertEquals(1, result);
//	}
	
//	@Test
//	void deleteTest() throws Exception {
//		NoticeVO noticeVO = new NoticeVO();
//	}

}
