<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ include file="/WEB-INF/views/include/head_css.jsp"%>
</head>
<body id="page-top">
	<div id="wrapper">
		<c:import url="/WEB-INF/views/include/sidebar.jsp"></c:import>

		<!-- Start  -->
		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				<c:import url="/WEB-INF/views/include/topbar.jsp"></c:import>
				<div class="container-fluid">
					<!-- page contents 내용 -->
					<div class="row justify-content-center ">
						<div class="col-md-6">
							<form  method="post">
								<input type="hidden" name="boardNum" value="${vo.boardNum}">
								<div class="mb-3">
									<label for="writer" class="form-label">Writer</label> 
									<input type="text" class="form-control" name="boardWriter"
										id="writer" aria-describedby="writerHelp" value="${vo.boardWriter}">
								</div>
								<div class="mb-3">
									<label for="title" class="form-label">Title</label> 
									<input type="text" class="form-control" name="boardTitle"
										id="title" aria-describedby="writerHelp" value="${vo.boardTitle}">
								</div>
								
								<div class="mb-3">
								  <label for="contents" class="form-label">Contents</label>
								  <textarea class="form-control" id="contents" rows="9" name="boardContents">${vo.boardContents}</textarea>
								</div>
								
								<button type="submit" class="btn btn-primary">Submit</button>
							</form>
						</div>
					</div>

				</div>
			</div>
			<!--  End Content  -->
			<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		</div>


	</div>
	<c:import url="/WEB-INF/views/include/tail.jsp"></c:import>

</body>
</html>