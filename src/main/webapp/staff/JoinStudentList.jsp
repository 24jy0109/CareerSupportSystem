<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>参加者一覧</title>

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

			<div class="header-user">ようこそ${name}さん</div>
		</div>
	</header>



	<div class="wrapper">
		<main class="content">
			<div class="top-info">
				<div class="company">
					<div class="field-name">企業名</div>
					<div class="company-name">${joinStudents[0].event.company.companyName}</div>
				</div>
			</div>

			<c:choose>
				<%-- joinStudents が空 or null --%>
				<c:when test="${empty joinStudents}">
					<div class="errormsg">参加予定の学生はいません。</div>
				</c:when>

				<%-- データあり --%>
				<c:otherwise>
					<table class="joinlist-table">
						<tr>
							<th class="joinlist-h">名前</th>
							<th class="joinlist-h">学科</th>
							<th class="joinlist-h">学籍番号</th>
						</tr>

						<c:forEach var="js" items="${joinStudents}">
							<tr>
								<td class="joinlist-td">${js.student.studentName}</td>
								<td class="joinlist-td">${js.student.course.courseName}</td>
								<td class="joinlist-td">${js.student.studentNumber}</td>
							</tr>
						</c:forEach>
					</table>
				</c:otherwise>
			</c:choose>
			
			<div class="bottom-btn-left">
				<button type="button" onclick="location.href='event?command=EventDetail&eventId=${joinStudents[0].event.eventId}'">戻る</button>
			</div>
		</main>
	</div>
	
	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

</body>
</html>