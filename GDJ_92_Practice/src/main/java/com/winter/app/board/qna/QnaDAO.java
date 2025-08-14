package com.winter.app.board.qna;

import org.apache.ibatis.annotations.Mapper;

import com.winter.app.board.BoardDAO;
import com.winter.app.board.BoardVO;

// @Mapper: 이 인터페이스는 MyBatis의 매퍼(Mapper)라고 알려주는 어노테이션입니다.
// 이걸 붙이면 Spring이 알아서 이 인터페이스의 구현체를 자동으로 만들어 줍니다.
@Mapper
// QnaDAO 인터페이스는 BoardDAO 인터페이스를 '상속(extends)'합니다. 이게 핵심이에요!
// 이렇게 하면 list, detail, insert 등 BoardDAO의 모든 기능을 그대로 물려받습니다.
// 덕분에 게시판마다 똑같은 기본 기능을 또 만들 필요가 없어지죠.
public interface QnaDAO extends BoardDAO {

	// 원본글 등록 후, 자신의 글 번호를 그룹 번호(REF)로 설정하기 위한 메서드
	// Service의 insert() 메서드에서 호출됩니다.
	public int refUpdate(BoardVO boardVO) throws Exception;

	// 답글 등록 전, 기존 답글들의 순서(STEP)를 뒤로 한 칸씩 밀기 위한 메서드
	// Service의 reply() 메서드에서 호출됩니다.
	public int replyUpdate(QnaVO qnaVO) throws Exception;

}