package com.winter.app.boards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
	
	@Autowired
	private BoardDAO boardDAO;
	
	public void list() {
		System.out.println("list");
	}
	
	public void detail() {
		System.out.println("detail");
	}

}
