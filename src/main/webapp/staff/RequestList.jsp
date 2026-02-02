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
<title>申請者一覧</title>
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

	<main class="request-width">



		<div class="wrapper">

			<div class="page-title-nobottom">申請者一覧</div>
			<div class="content">
				<div class="request-top">
					<div class="eventlist">
						<div class="requestlist-title">企業情報</div>
					</div>

					<c:choose>
						<c:when test="${empty requests}">
							<c:set var="company" value="${showCompany.company}" />
							<div class="company">
								<div class="field-name">企業名</div>
								<div class="company-name">${company.companyName}</div>
							</div>
						</c:when>
						<c:otherwise>
							<c:set var="company" value="${requests[0].company}" />
							<div class="company">
								<div class="field-name">企業名</div>
								<div class="company-name">${company.companyName}</div>
							</div>
						</c:otherwise>
					</c:choose>

					<div class="event">
						<div class="event-info">
							<div class="event-frex">
								<div class="field-name">開催有無</div>
								<table class="detail-table">
									<c:choose>
										<c:when test="${empty company.events}">
											<tr class="noline">
												<div class="errormsg">なし</div>
											</tr>
										</c:when>
										<c:otherwise>
											<tr class="noline">
												<div>あり</div>
											</tr>
										</c:otherwise>
									</c:choose>
								</table>
							</div>
						</div>
					</div>

					<div class="eventlist">
						<div class="requestlist-title2">申請者一覧</div>
					</div>

					<c:if test="${empty requests}">
						<div class="errormsg">リクエストした学生はいません。</div>
					</c:if>

					<c:if test="${not empty requests}">

						<!-- 並び替え（サーバーサイド用） -->
						<form method="get" action="appointment_request">
							<input type="hidden" name="command" value="RequestList">
							<input type="hidden" name="companyId" value="${company.companyId}">

							<div class="sort-area">
								<label class="field-name">並び替え：</label>
								<select name="sort" onchange="this.form.submit()">
									<option value="date_asc"
										<c:if test="${sort == 'date_asc'}">selected</c:if>>
										日時（古い順）
									</option>
									<option value="date_desc"
										<c:if test="${sort == 'date_desc'}">selected</c:if>>
										日時（新しい順）
									</option>
									<option value="course_date_asc"
										<c:if test="${sort == 'course_date_asc'}">selected</c:if>>
										学科ごと（日時古い順）
									</option>
									<option value="course_date_desc"
										<c:if test="${sort == 'course_date_desc'}">selected</c:if>>
										学科ごと（日時新しい順）
									</option>
								</select>
							</div>
						</form>

						<table class="request-table">
							<thead>
								<tr class="request-info">
									<th class="request-h"></th>
									<th class="request-h">氏名</th>
									<th class="request-h">学科</th>
									<th class="request-h">学籍番号</th>
									<th class="request-h">リクエスト日時</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="req" items="${requests}" varStatus="st">
									<tr class="request-info">
										<td class="row-no">${st.index + 1}</td>
										<td>${req.student.studentName}</td>
										<td>${req.student.course.courseName}</td>
										<td>${req.student.studentNumber}</td>
										<td>
											${req.requestTime.year}/${req.requestTime.monthValue}/${req.requestTime.dayOfMonth}
											${req.requestTime.hour < 10 ? '0' : ''}${req.requestTime.hour}:
											${req.requestTime.minute < 10 ? '0' : ''}${req.requestTime.minute}
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>

					<div class="bottom-btn-split">
						<button type="button"
							onclick="location.href='company?command=CompanyList'">
							企業一覧に戻る
						</button>

						<c:if test="${not empty company.events}">
							<button type="button"
								onclick="location.href='event?command=EventList&companyName=${company.companyName}'">
								開催一覧へ
							</button>
						</c:if>
					</div>

				</div>
			</div>
		</div>
	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>
