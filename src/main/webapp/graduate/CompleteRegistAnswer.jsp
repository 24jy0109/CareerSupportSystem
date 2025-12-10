<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Answer"%>
<%@ page import="java.util.List"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%
List<Answer> answers = (List<Answer>) request.getAttribute("answers");
Answer answer = null;
if (answers != null && !answers.isEmpty()) {
	answer = answers.get(0); // 登録された Answer を取得
}

DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Submission Complete</title>
<style>
body {
	font-family: Arial;
	margin: 40px;
}

.box {
	border: 1px solid #ccc;
	padding: 20px;
	width: 400px;
	border-radius: 8px;
	background: #f8f8f8;
}

.label {
	font-weight: bold;
}
</style>
</head>
<body>

	<h1>Submission Complete</h1>

	<div class="box">
		<%
		if (answer != null) {
		%>
		<p>
			<span class="label">Answer ID:</span>
			<%=answer.getAnswerId()%></p>

		<p>
			<span class="label">Graduate:</span>
			<%=answer.getGraduate() != null ? answer.getGraduate().getGraduateName() : "未設定"%>
		</p>
		<p>
			<span class="label">Student Number:</span>
			<%=answer.getGraduate() != null ? answer.getGraduate().getGraduateStudentNumber() : "未設定"%>
		</p>

		<p>
			<span class="label">Availability:</span>
			<%=answer.getEventAvailability() != null ? answer.getEventAvailability() : "未設定"%>
		</p>

		<p>
			<span class="label">First Choice:</span>
			<%=answer.getFirstChoiceStartTime() != null ? dtf.format(answer.getFirstChoiceStartTime()) : ""%>
			～
			<%=answer.getFirstChoiceEndTime() != null ? dtf.format(answer.getFirstChoiceEndTime()) : ""%>
		</p>

		<p>
			<span class="label">Second Choice:</span>
			<%=answer.getSecondChoiceStartTime() != null ? dtf.format(answer.getSecondChoiceStartTime()) : ""%>
			～
			<%=answer.getSecondChoiceEndTime() != null ? dtf.format(answer.getSecondChoiceEndTime()) : ""%>
		</p>

		<p>
			<span class="label">Third Choice:</span>
			<%=answer.getThirdChoiceStartTime() != null ? dtf.format(answer.getThirdChoiceStartTime()) : ""%>
			～
			<%=answer.getThirdChoiceEndTime() != null ? dtf.format(answer.getThirdChoiceEndTime()) : ""%>
		</p>
		<%
		} else {
		%>
		<p>No answer information was found.</p>
		<%
		}
		%>
	</div>

	<br>

</body>
</html>
