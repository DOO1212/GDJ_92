package com.winter.app.home;

import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
	
	public String home() {
		return "index";
	}

}
