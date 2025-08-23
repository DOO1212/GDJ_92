// 경로: java/com/winter/app/board/notice/NoticeService.java

// ... import 및 클래스 선언부 ...

@Service
@Transactional // 이 메서드 내의 모든 DB 작업이 전부 성공하거나 전부 실패하도록 보장
public class NoticeService implements BoardService {
	
	@Autowired
	private NoticeDAO noticeDAO;
	
	@Autowired
	private FileManager fileManager; // FileManager를 주입받아 사용
	
	@Value("${app.upload}") // application.properties의 app.upload 값을 주입
	private String upload;
	
	@Value("${board.notice}") // application.properties의 board.notice 값을 주입
	private String board;

	// ... list, detail 메서드 ...
	
	@Override
	public int insert(BoardVO boardVO, MultipartFile [] attaches) throws Exception {
		// 1. 게시글 내용(제목, 내용 등)을 NOTICE 테이블에 먼저 저장합니다.
		int result = noticeDAO.insert(boardVO);
		// insert 쿼리에 useGeneratedKeys 옵션을 사용했기 때문에,
		// 파라미터로 전달된 boardVO 객체의 boardNum 변수에 방금 생성된 게시글 번호가 담겨있게 됩니다.
		
		// 2. 첨부파일이 없으면 여기서 메서드를 종료합니다.
		if(attaches == null) {
			return result;
		}
	
		// 3. 첨부파일이 있으면, 각 파일을 서버에 저장하고 DB에 정보를 기록합니다. (반복)
		for(MultipartFile m : attaches) {
			// 파일이 비어있는지 체크 (예: <input type="file">을 추가만 하고 파일을 선택하지 않은 경우)
			if(m == null || m.isEmpty()) {
				continue; // 다음 파일로 넘어감
			}
			
			// 3-1. 파일을 서버 HDD에 저장합니다. (FileManager 사용)
			// 저장 경로는 "D:/upload/notice/" 와 같이 조합됩니다.
			String fileName = fileManager.fileSave(upload + board, m);
			
			// 3-2. 저장된 파일 정보를 DB(NOTICEFILES 테이블)에 저장합니다.
			BoardFileVO vo = new BoardFileVO();
			vo.setOriName(m.getOriginalFilename()); // 사용자가 올린 원래 파일명
			vo.setSaveName(fileName);              // 서버에 저장된 고유 파일명
			vo.setBoardNum(boardVO.getBoardNum()); // 방금 등록된 게시글의 번호
			result = noticeDAO.insertFile(vo);
		}
		
		return result;
	}
	
	// ... 나머지 메서드들 ...
}