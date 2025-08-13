package com.winter.app.products;

// 필요한 클래스들을 가져옵니다.
import java.util.List;

import com.winter.app.board.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// @Service: 이 클래스가 비즈니스 로직을 처리하는 '서비스' 계층의 컴포넌트임을 Spring에게 알립니다.
// 이 어노테이션을 통해 Spring이 이 클래스를 객체(Bean)로 관리하게 됩니다.
@Service
public class ProductService {

    // final로 선언된 필드는 생성자를 통해 주입받는 것이 권장됩니다.
    // 현재 이 클래스 내에서 사용되고 있지는 않네요.
    private final NoticeService noticeService;
	
	// 이 DAO를 통해 데이터베이스 관련 작업을 수행합니다.
	@Autowired
	private ProductDAO productDAO;

    // 생성자를 통한 의존성 주입(Constructor Injection) 방식입니다.
    // Spring이 ProductService 객체를 생성할 때, NoticeService 객체를 자동으로 넣어줍니다.
    ProductService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }
	
	// 상품 목록을 조회하는 서비스 메서드입니다.
	public List<ProductVO> list()throws Exception{
		// 별도의 비즈니스 로직 없이, DAO의 list 메서드를 그대로 호출하여 결과를 반환합니다.
		return productDAO.list();
	}
	
	// 특정 상품의 상세 정보를 조회하는 서비스 메서드입니다.
	public ProductVO detail(ProductVO productVO) throws Exception{
		// DAO의 detail 메서드를 그대로 호출하여 결과를 반환합니다.
		return productDAO.detail(productVO);
	}
	
	// 새로운 상품을 등록하는 서비스 메서드입니다.
	public int insert(ProductVO productVO)throws Exception{
		// DAO의 insert 메서드를 그대로 호출하여 결과를 반환합니다.
		return productDAO.insert(productVO);
	}
	
	// 기존 상품 정보를 수정하는 서비스 메서드입니다.
	public int update(ProductVO productVO)throws Exception{
		// DAO의 update 메서드를 그대로 호출하여 결과를 반환합니다.
		return productDAO.update(productVO);
	}
	
	// 특정 상품을 삭제하는 서비스 메서드입니다.
	public int delete(ProductVO productVO)throws Exception{
		// DAO의 delete 메서드를 그대로 호출하여 결과를 반환합니다.
		return productDAO.delete(productVO);
	}

}