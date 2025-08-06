package com.winter.app.products;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDAO {
	
	public List<ProductVO> list()throws Exception;
	
	public ProductVO detail(ProductVO productVO) throws Exception;
	
	public int insert(ProductVO productVO)throws Exception;
	
	public int update(ProductVO productVO)throws Exception;
	
	public int delete(ProductVO productVO)throws Exception;

}
