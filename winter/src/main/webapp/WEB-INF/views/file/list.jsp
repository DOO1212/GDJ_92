<%-- 페이지 지시자: JSP 설정 (언어/인코딩) --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- JSTL 코어/포맷 태그 사용 --%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
  <title>파일 목록</title>
  <%-- 표 가독성을 위한 간단 스타일 --%>
  <style>
    /* 단순 테이블 스타일 */
    table { width: 100%; border-collapse: collapse; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    th { background: #f4f4f4; }
  </style>
</head>
<%-- 본문 시작 --%>
<body>

  <%-- 상단 헤더 --%>
  <h1>파일 목록</h1>

  <%-- 업로드 폼 이동 링크 --%>
  <p><a href="${cPath}/file/uploadForm">파일 업로드</a></p>

  <%-- 파일 리스트가 비었을 때의 처리 --%>
  <c:if test="${empty files}">
    <%-- 안내 문구 출력 --%>
    <p>등록된 파일이 없습니다.</p>
  </c:if>

  <%-- 파일 리스트가 있을 때의 테이블 렌더링 --%>
  <c:if test="${not empty files}">
    <%-- 표 시작 --%>
    <table>
      <%-- 표 헤더 --%>
      <thead>
        <tr>
          <th>No</th>
          <th>원본 파일명</th>
          <th>크기(Bytes)</th>
          <th>타입</th>
          <th>등록일시</th>
          <th>다운로드</th>
          <th>삭제</th>
        </tr>
      </thead>
      <%-- 표 본문 --%>
      <tbody>
        <%-- files 목록을 순회하며 각 행을 출력 --%>
        <c:forEach var="f" items="${files}" varStatus="st">
          <tr>
            <%-- 일련 번호(1부터) --%>
            <td><c:out value="${st.index + 1}" /></td>
            <%-- 원본 파일명 출력 --%>
            <td><c:out value="${f.originName}" /></td>
            <%-- 파일 크기 출력 --%>
            <td><c:out value="${f.fileSize}" /></td>
            <%-- 콘텐츠 타입 출력 --%>
            <td><c:out value="${f.contentType}" /></td>
            <%-- 등록일시 출력 (그대로 출력하거나 fmt로 포맷) --%>
            <td>
              <c:choose>
                <%-- LocalDateTime이면 그냥 출력 --%>
                <c:when test="${not empty f.regDate}">
                  <c:out value="${f.regDate}" />
                </c:when>
                <%-- 혹시 Timestamp/Date 타입이면 fmt:formatDate 사용 예시 --%>
                <c:otherwise>
                  <fmt:formatDate value="${f.regDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                </c:otherwise>
              </c:choose>
            </td>
            <%-- 다운로드 링크: /file/download/{id} --%>
            <td>
              <a href="${cPath}/file/download/${f.id}">다운로드</a>
            </td>
            <%-- 삭제 폼: POST /file/delete/{id} --%>
            <td>
              <form action="${cPath}/file/delete/${f.id}" method="post" onsubmit="return confirm('삭제할까요?');">
                <button type="submit">삭제</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </c:if>

</body>
</html>
