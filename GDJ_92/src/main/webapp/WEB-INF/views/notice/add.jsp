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
					<form action="./add" method="post">
						<div class="mb-4">
							<label for="title" class="form-label">Title</label> <input type="text" class="form-control"
								id="title" name="boardTitle">
						</div>
						
						<div class="mb-4">
							<label for="writer" class="form-label">Writer</label> <input type="text" class="form-control"
								id="writer" name="boardWriter">
						</div>
						
						<div class="mb-4">
							<label for="content">Comments</label>
							<textarea class="form-control" placeholder="Write your content here!" id="content"
								style="height: 100px" name="boardContent"></textarea>
						</div>
						
						<button type="submit" class="btn btn-primary">Submit</button>
						
					</form>

				</div>
			</div>
			<!-- end contents -->
			<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		</div>
	</div>

	<c:import url="/WEB-INF/views/include/tail.jsp"></c:import>

</body>
</html>