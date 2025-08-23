<%-- 경로: /WEB-INF/views/board/add.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
<%-- 공통 CSS 파일을 포함합니다. --%>
<%@ include file="/WEB-INF/views/include/head_css.jsp" %>
</head>
<body id="page-top">
	<div id="wrapper">
		<%-- 사이드바 Include --%>
		<c:import url="/WEB-INF/views/include/sidebar.jsp"></c:import>
		
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				<%-- 상단바 Include --%>
				<c:import url="/WEB-INF/views/include/topbar.jsp"></c:import>
				
				<div class="container-fluid">
					<h2>게시글 작성</h2>
					
					<%-- 
					  action="./add": 폼 데이터는 현재 URL을 기준으로 './add' 경로로 전송됩니다. (예: /notice/add)
					  method="post": POST 방식으로 전송합니다.
					  enctype="multipart/form-data": 파일 전송을 위해 반드시 필요합니다.
					--%>
					<form action="./add" method="post" enctype="multipart/form-data">
						<div class="mb-3">
							<label for="boardTitle" class="form-label">제목</label>
							<%-- 
							  name="boardTitle": Controller의 BoardVO boardVO 파라미터의 boardTitle 필드와 연결됩니다. 
							--%>
							<input type="text" class="form-control" name="boardTitle" id="boardTitle">
						</div>
						
						<div class="mb-3">
							<label for="boardContents" class="form-label">내용</label>
							<%-- 
							  name="boardContents": Controller의 BoardVO boardVO 파라미터의 boardContents 필드와 연결됩니다. 
							--%>
							<textarea class="form-control" name="boardContents" id="boardContents" rows="10"></textarea>
						</div>

						<div class="mb-3">
							<label for="attaches" class="form-label">첨부파일</label>
							<%-- 
							  name="attaches": Controller의 MultipartFile[] attaches 파라미터와 연결됩니다.
							  multiple: 여러 파일을 한 번에 선택할 수 있게 합니다.
							--%>
							<input type="file" class="form-control" name="attaches" id="attaches" multiple>
						</div>

						<button type="submit" class="btn btn-primary">등록</button>
					</form>
					</div>
			</div>
			<%-- 푸터 Include --%>
			<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		</div>
	</div>
	<%-- 공통 JS 파일을 포함합니다. --%>
	<c:import url="/WEB-INF/views/include/tail.jsp"></c:import>
</body>
</html>