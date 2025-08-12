<%-- JSP 페이지 설정 지시어 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- JSTL(Jakarta Standard Tag Library) 사용을 위한 태그 라이브러리 선언 --%>
<%-- 'c' 접두사로 Core 라이브러리(변수, 제어문 등)를 사용합니다. --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%-- 'fn' 접두사로 Functions 라이브러리(문자열 처리, 컬렉션 길이 계산 등)를 사용합니다. --%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%-- 공통 CSS 파일 및 라이브러리를 모아놓은 head_css.jsp 파일을 포함시킵니다. --%>
<%@ include file="/WEB-INF/views/include/head_css.jsp"%>
<%-- Summernote 에디터의 CSS 파일을 CDN을 통해 가져옵니다. --%>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.css" rel="stylesheet">

</head>
<body id="page-top">
	<%-- 전체 페이지의 최상위 wrapper div --%>
	<div id="wrapper">
		<%-- 사이드바 메뉴를 포함시킵니다. c:import는 외부 리소스를 페이지에 포함시키는 JSTL 태그입니다. --%>
		<c:import url="/WEB-INF/views/include/sidebar.jsp"></c:import>

		<div id="content-wrapper" class="d-flex flex-column">
			<div id="content">
				<%-- 상단바(Topbar)를 포함시킵니다. --%>
				<c:import url="/WEB-INF/views/include/topbar.jsp"></c:import>
				<div class="container-fluid">
					<div class="row justify-content-center ">
						<div class="col-md-8">
							<form  method="post" enctype="multipart/form-data">
								<input type="hidden" name="boardNum" value="${vo.boardNum}">
								
								<div class="mb-3">
									<label for="writer" class="form-label">Writer</label> 
									<input type="text" class="form-control" name="boardWriter"
										id="writer" value="${vo.boardWriter}">
								</div>
								
								<div class="mb-3">
									<label for="title" class="form-label">Title</label> 
									<input type="text" class="form-control" name="boardTitle"
										id="title" value="${vo.boardTitle}">
								</div>
								
								<div class="mb-3">
								  <label for="contents" class="form-label">Contents</label>
								  <textarea class="form-control" id="contents" rows="9" name="boardContents">${vo.boardContents}</textarea>
								</div>
								
								<div>
									<button class="btn btn-primary" type="button" id="add">ADD</button>
								</div>
								
								<div>
									<%--
										JSTL forEach를 사용해 컨트롤러에서 받은 파일 목록(vo.boardFileVOs)을 반복 처리합니다.
										각 파일에 대해 삭제 버튼을 생성합니다.
									--%>
									<c:forEach items="${vo.boardFileVOs}" var="f">
										<%--
											파일 삭제 버튼입니다.
											- data-file-num: 삭제할 파일의 고유 번호(fileNum)를 저장하는 data 속성입니다. JS에서 이 값을 사용해 어떤 파일을 삭제할지 서버에 알립니다.
											- ${f.oriName}: 파일의 원본 이름을 버튼 텍스트로 표시합니다.
										--%>
										<button class="deleteFile" data-file-num="${f.fileNum}" type="button">${f.oriName}</button>
									</c:forEach>
								</div>
								
								<div id="result" data-file-count="${vo.boardFileVOs.size()}">
								</div>

								<button type="submit" class="btn btn-primary">Submit</button>
							</form>
						</div>
					</div>

				</div>
			</div>
			<%-- 푸터(Footer)를 포함시킵니다. --%>
			<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>
		</div>
	</div>
	
	<%-- 공통 JavaScript 파일 및 라이브러리를 모아놓은 tail.jsp 파일을 포함시킵니다. --%>
	<c:import url="/WEB-INF/views/include/tail.jsp"></c:import>
	
	<%-- 이 페이지의 파일 첨부 관련 로직이 담긴 외부 JavaScript 파일을 불러옵니다. (ex: 파일 필드 추가/삭제) --%>
	<script type="text/javascript" src ="/js/board/board_add.js"></script>
	
	<%-- Summernote 에디터의 JavaScript 파일을 CDN을 통해 가져옵니다. --%>
	<script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.js"></script>
	
	<%-- Summernote 에디터를 초기화하고 관련 이벤트를 처리하는 스크립트 --%>
	<script type="text/javascript">
	
	// id가 'contents'인 textarea에 Summernote 에디터를 적용합니다.
	$("#contents").summernote({
		// Summernote 에디터의 이벤트 콜백 함수들을 정의하는 부분입니다.
		callbacks:{
			// 에디터에 이미지를 업로드할 때 실행되는 콜백 함수입니다.
			onImageUpload: function (files) {
				// 업로드된 파일 정보가 files 배열로 전달됩니다.
				
				// 서버로 파일을 보내기 위해 FormData 객체를 생성합니다.
				let f = new FormData();
				// 'bf'라는 이름(파라미터명)으로 첫 번째 파일(files[0])을 FormData에 추가합니다.
				// 이 이름은 서버(Controller)에서 @RequestParam("bf") 등으로 받게 됩니다.
				f.append("bf", files[0])
				
				// fetch API를 사용해 서버의 './boardFile' 주소로 비동기 POST 요청을 보냅니다.
				fetch("./boardFile",{
					method:"POST",
					body:f // body에 파일이 담긴 FormData 객체를 전달합니다.
				})
				.then(r=>r.text()) // 서버로부터의 응답을 텍스트 형식으로 변환합니다. (서버는 저장된 이미지의 URL 경로를 반환해야 함)
				.then(r=>{
					// 성공적으로 응답을 받으면, 반환된 이미지 경로(r)를 에디터에 삽입합니다.
					$("#contents").summernote('editor.insertImage', r);
				})
				.catch(e => console.log(e)) // 오류 발생 시 콘솔에 로그를 출력합니다.
				
			},
			// 에디터에서 미디어(이미지 등)를 삭제할 때 실행되는 콜백 함수입니다.
			onMediaDelete: function(files){
				// 삭제된 미디어의 DOM 요소가 files 배열로 전달됩니다.
				
				// 삭제된 이미지의 'src' 속성 값을 가져옵니다. 이 값은 서버에 저장된 파일의 경로입니다.
				let f = $(files[0]).attr("src"); 
				
				// 서버로 전송할 파라미터를 만들기 위해 URLSearchParams 객체를 사용합니다.
				let params = new URLSearchParams();
				// 'fileName'이라는 이름으로 파일 경로(f)를 추가합니다.
				params.append("fileName", f);

				// fetch API를 사용해 서버의 './boardFileDelete' 주소로 비동기 POST 요청을 보냅니다.
				fetch("./boardFileDelete", {
					method:"POST",
					body:params // body에 파일 경로가 담긴 파라미터를 전달합니다.
				})
				.then(r=>r.json()) // 서버로부터의 응답을 JSON 형식으로 변환합니다. (예: { "result": 1 })
				.then(r=>{
					// 서버의 응답을 콘솔에 출력합니다.
					console.log(r)
				})
			}
		}
	})
	</script>
</body>
</html>