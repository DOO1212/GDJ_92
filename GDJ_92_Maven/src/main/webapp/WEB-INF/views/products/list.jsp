<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
					<div class="row col-md-8 offset-md-2">
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
								<c:forEach items="${list}" var="vo">
									<tr>
										<td>${vo.productNum}</td>
										<td><a href="./detail?productNum=${vo.productNum}">${vo.productName}</a></td>
										<td>${vo.productRate}</td>
										<td>${vo.productDate}</td>
									</tr>
								</c:forEach>
							</tbody>
					</div>
				</div>
				<!-- end contents -->
				<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
			</div>
		</div>

		<c:import url="/WEB-INF/views/include/tail.jsp"></c:import>
</body>
</html>