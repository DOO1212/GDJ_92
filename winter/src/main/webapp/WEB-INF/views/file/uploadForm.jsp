<%-- 페이지 지시자: JSP 설정 (언어/인코딩) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- JSTL 코어 태그 사용을 위한 선언 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- 컨텍스트 경로를 변수로 보관 (링크 작성 편의) --%>
<c:set var="cPath" value="${pageContext.request.contextPath}" />

<%-- 문서 타입 선언 --%>
<!DOCTYPE html>
<%-- HTML 시작 --%>
<html>
<%-- 문서 메타 정보 설정 --%>
<head>
<%-- 문서 인코딩 지정 --%>
<meta charset="UTF-8">
<%-- 브라우저 탭에 표시될 제목 --%>
<title>파일 업로드</title>
</head>
<%-- 문서 본문 시작 --%>
<body>

	<%-- 업로드 안내 헤더 --%>
	<h1>파일 업로드</h1>

	<%-- 루트(D:/upload/)에 저장하는 간단 업로드 폼 --%>
	<form action="${cPath}/file/upload" method="post" enctype="multipart/form-data"
		style="margin-top: 16px">
		<%-- 업로드할 파일 선택 --%>
		<input type="file" name="file" required />
		<%-- 업로드 전송 버튼 --%>
		<button type="submit">간단 업로드(루트)</button>
	</form>

	<%-- 파일 목록 페이지로 이동하는 링크 --%>
	<p style="margin-top: 16px">
		<a href="${cPath}/file/list">파일 목록 보기</a>
	</p>

</body>
</html>
