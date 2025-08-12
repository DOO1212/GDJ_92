// 이 파일이 속한 패키지를 선언합니다.
package com.winter.app.board.notice;

// JUnit 5의 assertion(단정문) 메서드를 static으로 임포트하여 메서드 이름을 바로 사용할 수 있게 합니다.
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

// JUnit 5에서 테스트 메서드를 지정하기 위한 @Test 어노테이션을 임포트합니다.
import org.junit.jupiter.api.Test;
// Spring의 의존성 주입(DI)을 위한 @Autowired 어노테이션을 임포트합니다.
import org.springframework.beans.factory.annotation.Autowired;
// Spring Boot 테스트 환경을 구성해주는 핵심 어노테이션을 임포트합니다.
import org.springframework.boot.test.context.SpringBootTest;

// BoardVO 클래스를 임포트합니다.
import com.winter.app.board.BoardVO;

// Lombok의 로깅 어노테이션을 임포트합니다.
import lombok.extern.slf4j.Slf4j;

/**
 * NoticeDAO의 메서드들을 테스트하기 위한 클래스입니다.
 */
// @SpringBootTest: Spring Boot 애플리케이션 테스트에 필요한 모든 설정을 로드합니다.
// 이 어노테이션이 있으면 실제 애플리케이션처럼 모든 Bean들이 Spring 컨테이너에 등록되어 테스트 시 의존성 주입이 가능해집니다.
@SpringBootTest
// @Slf4j: Lombok 어노테이션으로, 컴파일 시 자동으로 'private static final Logger log = LoggerFactory.getLogger(NoticeDAOTest.class);' 코드를 생성해줍니다.
// 이를 통해 'log' 변수로 쉽게 로그를 출력할 수 있습니다.
@Slf4j
class NoticeDAOTest {
	
	// @Autowired: Spring 컨테이너에 등록된 Bean 중에서 NoticeDAO 타입의 Bean을 찾아 자동으로 주입(DI)해줍니다.
	@Autowired
	private NoticeDAO noticeDAO;
	
	// '@Test'가 주석 처리되어 있어 현재 이 테스트는 실행되지 않습니다.
	//@Test
//	void listTest()throws Exception{
//		// noticeDAO의 list 메서드를 호출하여 전체 공지사항 목록을 가져옵니다.
//		List<BoardVO> list = noticeDAO.list();
//		
//		// 단정문(Assertion): 조회된 리스트의 크기가 0이 아닐 것이라고 단정합니다.
//		// 즉, 데이터베이스에 최소 하나 이상의 데이터가 있을 것으로 기대하는 테스트입니다.
//		// 참고: list 객체 자체는 비어있더라도 null이 아니므로, list.size()로 비교하는 것이 더 명확합니다. (e.g., assertNotEquals(0, list.size());)
//		assertNotEquals(0, list.size());
//				
//	}
	
	// '@Test'가 주석 처리되어 있어 현재 이 테스트는 실행되지 않습니다.
	//@Test
	void detailTest()throws Exception{
		// 상세 정보를 조회할 NoticeVO 객체를 생성합니다.
		NoticeVO noticeVO = new NoticeVO();
		// 조회할 게시글의 번호를 1L로 설정합니다. (L은 Long 타입임을 명시)
		noticeVO.setBoardNum(1L);
		// noticeDAO의 detail 메서드를 호출하여 특정 게시글의 상세 정보를 가져옵니다.
		BoardVO boardVO = noticeDAO.detail(noticeVO);

		// SLF4J 로거를 사용하여 조회된 결과를 콘솔에 출력합니다. {} 부분에 boardVO.toString() 결과가 들어갑니다.
		log.info("result : {}", boardVO);
		
		// 단정문(Assertion): 조회된 boardVO 객체가 null이 아닐 것이라고 단정합니다.
		// 즉, 해당 번호의 게시글이 데이터베이스에 존재해야 테스트를 통과합니다.
		assertNotNull(boardVO);
	}

	// @Test: 이 메서드가 JUnit 테스트 케이스임을 나타냅니다. 현재 이 테스트만 활성화되어 있습니다.
	@Test
	void insertTest()throws Exception {
		// for 반복문을 사용하여 테스트용 데이터 105개를 생성합니다.
		for(int i=0;i<105;i++) {
			// 각 반복마다 새로운 NoticeVO 객체를 생성합니다.
			NoticeVO noticeVO = new NoticeVO();
			noticeVO.setBoardTitle("title"+i);
			noticeVO.setBoardContents("contents"+i);
			noticeVO.setBoardWriter("writer"+i);
			
			// noticeDAO의 insert 메서드를 호출하여 데이터베이스에 저장합니다.
			// result 변수에는 insert SQL 실행 후 영향받은 row의 수가 반환됩니다. (보통 1)
			int result = noticeDAO.insert(noticeVO);

			// 10번 insert 할 때마다 0.5초(500ms)씩 스레드를 잠시 멈춥니다.
			// 이는 DB에 과부하를 주지 않거나, 등록 시간을 다르게 하기 위한 등의 목적으로 사용될 수 있습니다.
			if(i%10 == 0) {
				Thread.sleep(500);
			}
		}
		
		// 현재 이 테스트는 최종적인 결과 검증(단정문) 없이 데이터 삽입만 수행하고 있습니다.
		// assertEquals(1, result); 와 같은 단정문을 추가하여 각 insert가 성공했는지 검증할 수 있습니다.
	}
}