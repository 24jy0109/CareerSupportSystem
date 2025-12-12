<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>確認画面</title>
</head>
<body>
	<form action="graduate" method="post">

		<p>会社名：${companyName}</p>
		<input type="hidden" name="companyId" value="${companyId}">

		<p>職種：${jobType}</p>
		<input type="hidden" name="jobType" value="${jobType}">

		<p>氏名：${graduateName}</p>
		<input type="hidden" name="graduateName" value="${graduateName}">

		<p>学科：${courseName}</p>
		<input type="hidden" name="courseCode" value="${courseCode}">

		<p>学籍番号：${graduateStudentNumber}</p>
		<input type="hidden" name="graduateStudentNumber"
			value="${graduateStudentNumber}">

		<p>メールアドレス：${graduateEmail}</p>
		<input type="hidden" name="graduateEmail" value="${graduateEmail}">

		<p>その他：${otherInfo}</p>
		<input type="hidden" name="otherInfo" value="${otherInfo}">

		<button type="submit" name="command" value="RegistEmail">編集する</button>
		<button type="submit" name="command" value="RegistEmailConfirm">登録する</button>
	</form>
</body>
</html>