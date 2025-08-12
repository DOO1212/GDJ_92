package com.winter.app.products;

// List 컬렉션을 사용하기 위해 가져옵니다.
import java.util.List;

// MyBatis의 @Mapper 어노테이션을 사용하기 위해 가져옵니다.
import org.apache.ibatis.annotations.Mapper;

// @Mapper: 이 인터페이스가 MyBatis의 매퍼(Mapper)임을 선언합니다.
// Spring이 이 인터페이스의 구현체를 자동으로 만들어주므로, 개발자가 직접 클래스를 만들 필요가 없습니다.
// 이 인터페이스의 메서드 이름과 동일한 ID를 가진 SQL 쿼리를 XML 파일에서 찾아 연결해 줍니다.
@Mapper
public interface ProductDAO {
	
	// 모든 상품의 목록을 조회하는 기능의 명세입니다.
	// ProductVO 객체들을 담은 List를 반환합니다.
	public List<ProductVO> list()throws Exception;
	
	// 특정 상품 하나의 상세 정보를 조회하는 기능의 명세입니다.
	// 조회할 상품의 번호가 담긴 productVO를 받아, 해당 상품의 모든 정보가 담긴 ProductVO를 반환합니다.
	public ProductVO detail(ProductVO productVO) throws Exception;
	
	// 새로운 상품 정보를 데이터베이스에 추가하는 기능의 명세입니다.
	// 성공적으로 추가된 행(row)의 개수(보통 1)를 정수(int)로 반환합니다.
	public int insert(ProductVO productVO)throws Exception;
	
	// 기존 상품 정보를 수정하는 기능의 명세입니다.
	// 성공적으로 수정된 행의 개수를 반환합니다.
	public int update(ProductVO productVO)throws Exception;
	
	// 특정 상품을 데이터베이스에서 삭제하는 기능의 명세입니다.
	// 성공적으로 삭제된 행의 개수를 반환합니다.
	public int delete(ProductVO productVO)throws Exception;

}