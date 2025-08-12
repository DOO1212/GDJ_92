<%-- JSP 페이지의 언어, 콘텐츠 타입, 인코딩을 설정합니다. --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- JSTL Core 태그 라이브러리를 'c' 접두사로 사용하도록 선언합니다. --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%-- 공통 CSS 설정이 담긴 JSP 파일을 포함시킵니다. --%>
<%@ include file="/WEB-INF/views/include/head_css.jsp"%>
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
				<div class="container-fluid">
					<h1>Product Detail</h1>

					<%-- 
						Controller에서 전달받은 모델 객체(vo)의 속성들을 출력합니다.
						EL(Expression Language)인 ${}를 사용하여 서버로부터 받은 데이터를 화면에 표시합니다.
					--%>
					<h2>Name : ${vo.productName}</h2>
					<h2>Contents : ${vo.productContents}</h2>
					<h2>Rate : ${vo.productRate}</h2>
					<h2>Kind : ${vo.kindNum}</h2>

					<%-- 
						${vo.productKindVO.kindName}
						이 코드는 vo 객체 안에 또 다른 객체(productKindVO)가 포함된 구조임을 보여줍니다.
						데이터베이스에서 JOIN을 통해 상품 정보와 상품 종류(Kind) 정보를 함께 조회한 결과입니다.
					--%>
					<h2>kindName : ${vo.productKindVO.kindName}</h2>

					<%-- 수정 및 삭제 기능을 위한 버튼 영역 --%>
					<div>
						<%-- 
							상품 삭제를 위한 form입니다. 
							- action="./delete": form을 제출(submit)할 때 요청을 보낼 URL입니다.
							- method="post": 데이터를 서버에 안전하게 전송하기 위해 POST 방식을 사용합니다. (데이터 변경/삭제 시 권장)
						--%>
						<form action="./delete" method="post">
							<%-- 
								어떤 상품을 삭제할지 서버에 알려주기 위한 hidden input 필드입니다.
								사용자 눈에는 보이지 않지만 form 제출 시 함께 전송됩니다.
							--%>
							<input type="hidden" name="productNum" value="${vo.productNum}">

							<%-- 
								상품 수정 페이지로 이동하는 링크(<a> 태그)입니다.
								- href 속성을 통해 GET 방식으로 ./update URL에 상품 번호를 파라미터로 전달합니다.
							--%>
							<a class="btn btn-success" href="./update?productNum=${vo.productNum}">Update </a>

							<%-- 이 버튼을 클릭하면 위 form이 제출되어 삭제가 진행됩니다. --%>
							<button class="btn btn-danger">Delete</button>
						</form>
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