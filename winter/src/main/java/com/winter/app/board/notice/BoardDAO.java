package com.winter.app.board.notice;

import org.apache.ibatis.annotations.Mapper;

import com.winter.app.board.BoardDAO;

@Mapper // MyBatis가 이 인터페이스를 인식하고 실제 객체를 만들어주는 어노테이션
public interface NoticeDAO extends BoardDAO { // BoardDAO의 기능을 모두 물려받음

}