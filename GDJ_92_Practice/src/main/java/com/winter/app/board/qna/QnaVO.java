package com.winter.app.board.qna;

import com.winter.app.board.BoardVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 보일러플레이트 코드(반복적으로 작성하는 코드)를 자동으로 생성합니다.
@Setter
@Getter
@ToString
// QnaVO 클래스는 BoardVO 클래스를 '상속'합니다.
// 따라서 boardNum, boardTitle 등 BoardVO의 모든 필드를 기본으로 가집니다.
public class QnaVO extends BoardVO {

	// 계층형 게시판의 핵심 필드 3가지

	// 1. boardRef: 그룹 번호. 원본글과 그에 달린 모든 답글은 동일한 boardRef 값을 가집니다.
	// 어떤 글들이 하나의 묶음인지를 나타냅니다.
	private Long boardRef;

	// 2. boardStep: 그룹 내 순서. 같은 그룹(boardRef) 안에서 글이 표시될 순서를 정합니다.
	// 숫자가 작을수록 위에 표시됩니다.
	private Long boardStep;

	// 3. boardDepth: 글의 깊이 (들여쓰기 레벨).
	// 원본글은 0, 그에 대한 답글은 1, 답글의 답글은 2와 같이 설정되어 시각적으로 계층을 보여줍니다.
	private Long boardDepth;

	// --- 커스텀 Getter 메서드 ---
	// @Getter 어노테이션이 있지만, 아래에 같은 이름의 메서드를 직접 작성하면
	// Lombok이 만든 메서드를 덮어쓰고 이 코드가 대신 사용됩니다.

	// boardRef 필드 값을 가져오는 메서드입니다.
	public Long getBoardRef() {
		// 만약 DB에서 가져온 boardRef 값이 NULL이라면, NullPointerException을 방지하기 위해
		// 기본값으로 0L을 설정해 줍니다. 이런 방어적인 코드는 안정성을 높여줍니다.
		if (this.boardRef == null) {
			this.boardRef = 0L;
		}
		return this.boardRef;
	}

	// boardStep 필드 값을 가져오는 메서드입니다.
	public Long getBoardStep() {
		// boardRef와 마찬가지로, NULL일 경우 기본값 0L을 반환합니다.
		if (this.boardStep == null) {
			this.boardStep = 0L;
		}
		return this.boardStep;
	}

	// boardDepth 필드 값을 가져오는 메서드입니다.
	public Long getBoardDepth() {
		// boardDepth 역시 NULL일 경우 기본값 0L을 반환합니다.
		if (this.boardDepth == null) {
			this.boardDepth = 0L;
		}
		return this.boardDepth;
	}

}