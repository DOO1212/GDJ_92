<%-- JSP 페이지의 언어, 콘텐츠 타입, 인코딩을 설정하는 지시어입니다. --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%-- 
		이 스크립트는 페이지가 로드되자마자 실행됩니다.
		주로 Spring Controller와 같은 서버 측 코드에서 작업 완료 후,
		사용자에게 메시지를 전달하고 다른 페이지로 이동시킬 때 사용됩니다.
	--%>
	<script type="text/javascript">
		// Controller에서 Model이나 RedirectAttributes에 담아 전달한 'msg' 값을 JavaScript 알림창으로 띄웁니다.
		// 예를 들어, Controller에서 model.addAttribute("msg", "글이 등록되었습니다."); 와 같이 설정합니다.
		alert('${msg}');
		
		// Controller에서 전달한 'url' 값으로 현재 페이지를 이동시킵니다.
		// 예를 들어, Controller에서 model.addAttribute("url", "./list"); 와 같이 설정하면 목록 페이지로 이동합니다.
		location.href="${url}";
	</script>
</body>
</html>