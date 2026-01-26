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
	<main>
		<div class="wrapper">
			<div class="page-title">申請者一覧</div>
			<main class="content">
				<div class="request-top">
					<c:choose>
						<c:when test="${empty requests}">
							<c:set var="company" value="${showCompany.company}" />
							<div class="company">
								<div class="field-name">企業名</div>
								<div class="company-name">${showCompany.company.companyName}</div>
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
									<!--									<c:forEach var="ev" items="${company.events}">-->
									<!--										<tr class="noline">-->

									<!--											<td>${ev.eventStartTime.year}/${ev.eventStartTime.monthValue}/${ev.eventStartTime.dayOfMonth}</td>-->
									<!--											<td>${ev.eventStartTime.hour < 10 ? '0' : ''}${ev.eventStartTime.hour}:${ev.eventStartTime.minute < 10 ? '0' : ''}${ev.eventStartTime.minute}</td>-->
									<!--											<td>～</td>-->
									<!--											<td>${ev.eventEndTime.year}/${ev.eventEndTime.monthValue}/${ev.eventEndTime.dayOfMonth}</td>-->
									<!--											<td>${ev.eventEndTime.hour < 10 ? '0' : ''}${ev.eventEndTime.hour}:${ev.eventEndTime.minute < 10 ? '0' : ''}${ev.eventEndTime.minute}</td>-->
									<!--											<td>${ev.eventPlace}</td>-->
									<!--										</tr>-->
									<!--									</c:forEach>-->
								</table>
							</div>
						</div>


					</div>
					<c:if test="${empty requests}">
						<div class="errormsg">リクエストした学生はいません。</div>
					</c:if>
					<c:if test="${not empty requests}">

						<div class="sort-area">
							<label class="field-name">並び替え：</label> <select id="sortSelect"
								onchange="sortRequests()">
								<option value="course_date_asc">学科ごと（日時古い順）</option>
								<option value="course_date_desc">学科ごと（日時新しい順）</option>
								<option value="date_asc">日時（古い順）</option>
								<option value="date_desc">日時（新しい順）</option>
							</select>
						</div>

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
								<c:forEach var="req" items="${requests}">
									<tr class="request-info">
										<td class="row-no"></td>
										<td>${req.student.studentName}</td>
										<td>${req.student.course.courseName}</td>
										<td>${req.student.studentNumber}</td>
										<td>${req.requestTime.year}/${req.requestTime.monthValue}/${req.requestTime.dayOfMonth}
											${req.requestTime.hour < 10 ? '0' : ''}${req.requestTime.hour}:${req.requestTime.minute < 10 ? '0' : ''}${req.requestTime.minute}
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>

					<div class="bottom-btn-split">
						<!-- 戻るボタン　左側 -->
						<button type="button"
							onclick="location.href='company?command=CompanyList'">企業一覧に戻る</button>

						<c:if test="${not empty company.events}">

							<button type="button"
								onclick="location.href='event?command=EventList&companyName=${company.companyName}'">
								開催一覧へ</button>
						</c:if>
					</div>
			</main>
		</div>

		<footer>
			<p>
				<small>&copy; 2024 Example Inc.</small>
			</p>
		</footer>
</body>

<script>
	document.addEventListener("DOMContentLoaded", () => {
	  	renumberRows();
	});
	
	function sortRequests() {
		const table = document.querySelector(".request-table");
		if (!table) return;
		
		const tbody = table.querySelector("tbody");
		const rows = Array.from(tbody.querySelectorAll("tr"));
	
		const mode = document.getElementById("sortSelect").value;
	
		rows.sort((a, b) => {
			const courseA = a.children[2].innerText.trim();
			const courseB = b.children[2].innerText.trim();
	
			const dateA = parseDate(a.children[4].innerText);
			const dateB = parseDate(b.children[4].innerText);
	
			switch (mode) {
			case "course_date_asc":
				if (courseA !== courseB) {
					return courseA.localeCompare(courseB);
				}
				return dateA - dateB;
	
			case "course_date_desc":
				if (courseA !== courseB) {
					return courseA.localeCompare(courseB);
				}
				return dateB - dateA;
	
			case "date_asc":
				return dateA - dateB;
	
			case "date_desc":
				return dateB - dateA;
			}
		});
	
		rows.forEach(row => tbody.appendChild(row));
		//並び替え後に番号振り直し
		renumberRows();
	}
	
	function parseDate(text) {
		// "2026/1/23 09:05" → Date
		const normalized = text
			.replace(/\//g, "-")
			.replace(" ", "T");
		return new Date(normalized);
	}

	function renumberRows() {
		  const rows = document.querySelectorAll(".request-table tbody tr");

		  rows.forEach((row, index) => {
		    const noCell = row.querySelector(".row-no");
		    if (noCell) {
		      noCell.textContent = index + 1;
		    }
		  });
		}

</script>
</html>
