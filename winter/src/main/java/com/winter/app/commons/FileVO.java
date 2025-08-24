package com.winter.app.commons;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FileVO {
	
	private Long fileNum;   // 파일 고유 번호 (PK)
	private String oriName; // 원본 파일명
	private String saveName;// 서버 저장명

}