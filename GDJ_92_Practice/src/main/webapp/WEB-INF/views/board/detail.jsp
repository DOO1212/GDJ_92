<%-- JSP 페이지 기본 설정 지시어 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- JSTL Core 태그 라이브러리를 'c'라는 접두사로 사용하겠다고 선언합니다. --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%-- 모든 페이지에서 공통으로 사용하는 CSS 설정들을 담은 jsp 파일을 포함시킵니다. --%>
<%@ include file="/WEB-INF/views/include/head_css.jsp" %>
</head>
<body id="page-top">
	<%-- 전체 페이지 레이아웃을 감싸는 div --%>
	<div id="wrapper">
		<%-- c:import 태그를 사용하여 사이드바 메뉴를 동적으로 포함시킵니다. --%>
		<c:import url="/WEB-INF/views/include/sidebar.jsp"></c:import>
		
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				<%-- 상단 메뉴바(topbar)를 포함시킵니다. --%>
				<c:import url="/WEB-INF/views/include/topbar.jsp"></c:import>
				
				<%-- 실제 페이지 내용이 표시될 컨테이너 --%>
				<div class="container-fluid">
					<%-- 
						컨트롤러에서 전달된 'board' 모델 속성값을 이용해 페이지 제목을 동적으로 표시합니다.
						예: 'notice'가 전달되면 "notice Detail page"라고 출력됩니다.
					--%>
					<h1>${board} Detail page</h1>
					
					<%-- 컨트롤러에서 전달된 게시글 정보(vo) 객체의 제목과 내용을 출력합니다. --%>
					<h3>${vo.boardTitle}</h3>
					<%-- 
						주의: ${vo.boardContents}와 같이 내용을 그대로 출력하면 
						XSS(Cross-Site Scripting) 공격에 취약할 수 있습니다.
						만약 내용에 <script> 태그가 포함되어 있다면 그대로 실행될 수 있으므로,
						보안 처리를 위해 JSTL의 <c:out> 태그를 사용하는 것이 안전합니다.
						예: <c:out value="${vo.boardContents}" />
					--%>
					<h3>${vo.boardContents}</h3>
					
					<%-- 첨부파일 목록을 표시하는 영역 --%>
					<div>
						<h2>첨부파일</h2>
						<%-- 
							게시글에 포함된 파일 목록(vo.boardFileVOs)을 순회하며 각 파일 정보를 출력합니다.
							'var="f"'는 반복 중인 현재 파일 객체를 참조하는 변수입니다.
						--%>
						<c:forEach items="${vo.boardFileVOs}" var="f">
						<%-- 
							파일 다운로드 링크입니다.
							- href의 ./fileDown 은 파일을 다운로드 처리하는 서버의 주소(URL)입니다.
							- ?fileNum=${f.fileNum} 부분은 서버에 어떤 파일을 다운로드할지 알려주기 위한 파라미터입니다.
							- ${f.oriName}은 사용자가 업로드했던 원본 파일명을 화면에 표시합니다.
						--%>
						<h4><a href="./fileDown?fileNum=${f.fileNum}">${f.oriName}</a></h4>
						<%-- 서버에 저장된 실제 파일명을 출력합니다. (개발 확인용으로 사용될 수 있습니다) --%>
						<h4>${f.saveName}</h4>
						</c:forEach>
					</div>
					
					<%-- 수정, 삭제, 답글 등 기능 버튼이 있는 영역 --%>
					<div>
						<%-- 
							이 form은 JavaScript에서 게시글 번호(boardNum)를 쉽게 가져다 쓰기 위해 사용됩니다.
							버튼 클릭 시 이 form이 직접 제출(submit)되지는 않습니다.
						--%>
						<form id="frm">
							<input type="hidden" name="boardNum" value="${vo.boardNum}">
						</form>
						
						<%-- 
							수정/삭제 버튼입니다.
							- class="action": JavaScript에서 이 클래스를 가진 버튼들에 공통 이벤트 리스너를 추가하기 위함입니다.
							- data-kind="u" 또는 "d": 클릭된 버튼이 어떤 종류의 동작(Update, Delete)을 원하는지 구분하기 위한 사용자 정의 속성입니다.
							  JavaScript에서 이 값을 읽어 분기 처리를 합니다.
						--%>
						<button class="btn btn-outline-success action" data-kind="u">Update</button>
						<button class="btn btn-outline-danger action" data-kind="d">Delete</button>
						
						<%-- 
							JSTL if문을 사용하여 특정 조건에서만 답글 버튼을 보여줍니다.
							게시판 종류(${board})가 'notice'가 아닐 경우에만 답글 버튼이 생성됩니다.
						--%>
						<c:if test="${board ne 'notice'}">
						<button class="btn btn-outline-primary action" data-kind="r">Reply</button>
						</c:if>
					</div>
				</div>
			</div>
			<%-- 페이지 하단 푸터(footer)를 포함시킵니다. --%>
			<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		</div>
	</div>
	
	<%-- 공통 JavaScript 파일들을 포함시킵니다. (ex: jQuery, Bootstrap JS 등) --%>
	<c:import url="/WEB-INF/views/include/tail.jsp"></c:import>
	
	<%-- 
		이 상세 페이지의 버튼 클릭 이벤트(수정, 삭제, 답글)를 처리하는 로직이 담긴
		외부 JavaScript 파일을 불러옵니다.
	--%>
	<script type="text/javascript" src="/js/board/board_detail.js"></script>
</body>
</html>