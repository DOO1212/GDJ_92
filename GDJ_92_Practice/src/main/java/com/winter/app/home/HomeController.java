package com.winter.app.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping; // 이 import문이 필요합니다!

@Controller
public class HomeController {

	// @GetMapping("/"): 이 어노테이션이 꼭 필요합니다!
	// 웹사이트의 가장 기본 주소(예: http://localhost/)로 GET 방식의 요청이 왔을 때
	// 이 메서드를 실행하라고 알려주는 역할을 합니다.
	@GetMapping("/")
	public String home() {
		// "index"라는 문자열을 반환합니다.
		// Spring은 application.properties의 설정을 바탕으로
		// 최종적으로 "/WEB-INF/views/index.jsp" 파일을 찾아 사용자에게 보여줍니다.
		return "index";
	}

}