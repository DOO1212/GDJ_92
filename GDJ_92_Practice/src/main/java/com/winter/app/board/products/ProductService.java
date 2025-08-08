package com.winter.app.board.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winter.app.board.notice.NoticeService;

@Service
public class ProductService {
	
	private final NoticeService noticeService;	

	@Autowired
	private ProductDAO productDAO;
	
	ProductService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	
	

}
