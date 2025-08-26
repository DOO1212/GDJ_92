<%-- /WEB-INF/views/file/detail.jsp --%>
<%-- 상세 화면의 기본 페이지 지시자 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- JSTL 코어/함수 태그를 사용한다 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<%-- 제목을 설정한다 --%>
<title>파일 상세</title>
<%-- 간단한 스타일을 추가한다 --%>
<style>
/* 카드 형태로 중앙 배치 */
.card {
	width: 600px;
	margin: 48px auto;
	border: 1px solid #eee;
	border-radius: 8px;
	padding: 16px;
}

.row {
	display: flex;
	justify-content: space-between;
	padding: 6px 0;
}

.btn {
	border: none;
	background: #205DAC;
	color: #fff;
	padding: 8px 14px;
	border-radius: 6px;
	cursor: pointer;
	text-decoration: none;
}

.btn:hover {
	background: #3E7AC8;
}

.toolbar {
	display: flex;
	gap: 8px;
	margin-top: 12px;
}
</style>
</head>
<body>
	<%-- 상세 데이터를 표시한다 --%>
	<div class="card">
		<div class="row">
			<strong>ID</strong><span><c:out value="${file.id}" /></span>
		</div>
		<div class="row">
			<strong>원본명</strong><span><c:out value="${file.originName}" /></span>
		</div>
		<div class="row">
			<strong>저장명</strong><span><c:out value="${file.savedName}" /></span>
		</div>
		<div class="row">
			<strong>MIME</strong><span><c:out value="${file.contentType}" /></span>
		</div>
		<div class="row">
			<strong>크기</strong><span><c:out value="${file.fileSize}" /></span>
		</div>
		<div class="row">
			<strong>확장자</strong><span><c:out value="${file.fileExt}" /></span>
		</div>
		<div class="row">
			<strong>경로</strong><span><c:out value="${file.filePath}" /></span>
		</div>
		<div class="row">
			<strong>등록일</strong><span><c:out value="${fn:replace(file.regDate, 'T', ' ')}" /></span>
		</div>
		<div class="row">
			<strong>수정일</strong><span><c:out value="${fn:replace(file.modDate, 'T', ' ')}" /></span>
		</div>

		<div class="toolbar">
			<%-- 다운로드 버튼 --%>
			<a class="btn" href="${pageContext.request.contextPath}/file/download/${file.id}">다운로드</a>
			<%-- 목록 버튼 --%>
			<a class="btn" href="${pageContext.request.contextPath}/file/list">목록</a>
			<%-- 삭제 폼 --%>
			<form action="${pageContext.request.contextPath}/file/delete/soft" method="post"
				style="display: inline;">
				<input type="hidden" name="id" value="${file.id}" />
				<button class="btn" type="submit">삭제</button>
			</form>
		</div>
	</div>
</body>
</html>
