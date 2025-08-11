<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ include file="/WEB-INF/views/include/head_css.jsp" %>
</head>
<body id="page-top">
	<div id="wrapper">
		<c:import url="/WEB-INF/views/include/sidebar.jsp"></c:import>
		
		<!-- Start  -->
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				<c:import url="/WEB-INF/views/include/topbar.jsp"></c:import>
				<div class="container-fluid">
					<h1>${board} Detail page</h1>
					<h3>${vo.boardTitle}</h3>
					<h3>${vo.boardContents}</h3>
					
					<div>
						<h2>첨부파일</h2>
						<c:forEach items="${vo.boardFileVOs}" var="f">
						<h4><a href="./fileDown?fileNum=${f.fileNum}">${f.oriName}</a></h4>
						<h4>${f.saveName}</h4>
						</c:forEach>
					</div>
					
					<div>
						<form id="frm">
							<input type="hidden" name="boardNum" value="${vo.boardNum}">
						</form>
						
						<button class="btn btn-outline-success action" data-kind="u">Update</button>
						<button class="btn btn-outline-danger action" data-kind="d">Delete</button>
						
						<c:if test="${board ne 'notice'}">
						<button class="btn btn-outline-primary action" data-kind="r">Reply</button>
						</c:if>
					</div>
				</div>
			</div>
			<!--  End Content  -->
			<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		</div>
		
		
	</div>
	<c:import url="/WEB-INF/views/include/tail.jsp"></c:import>
	<script type="text/javascript" src="/js/board/board_detail.js"></script>
</body>
</html>