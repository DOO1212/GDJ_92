<%-- /WEB-INF/views/file/list.jsp --%>
<%-- 목록 화면의 기본 페이지 지시자 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- JSTL 코어/함수 태그를 사용한다 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<%-- 제목을 설정한다 --%>
<title>파일 목록</title>
<%-- 간단한 스타일을 추가한다 --%>
<style>
/* 테이블 기본 스타일 */
table {
	border-collapse: collapse;
	width: 70%;
	margin: 24px auto;
}

th, td {
	border-bottom: 1px solid #ddd;
	padding: 10px;
	text-align: left;
}

th {
	background: #fafafa;
}
/* 상단 영역 */
.toolbar {
	width: 70%;
	margin: 24px auto;
	display: flex;
	justify-content: space-between;
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

.msg {
	color: #205DAC;
}
</style>
</head>
<body>
	<%-- 상단 툴바: 메시지와 업로드 버튼을 배치한다 --%>
	<div class="toolbar">
		<div class="msg">
			<c:out value="${msg}" />
		</div>
		<div>
			<%-- 업로드 폼으로 이동하는 링크 버튼 --%>
			<a class="btn" href="${pageContext.request.contextPath}/file/upload">업로드</a>
		</div>
	</div>

	<%-- 파일 목록을 표로 출력한다 --%>
	<table>
		<thead>
			<tr>
				<th>번호</th>
				<th>원본 파일명</th>
				<th>크기</th>
				<th>MIME</th>
				<th>등록일</th>
				<th>다운로드</th>
				<th>삭제</th>
			</tr>
		</thead>
		<tbody>
			<%-- 목록이 비어있을 때를 처리한다 --%>
			<c:if test="${empty list}">
				<tr>
					<td colspan="7">데이터가 없습니다</td>
				</tr>
			</c:if>
			<%-- 목록이 있을 때 각 행을 출력한다 --%>
			<c:forEach var="f" items="${list}">
				<tr>
					<%-- 상세로 이동하는 링크는 숫자 id만 매칭되도록 컨트롤러를 수정한 상태를 가정한다 --%>
					<td><c:out value="${f.id}" /></td>
					<td><a href="${pageContext.request.contextPath}/file/${f.id}"><c:out value="${f.originName}" /></a></td>
					<td><c:out value="${f.fileSize}" /></td>
					<td><c:out value="${f.contentType}" /></td>
					<%-- LocalDateTime 문자열의 'T' 를 공백으로 치환해 간단히 표시한다 --%>
					<td><c:out value="${fn:replace(f.regDate, 'T', ' ')}" /></td>
					<td>
						<%-- 다운로드 링크 버튼 --%> <a class="btn"
						href="${pageContext.request.contextPath}/file/download/${f.id}">다운로드</a>
					</td>
					<td>
						<%-- 논리 삭제 폼 버튼 --%>
						<form action="${pageContext.request.contextPath}/file/delete/soft" method="post"
							style="display: inline;">
							<input type="hidden" name="id" value="${f.id}" />
							<button class="btn" type="submit">삭제</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
