<%-- /WEB-INF/views/file/uploadForm.jsp --%>
<%-- 업로드 폼의 기본 페이지 지시자 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%-- 제목을 설정한다 --%>
<title>파일 업로드</title>
<%-- 간단한 스타일을 추가한다 --%>
<style>
/* 래퍼와 버튼 스타일 */
.wrap {
	width: 480px;
	margin: 48px auto;
}

.btn {
	border: none;
	background: #205DAC;
	color: #fff;
	padding: 8px 14px;
	border-radius: 6px;
	cursor: pointer;
}

.btn:hover {
	background: #3E7AC8;
}
</style>
</head>
<body>
	<%-- 업로드 폼을 렌더링한다 --%>
	<div class="wrap">
		<%-- 목록으로 돌아가는 버튼 --%>
		<p>
			<a class="btn" href="${pageContext.request.contextPath}/file/list">목록</a>
		</p>
		<%-- multipart/form-data 로 설정하고 input name="file" 을 컨트롤러와 일치시킨다 --%>
		<form action="${pageContext.request.contextPath}/file/upload" method="post"
			enctype="multipart/form-data">
			<p>
				<input type="file" name="file" required>
			</p>
			<p>
				<button class="btn" type="submit">업로드</button>
			</p>
		</form>
	</div>
</body>
</html>
