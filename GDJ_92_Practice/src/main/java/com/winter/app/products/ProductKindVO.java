package com.winter.app.products;

// 필요한 클래스들을 가져옵니다.
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProductKindVO {
	
	// 카테고리의 고유 번호 (Primary Key)
	private Long kindNum;
	// 카테고리의 이름 (예: "전자기기", "식품")
	private String kindName;
	
	// --- 1:N 관계 (하나의 카테고리 : 여러 개의 상품) ---
	
	// 현재 주석 처리되어 비활성화 상태입니다.
	// 만약 이 주석을 해제하면, 특정 카테고리 정보를 조회할 때 
	// 그 카테고리에 속한 모든 상품(ProductVO)들의 목록까지 한 번에 가져와 담을 수 있습니다.
	// DB에서 JOIN을 사용하고, MyBatis의 <collection> 태그로 매핑할 때 사용되는 구조입니다.
	// private List<ProductVO> list;

}