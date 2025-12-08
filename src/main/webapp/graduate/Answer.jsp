<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>イベント回答フォーム</title>
</head>
<body>

	<%
// URL: answer?command=AnswerForm&answerId=1
String answerId = request.getParameter("answerId");
%>

	<h2>参加可否・希望日時フォーム</h2>

	<form action="Answer" method="post">

		<!-- Hidden: answerId -->
		<input type="hidden" name="answerId" value="<%=answerId%>">
		
		<input type="hidden" name="command" value="registAnswer">

		<!-- 参加可否 -->
		<label>参加可否 (event_availability)</label><br> <input type="radio"
			name="eventAvailability" value="true"> 出席する<br> <input
			type="radio" name="eventAvailability" value="false"> 出席しない<br>
		<input type="radio" name="eventAvailability" value="null" checked>
		未回答<br>

		<!-- 第一希望 -->
		<label>第一希望日時（first_choice）</label> <input type="datetime-local"
			name="firstChoice">

		<!-- 第二希望 -->
		<label>第二希望日時（second_choice）</label> <input type="datetime-local"
			name="secondChoice">

		<!-- 第三希望 -->
		<label>第三希望日時（third_choice）</label> <input type="datetime-local"
			name="thirdChoice"> <br> <input type="submit"
			value="回答する">

	</form>

</body>
</html>