<%-- JSP 페이지의 언어, 콘텐츠 타입, 인코딩을 설정합니다. --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- JSTL Core 태그 라이브러리를 'c' 접두사로 사용하도록 선언합니다. --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%-- 공통 CSS 설정이 담긴 JSP 파일을 포함시킵니다. --%>
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
					<div class="row col-md-8 offset-md-2">
						<%-- 상품 목록을 표시하는 테이블 --%>
						<table class="table table-striped">
							<thead>
								<tr>
									<th>Num</th>
									<th>Title</th>
									<th>Rate</th>
									<th>Date</th>
								</tr>
							</thead>
							<tbody>
							<%-- 
								Controller에서 전달받은 상품 목록(list)을 JSTL forEach 태그로 반복 처리합니다.
								'var="vo"'는 반복 중인 현재 상품 객체를 참조하는 변수입니다.
							--%>
							<c:forEach items="${list}" var="vo">
								<tr>
									<%-- 각 상품(vo)의 속성값을 테이블 데이터(td)에 출력합니다. --%>
									<td>${vo.productNum}</td>
									<%-- 
										상품 이름을 상세 페이지로 가는 링크로 만듭니다.
										클릭 시 ./detail URL로 이동하며, productNum을 파라미터로 전달합니다.
									--%>
									<td><a href="./detail?productNum=${vo.productNum}">${vo.productName}</a></td>
									<td>${vo.productRate}</td>
									<td>${vo.productDate}</td>
								</tr>
							</c:forEach>	
							</tbody>
						</table>
						
						<%-- 상품 등록 페이지로 이동하는 버튼 --%>
						<div>
							<a href="./add" class="btn btn-outline-success">상품등록</a>
						</div>
						
					</div>
				</div>
			</div>
			<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		</div>
		
		
	</div>
	<%-- 공통 JavaScript 파일들(jQuery, Bootstrap 등)을 포함시킵니다. --%>
	<c:import url="/WEB-INF/views/include/tail.jsp"></c:import>
	
</body>
</html>