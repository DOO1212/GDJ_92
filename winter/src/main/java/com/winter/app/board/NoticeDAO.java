package com.winter.app.board;

import org.apache.ibatis.annotations.Mapper;

//@Mapper: 이 인터페이스가 MyBatis의 Mapper임을 Spring에게 알려줍니다.
//Spring은 이 인터페이스를 구현한 프록시 객체를 자동으로 생성하여 DAO로 쓸 수 있게 해줍니다.
@Mapper
//BoardDAO를 상속받아 list, insert 등의 메서드 규칙을 그대로 물려받습니다.
public interface NoticeDAO extends BoardDAO {

	// 만약 Notice 게시판에만 필요한 특별한 기능이 있다면 여기에 추가하면 됩니다.
	// (예: public int updateHit(BoardVO boardVO) throws Exception;)
	// 지금은 파일 업로드/다운로드만 할 것이므로 비워둡니다.
}