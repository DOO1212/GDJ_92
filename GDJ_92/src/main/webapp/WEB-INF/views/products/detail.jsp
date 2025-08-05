<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<c:import url="/WEB-INF/views/include/head_css.jsp"></c:import>
</head>
<body id="page-top">

	<div id="wrapper">
		<c:import url="/WEB-INF/views/include/sidebar.jsp"></c:import>
		
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				<c:import url="/WEB-INF/views/include/topbar.jsp"></c:import>
				<div class="container-fluid">
				
					<!-- page contents -->
					<h1>Product Detail</h1>
					<h2>Name : ${vo.productName}</h2>
					<h2>Contents : ${vo.productContents}</h2>
					<h2>Rate : ${vo.productRate}</h2>
					<h2>Kind : ${vo.kindNum}</h2>
					<h2>kindName : ${vo.productKindVO.kindName}</h2>
					
				</div>
			</div>
			<!-- end contents -->
			<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		</div>
	</div>

	<c:import url="/WEB-INF/views/include/tail.jsp"></c:import>

</body>
</html>