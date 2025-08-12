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
<%-- 공통 CSS 설정이 담긴 jsp 파일을 포함시킵니다. --%>
<%@ include file="/WEB-INF/views/include/head_css.jsp" %>
</head>
<body id="page-top">
	<div id="wrapper">
		<%-- 사이드바 메뉴를 동적으로 포함시킵니다. --%>
		<c:import url="/WEB-INF/views/include/sidebar.jsp"></c:import>
		
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				<%-- 상단 메뉴바(topbar)를 포함시킵니다. --%>
				<c:import url="/WEB-INF/views/include/topbar.jsp"></c:import>
				<div class="container-fluid">
					<div class="col-md-8 offset-md-2">
						<%-- 컨트롤러에서 전달받은 게시판 이름(${board})을 출력합니다. --%>
						<h2>${board}</h2>
						
						<%-- 검색 기능 폼 --%>
						<div class="row">
							<form id="searchForm">
							<div class="input-group mb-3">
							  <%-- 페이지 번호를 전송하기 위한 hidden input. JavaScript에서 페이지 이동 시 이 값을 설정합니다. --%>
							  <input type="hidden" id="pageNum" name="pageNum">
							  
							  <%-- 검색 종류(제목, 내용, 작성자)를 선택하는 드롭다운 메뉴 --%>	
							  <select class="form-control" name="kind">
								  <%-- 
								  	삼항 연산자를 사용해 컨트롤러에서 받은 Pager 객체의 kind 값과 일치하는 옵션을 'selected' 상태로 만듭니다.
								  	이를 통해 검색 후에도 사용자가 선택한 검색 조건이 유지됩니다.
								  --%>
								  <option value="k1" ${pager.kind eq 'k1'?'selected':''}>Title</option>
								  <option value="k2" ${pager.kind eq 'k2'?'selected':''}>Contents</option>
								  <option value="k3" ${pager.kind eq 'k3'?'selected':''}>Writer</option>
								</select>
								
							  <%-- 검색어를 입력하는 텍스트 필드. value 값으로 기존 검색어를 유지합니다. --%>
							  <input type="text" class="form-control" value="${pager.keyword}" name="keyword" placeholder="검색어를 입력하세요">
							  
							  <%-- 검색 실행 버튼 --%>
							  <button class="btn btn-outline-secondary" type="submit" id="button-addon2">Search</button>
							</div>
							</form>
						</div>
						
						<%-- 게시물 목록을 보여주는 테이블 --%>
						<table class="table table-striped">
							<thead>
								<tr>
									<th>Num</th>
									<th>Title</th>
									<th>Writer</th>
									<th>Date</th>
									<th>Hit</th>
								</tr>
							</thead>
							<tbody>
							<%-- 컨트롤러에서 전달받은 게시물 목록(list)을 JSTL forEach로 반복 처리합니다. --%>
							<c:forEach items="${list}" var="vo">
								<tr>
									<td>${vo.boardNum}</td>
									<td>
									<%-- 
										답글의 깊이(depth)를 표현하기 위한 부분입니다.
										boardDepth 값만큼 '--'를 반복 출력하여 들여쓰기 효과를 줍니다.
										예: boardDepth=1이면 '--', boardDepth=2이면 '----'
									--%>
									<c:catch>
										<c:forEach begin="1" end="${vo.boardDepth}">--</c:forEach>
									</c:catch>

									<%-- 
										게시글 제목이 비어있는 경우(삭제된 글일 수 있음)와 그렇지 않은 경우를 분기 처리합니다. 
										계층형 게시판에서 원본 글이 삭제되어도 답글을 유지하기 위한 로직입니다.
									--%>
									<c:choose>
										<c:when test="${empty vo.boardTitle}">
											삭제된 글입니다
										</c:when>
										<c:otherwise>										
											<%-- 게시글 제목을 클릭하면 상세 페이지로 이동하는 링크 --%>
											<a href="./detail?boardNum=${vo.boardNum}">${vo.boardTitle}</a>
										</c:otherwise>
									</c:choose>
									</td>
									<td>${vo.boardWriter}</td>
									<td>${vo.boardDate}</td>
									<td>${vo.boardHit}</td>
								</tr>
							</c:forEach>	
							</tbody>
						</table>
						
						<%-- 페이지네이션(페이지 번호) UI --%>
						<div>
							<nav>
							  <ul class="pagination">
							    <%-- '이전' 페이지 블록으로 이동하는 버튼 --%>
							    <li class="page-item">
							      <%-- 
							      	data-pn 속성에 이동할 페이지 번호를 저장합니다. 
							      	JavaScript에서 이 값을 읽어 페이지를 이동시킵니다.
							      --%>
							      <a class="page-link pn" data-pn="${pager.startNum-1}" aria-label="Previous">
							        <span>&laquo;</span>
							      </a>
							    </li>
							    
							    <%-- 시작 페이지 번호부터 끝 페이지 번호까지 반복하며 페이지 링크를 생성합니다. --%>
							    <c:forEach begin="${pager.startNum}" end="${pager.endNum}" var="i">
							    <li class="page-item"><a class="page-link pn" data-pn="${i}">${i}</a></li>
							  	</c:forEach>
							  	
							    <%-- '다음' 페이지 블록으로 이동하는 버튼 --%>
							    <li class="page-item">
							      <a class="page-link pn" data-pn="${pager.endNum+1}" aria-label="Next">
							        <span>&raquo;</span>
							      </a>
							    </li>
							  </ul>
							</nav>
						</div>
						
						<%-- 글쓰기 페이지로 이동하는 버튼 --%>
						<div>
							<a href="./add" class="btn btn-outline-success">글쓰기</a>
						</div>
						
					</div>
				</div>
			</div>
			<%-- 페이지 하단 푸터(footer)를 포함시킵니다. --%>
			<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		</div>
	</div>
	
	<%-- 공통 JavaScript 파일들을 포함시킵니다. --%>
	<c:import url="/WEB-INF/views/include/tail.jsp"></c:import>
	
	<%-- 
		이 페이지의 동적 기능(페이지 번호 클릭, 검색)을 처리하는 
		외부 JavaScript 파일을 불러옵니다.
	--%>
	<script type="text/javascript" src="/js/board/board_list.js"></script>
</body>
</html>