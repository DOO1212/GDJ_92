package com.winter.app.products;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductVO {
	
	private Long productNum;
	private String productName;
	private String productContents;
	private LocalDateTime productDate;
	private Double productRate;
	private Long kindNum;
	
	// 1:1
	// 단방향
	private ProductVO productVO;
	

}
