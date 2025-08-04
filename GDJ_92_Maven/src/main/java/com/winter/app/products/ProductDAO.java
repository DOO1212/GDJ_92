package com.winter.app.products;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDAO {

	public List<ProductVO> list() throws Exception;
	
}
