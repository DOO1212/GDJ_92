<%-- JSP 페이지의 언어, 콘텐츠 타입, 인코딩을 설정합니다. --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- JSTL Core 태그 라이브러리를 'c' 접두사로 사용하도록 선언합니다. --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Product Form</title>
<%-- 공통 CSS 설정이 담긴 JSP 파일을 포함시킵니다. --%>
<%@ include file="/WEB-INF/views/include/head_css.jsp"%>
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
					<div class="row justify-content-center ">
						<div class="col-md-6">
							<%-- 
								상품 정보를 서버로 전송하는 폼입니다.
								- method="post": 데이터 생성 또는 수정을 위해 POST 방식으로 요청합니다.
								- action 속성이 비어있으면 현재 페이지의 URL로 폼 데이터를 전송합니다.
							--%>
							<form method="post">
								<%--
									수정할 상품의 고유 번호(productNum)입니다.
									사용자에게 보이지 않는 hidden 타입으로, 폼 제출 시 서버에 어떤 상품을 수정할지 알려주는 역할을 합니다.
								--%>
								<input type="hidden" name="productNum" value="${vo.productNum}">

								<%-- 상품 종류를 선택하는 드롭다운 (select) --%>
								<div class="mb-3">
									<label for="kindNum" class="form-label">상품종류</label> <select
										class="form-control form-select" name="kindNum">
										<%-- 
											JSTL EL의 삼항 연산자를 사용해 현재 상품의 종류(vo.kindNum)에 해당하는 옵션을 자동으로 선택(selected)해줍니다.
											예: vo.kindNum이 '1'이면 '예금' 옵션에 selected 속성이 추가됩니다.
										--%>
										<option value="1" ${vo.kindNum eq '1'?'selected':''}>예금</option>
										<option value="2" ${vo.kindNum eq '2'?'selected':''}>적금</option>
										<option value="3" ${vo.kindNum eq '3'?'selected':''}>대출</option>
									</select>
								</div>

								<%-- 상품 이름을 입력하는 텍스트 필드 --%>
								<div class="mb-3">
									<label for="productName" class="form-label">상품이름</label> <input type="text"
										class="form-control" name="productName" id="productName" value="${vo.productName}">
								</div>

								<%-- 상품 상세 내용을 입력하는 텍스트 영역 --%>
								<div class="mb-3">
									<label for="contents" class="form-label">상품내용</label>
									<textarea class="form-control" id="contents" rows="9" name="productContents">${vo.productContents}</textarea>
								</div>

								<%-- 상품 판매 기간(또는 만기일)을 입력하는 날짜 필드 --%>
								<div class="mb-3">
									<label for="productDate" class="form-label">상품사용기간</label> <input type="date"
										class="form-control" name="productDate" id="productDate" value="${vo.productDate}">
								</div>

								<%-- 상품 이자율을 입력하는 필드 --%>
								<div class="mb-3">
									<label for="productRate" class="form-label">상품이자</label>
									<%-- 
										'datetime' 타입은 HTML5에서 더 이상 사용되지 않습니다.
										이율과 같은 숫자를 입력받으려면 'type="number"' 와 'step="0.01"' 속성을 사용하는 것이 더 적합합니다.
										예: <input type="number" step="0.01" ... >
									--%>
									<input type="datetime" class="form-control" name="productRate" id="productRate"
										value="${vo.productRate}">
								</div>

								<%-- 폼 데이터를 서버로 최종 제출하는 버튼 --%>
								<button type="submit" class="btn btn-primary">Submit</button>
							</form>
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