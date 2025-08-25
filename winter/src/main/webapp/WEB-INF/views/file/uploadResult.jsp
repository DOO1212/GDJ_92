<%-- 페이지 지시자: JSP 설정 (언어/인코딩) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- JSTL 코어 태그 사용 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- 컨텍스트 경로 보관 --%>
<c:set var="cPath" value="${pageContext.request.contextPath}" />

<%-- 문서 타입 선언 --%>
<!DOCTYPE html>
<%-- HTML 시작 --%>
<html>
<%-- 헤더 영역 --%>
<head>
  <%-- 문서 인코딩 지정 --%>
  <meta charset="UTF-8">
  <%-- 브라우저 탭 제목 --%>
  <title>업로드 결과</title>
</head>
<%-- 본문 시작 --%>
<body>

  <%-- 업로드 결과 메시지 출력 (컨트롤러에서 model에 저장) --%>
  <h2>${message}</h2>

  <%-- 업로드 폼으로 돌아가는 링크 --%>
  <p><a href="${cPath}/file/uploadForm">다시 업로드</a></p>

  <%-- 파일 목록으로 이동하는 링크 --%>
  <p><a href="${cPath}/file/list">파일 목록</a></p>

</body>
</html>
