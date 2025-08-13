package com.winter.app.products;

// 날짜(LocalDate)와 날짜/시간(LocalDateTime) 정보를 다루기 위한 클래스를 가져옵니다.
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductVO {
	
	// 상품의 고유 번호 (Primary Key)
	private Long productNum;
	// 상품명
	private String productName;
	// 상품 상세 설명
	private String productContents;
	// 상품 등록일 (시간 정보가 없는 날짜만 저장)
	private LocalDate productDate;
	// 상품 평점 (소수점을 포함할 수 있으므로 Double 타입 사용)
	private Double productRate;
	// 상품 종류 번호 (Foreign Key, ProductKind 테이블과 연결됨)
	private Long kindNum;
	
	// --- 1:1 관계 (하나의 상품 : 하나의 카테고리) ---
	
	// 이 상품이 어떤 카테고리(ProductKind)에 속하는지에 대한 정보를
	// 단순한 번호(kindNum)뿐만 아니라, 카테고리 객체(ProductKindVO) 자체로도 가지고 있습니다.
	// DB에서 JOIN을 통해 상품 정보와 카테고리 정보를 함께 조회하여 여기에 담을 수 있습니다.
	// 이렇게 하면 Service나 View에서 productVO.getProductKindVO().getKindName()처럼
	// 카테고리 이름에 바로 접근할 수 있어 매우 편리합니다.
	//
	// '단방향'이란, ProductVO는 자신의 ProductKindVO를 알지만,
	// ProductKindVO는 자신에게 속한 ProductVO를 모른다는 의미입니다.
	private ProductKindVO productKindVO;

}