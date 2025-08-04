package com.winter.app.products;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductKindVO {
	
	private Long kindNum;
	private String kindName;
	
	// 1:N
	// 양방향
	private List<ProductVO> list;

}
