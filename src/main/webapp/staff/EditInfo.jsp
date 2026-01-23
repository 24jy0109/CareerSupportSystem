<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/companylist.css">
<title>情報編集画面</title>
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
		<div class="wrapper">
			<main class="content">
				<div class="request-top">
					<div class="company">
						<div class="field-name">企業名</div>
						<div class="company-name">
							<!--							<p>${companyDTO}</p>-->
							<!--							<p>${companyDTO.company}</p>-->
							<p>${companyDTO.company.companyName}</p>
						</div>

						<div>
							<a
								href="company?command=CompanyRegister&companyId=${companyDTO.company.companyId}">企業名編集</a>
						</div>
					</div>
					<table>
						<tr>
							<th>卒業年次</th>
							<th>名前</th>
							<th>学科</th>
							<th>職種</th>
							<th>メールアドレス</th>
							<th></th>

						</tr>
						<c:forEach var="graduate" items="${companyDTO.company.graduates}">
							<c:set var="admissionYear"
								value="${fn:substring(graduate.graduateStudentNumber, 0, 2)}" />
							<c:set var="graduationYear"
								value="${admissionYear + graduate.course.courseTerm}" />




							<tr>
								<td>${graduationYear}年卒</td>
								<td>${graduate.graduateName}</td>
								<td>${graduate.course.courseName}</td>
								<td>${graduate.graduateJobCategory}</td>
								<td>${graduate.graduateEmail}</td>
								<td>
									<button type="button" class="edit-btn"
										onclick="location.href='graduate?command=RegistEmail&graduateStudentNumber=${graduate.graduateStudentNumber}&updateMode=true'">編集</button>

									<button type="button" class="delete-btn"
										onclick="location.href='graduate?command=deleteGraduate&graduateStudentNumber=${graduate.graduateStudentNumber}&companyId=${companyDTO.company.companyId}'
											; return confirm('本当に削除しますか？');">削除</button>
							</tr>
						</c:forEach>
					</table>



				</div>


				<div class="bottom-btn-left">
					<!-- 戻るボタン　左側 -->
					<button type="button"
						onclick="location.href='company?command=CompanyList'">企業一覧に戻る</button>
				</div>

			</main>
		</div>
	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>