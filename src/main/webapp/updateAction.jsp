<%@page import="java.io.PrintWriter"%>
<%@page import="board.boardDAO"%>
<%@page import="board.boardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("utf-8"); %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%	
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
		// 로그인을 한 사람만 글을 쓸 수 있도록
		if(userID == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href='login.jsp'");
			script.println("</script>");
		} 
		
		int postID = 0;
		if(request.getParameter("postID") != null){
			postID = Integer.parseInt(request.getParameter("postID"));
		}
		if(postID == 0){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href='board.jsp'");
			script.println("</script>");
		}
		// 해당 postID에 대한 게시글을 가져온 다음 다음 세션을 통하여 작성자 본인이 맞는지 체크한다.
		boardDTO dto = new boardDAO().getBoard(postID);
		if(!userID.equals(dto.getUserID())){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('권한이 없습니다.')");
			script.println("location.href='board.jsp'");
			script.println("</script>");
		} else{
			//입력이 안 됐거나 빈 값이 있는지 체킇나다.
			if(request.getParameter("postTitle") == null || request.getParameter("postContent") == null 
					|| request.getParameter("postTitle").equals("") || request.getParameter("postContent").equals("")){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('입력이 안된 사항이 있습니다.')");
				script.println("history.back()");
				script.println("</script>");
				
			}else{
				boardDAO dao = new boardDAO();
				int result = dao.update(postID, request.getParameter("postTitle"), request.getParameter("postContent"));
				if(result == -1){
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글 수정하기에 실패 했습니다.')");
					script.println("history.back()");
					script.println("</script>");
				}else{
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글 수정하기에 성공 했습니다.')");
					script.println("location.href='board.jsp'");
					script.println("</script>");
				}
			}
		}
		
	%>
</body>
</html>