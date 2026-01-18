<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>連絡先登録確認画面（在校生/職員）</title>
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
	<main>

		<form action="graduate" method="post">
			<c:set var="isStudent" value="${sessionScope.role == 'student'}" />
			<input type="hidden" name="updateMode" value="${updateMode}">

			<div class="split-screen">
				<div class="left-screen">
					<span class="leftscreen-title">企業選択</span>
					<div class="flame">
						<table class="registscreen-table">
							<tr>
								<td>
									<div>企業</div>
								</td>
								<td>${companyName}<input type="hidden" name="companyId"
									value="${companyId}"> <input type="hidden"
									name="companyName" value="${companyName}">
								</td>
							</tr>
							<tr>
								<td>
									<div>職種</div>
								</td>
								<td>${jobType}<input type="hidden" name="jobType"
									value="${jobType}"></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="right-screen">
					<span class="rightscreen-title">学生情報</span>
					<div class="flame">
						<table class="registscreen-table">
							<tr>
								<td><div>氏名</div></td>
								<td><c:choose>

										<c:when test="${isStudent}">
							        ${name}
							        <input type="hidden" name="graduateName" value="${name}">
										</c:when>


										<c:otherwise>
							        ${graduateName}
							        <input type="hidden" name="graduateName"
												value="${graduateName}">
										</c:otherwise>
									</c:choose></td>
							</tr>


							<tr>
								<td>
									<div>学科</div>
								</td>
								<td>${courseName}<input type="hidden" name="courseCode"
									value="${courseCode}"> <input type="hidden"
									name="courseName" value="${courseName}"></td>
							</tr>
							<tr>
								<td>
									<div>学籍番号</div>
								</td>
								<td>${graduateStudentNumber}<input type="hidden"
									name="graduateStudentNumber" value="${graduateStudentNumber}"></td>
							</tr>
							<tr>
								<td>
									<div>メールアドレス</div>
								</td>
								<td>${graduateEmail}<input type="hidden"
									name="graduateEmail" value="${graduateEmail}"></td>
							</tr>
							<tr>
								<td>
									<div>備考（任意）</div>
								</td>
								<td>${otherInfo}<input type="hidden" name="otherInfo"
									value="${otherInfo}">
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div class="bottom-btn-right">
				<div class="edit-regist-btn">
					<div>
						<input type="hidden" name="fromConfirm" value="true"> <input
							type="hidden" name="updateMode" value="${updateMode}">

						<button type="submit" name="command" value="RegistEmail">編集</button>

						<button type="submit" name="command" value="RegistEmailConfirm">登録</button>
					</div>
				</div>
		</form>

	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

</body>
</html>