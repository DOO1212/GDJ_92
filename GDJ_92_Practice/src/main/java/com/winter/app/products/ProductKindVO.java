package com.winter.app.products;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProductKindVO {
	
	private Long kindNum;
	private String kindName;
	
	//1:N
	//private List<ProductVO> list;

}
