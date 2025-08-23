// 경로: java/com/winter/app/commons/FileVO.java

package com.winter.app.commons;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// @Setter: 각 변수의 값을 설정하는 setName(..) 메서드를 자동으로 만들어줍니다.
// @Getter: 각 변수의 값을 가져오는 getName() 메서드를 자동으로 만들어줍니다.
// @ToString: 객체의 변수 값들을 문자열로 예쁘게 출력하는 toString() 메서드를 만들어줍니다.
@Setter
@Getter
@ToString
public class FileVO {
	
	// fileNum: 파일의 고유 번호를 저장할 변수입니다. (데이터타입: Long)
	private Long fileNum;
	// oriName: 사용자가 업로드한 원본 파일 이름을 저장할 변수입니다. (데이터타입: String)
	private String oriName;
	// saveName: 서버에 저장될 때 중복을 피하기 위해 생성된 고유한 파일 이름을 저장할 변수입니다.
	private String saveName;

}