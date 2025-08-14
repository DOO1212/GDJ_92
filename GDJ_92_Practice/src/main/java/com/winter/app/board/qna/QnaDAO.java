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

	// 답글 순서를 업데이트하기 위한 메서드입니다.
	// 새 답글이 들어갈 자리를 만들기 위해, 같은 그룹(REF)에 있는 기존 답글들의 순서(STEP)를
	// 뒤로 한 칸씩 밀어내는 역할을 합니다. (예: UPDATE QNA SET STEP = STEP + 1 WHERE...)
	public int refUpdate(BoardVO boardVO) throws Exception;

	// 실제 답글 한 줄을 데이터베이스에 INSERT하는 메서드입니다.
	// 서비스(Service)에서 계산된 그룹번호(REF), 순서(STEP), 깊이(DEPTH)가 포함된
	// QnaVO 객체를 받아 DB에 저장합니다.
	public int replyInsert(QnaVO qnaVO) throws Exception;

}