package com.winter.app.board.products;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProductVO {
	
	private Long productNum;
	private String productName;
	private String productContents;
	private LocalDate productDate;
	private Double productRate;
	private Long kindNum;

}
