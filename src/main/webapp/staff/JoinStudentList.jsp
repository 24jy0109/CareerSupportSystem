<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>参加者一覧（職員）</title>
<style>
table {
	border-collapse: collapse;
	width: 90%;
	margin: 20px auto;
}

th, td {
	border: 1px solid #333;
	padding: 8px;
	text-align: center;
}

th {
	background-color: #eee;
}

h2 {
	text-align: center;
}
</style>
</head>
<body>
	<header>
		<div class="head-part">
			<div id="h-left">
				<img src="img/rogo.png" alt="アイコン">
			</div>

			<div class="header-title"
				onclick="location.href='mypage?command=AppointmentMenu'">
				<div class="title-jp">就活サポート</div>
				<div class="title-en">Career Support</div>
			</div>

			<div class="header-user">ようこそ 24jy0119 さん</div>
		</div>
	</header>

	<main>
		<h2>イベント参加学生一覧</h2>
		<h2>企業名${joinStudents[0].event.company.companyName}</h2>

		<c:choose>
			<%-- joinStudents が空 or null --%>
			<c:when test="${empty joinStudents}">
				<p style="text-align: center;">参加予定の学生はいません。</p>
			</c:when>

			<%-- データあり --%>
			<c:otherwise>
				<table>
					<tr>
						<th>学生番号</th>
						<th>学生名</th>
						<th>学科名</th>
					</tr>

					<c:forEach var="js" items="${joinStudents}">
						<tr>
							<td>${js.student.studentNumber}</td>
							<td>${js.student.studentName}</td>
							<td>${js.student.course.courseName}</td>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>
	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

</body>
</html>