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
					<!-- page contents 내용 -->
										<div class="row justify-content-center ">
						<div class="col-md-6">
							<form  method="post">
								<input type="hidden" name="productNum" value="${vo.productNum}">
								<div class="mb-3">
									<label for="kindNum" class="form-label">상품종류</label> 
									<select class="form-control form-select" name="kindNum">
										<option value="1" ${vo.kindNum eq '1'?'selected':''}>예금</option>
										<option value="2" ${vo.kindNum eq '2'?'selected':''}>적금</option>
										<option value="3" ${vo.kindNum eq '3'?'selected':''}>대출</option>
									</select>
								</div>									
								<div class="mb-3">
									<label for="productName" class="form-label">상품이름</label> 
									<input type="text" class="form-control" name="productName"
										id="productName" aria-describedby="writerHelp" value="${vo.productName}">
								</div>
								
								<div class="mb-3">
								  <label for="contents" class="form-label">상품내용</label>
								  <textarea class="form-control" id="contents" rows="9" name="productContents">${vo.productContents}</textarea>
								</div>
								<div class="mb-3">
									<label for="productDate" class="form-label">상품사용기간</label> 
									<input type="date" class="form-control" name="productDate"
										id="productDate" aria-describedby="writerHelp" value="${vo.productDate}">
								</div>		
								
								<div class="mb-3">
									<label for="productRate" class="form-label">상품이자</label> 
									<input type="datetime" class="form-control" name="productRate"
										id="productRate" aria-describedby="writerHelp" value="${vo.productRate}">
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