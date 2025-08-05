package com.winter.app.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	
	@Autowired
	private ProductDAO productDAO;
	
	public List<ProductVO> list() throws Exception {
		return productDAO.list();
	}
	
	public ProductVO detail(ProductVO productVO) throws Exception {
		return productDAO.detail(productVO);
	}

}
