package com.winter.app.commons;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
// FileVO 클래스를 상속합니다.
// 이로써 BoardFileVO는 FileVO가 가진 fileNum, oriName, saveName 변수를 모두 갖게 됩니다.
public class BoardFileVO extends FileVO {

	// boardNum: 이 파일이 어떤 게시글에 속해 있는지를 알려주는 게시글 번호입니다.
	private Long boardNum;
}