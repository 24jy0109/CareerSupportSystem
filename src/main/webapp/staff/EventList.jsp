<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/pagination.css">
<title>開催一覧/履歴</title>
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
			<form action="event" method="GET">
				<input type="hidden" name="command" value="EventList">
				<div class="search">
					<input type="text" name="companyName" placeholder="企業名で検索"
						value="${param.companyName}" class="search-box">
					<button type="submit" class="search-button">検索</button>
				</div>
			</form>

			<div class="eventlist">
				<div class="eventlist-title">開催一覧</div>
			</div>


			<!-- ================= 開催一覧（開催中） ================= -->
			<table class="eventlist-table">
				<tr class="eventlist-tr">
					<th>開催日時</th>
					<th>企業名</th>
					<th></th>
					<th>開催状況</th>
					<th>参加人数</th>
				</tr>

				<c:set var="hasCurrent" value="false" />
				<c:forEach var="dto" items="${requestScope.events}">
					<c:if test="${dto.event.eventProgress == 'ONGOING'}">
						<c:set var="hasCurrent" value="true" />
						<tr class="eventlist-tr current-row">
							<td>
								${dto.event.eventStartTime.year}/${dto.event.eventStartTime.monthValue}/${dto.event.eventStartTime.dayOfMonth}
								${dto.event.eventStartTime.hour < 10 ? '0' : ''}${dto.event.eventStartTime.hour}
								:${dto.event.eventStartTime.minute < 10 ? '0' : ''}${dto.event.eventStartTime.minute}
							</td>
							<td>${dto.event.company.companyName}</td>
							<td><a
								href="event?command=EventDetail&eventId=${dto.event.eventId}">
									開催詳細 </a></td>
							<c:choose>
								<c:when test="${dto.event.eventProgress.label == '開催'}">
									<td class="held">${dto.event.eventProgress.label}</td>
								</c:when>
								<c:otherwise>
									<td>${dto.event.eventProgress.label}</td>
								</c:otherwise>
							</c:choose>
							<td>${dto.joinStudentCount}</td>
						</tr>
					</c:if>
				</c:forEach>
			</table>

			<c:if test="${!hasCurrent}">
				<div class="errormsg">開催中のイベントはありません。</div>
			</c:if>


			<div id="pagination-current" class="pagination"></div>

			<!-- ================= 開催履歴 ================= -->
			<div class="eventlist">
				<div class="eventlist-title">開催履歴</div>
			</div>

			<table class="eventlist-table">
				<tr class="eventlist-tr">
					<th>開催日時</th>
					<th>企業名</th>
					<th></th>
					<th>開催状況</th>
					<th>参加人数</th>
				</tr>

				<c:set var="hasHistory" value="false" />


				<c:set var="hasHistory" value="false" />

				<c:forEach var="i" begin="0"
					end="${fn:length(requestScope.events) - 1}">
					<c:set var="dto"
						value="${requestScope.events[fn:length(requestScope.events) - 1 - i]}" />

					<c:if
						test="${dto.event.eventProgress == 'FINISHED'
               || dto.event.eventProgress == 'CANCELED'}">

						<c:set var="hasHistory" value="true" />
						<tr class="eventlist-tr history-row">
							<td>
								${dto.event.eventStartTime.year}/${dto.event.eventStartTime.monthValue}/${dto.event.eventStartTime.dayOfMonth}
								${dto.event.eventStartTime.hour < 10 ? '0' : ''}${dto.event.eventStartTime.hour}
								:${dto.event.eventStartTime.minute < 10 ? '0' : ''}${dto.event.eventStartTime.minute}
							</td>
							<td>${dto.event.company.companyName}</td>
							<td><a
								href="event?command=EventDetail&eventId=${dto.event.eventId}">
									開催詳細 </a></td>
							<td>${dto.event.eventProgress.label}</td>
							<td>${dto.joinStudentCount}</td>
						</tr>
					</c:if>
				</c:forEach>

			</table>

			<c:if test="${!hasHistory}">
				<div class="errormsg">開催履歴はありません。</div>
			</c:if>

			<div id="pagination-history" class="pagination"></div>
		</main>
	</div>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

	<script>
	let activePager = null;

	function setupPagination(rowSelector, paginationId, rowsPerPage = 5) {

		const rows = document.querySelectorAll(rowSelector);
		const pagination = document.getElementById(paginationId);

		if (!rows.length) return null;

		const totalPages = Math.ceil(rows.length / rowsPerPage);
		let currentPage = 1;
		const buttons = [];

		function showPage(page) {
			if (page < 1 || page > totalPages) return;

			currentPage = page;
			const start = (page - 1) * rowsPerPage;
			const end = start + rowsPerPage;

			rows.forEach((row, index) => {
				row.style.display = (index >= start && index < end) ? "" : "none";
			});

			buttons.forEach(btn => btn.classList.remove("active"));
			if (buttons[page - 1]) {
				buttons[page - 1].classList.add("active");
			}
		}

		for (let i = 1; i <= totalPages; i++) {
			const btn = document.createElement("button");
			btn.textContent = i;
			btn.onclick = () => {
				activePager = pager;
				showPage(i);
			};
			buttons.push(btn);
			pagination.appendChild(btn);
		}

		showPage(1);

		const pager = {
			next() { showPage(currentPage + 1); },
			prev() { showPage(currentPage - 1); }
		};

		return pager;
	}

	const currentPager = setupPagination(".current-row", "pagination-current", 5);
	const historyPager = setupPagination(".history-row", "pagination-history", 5);

	// 初期状態は「開催一覧」
	activePager = currentPager;

	document.addEventListener("keydown", function(e) {

		const tag = document.activeElement.tagName;
		if (tag === "INPUT" || tag === "TEXTAREA") return;
		if (!activePager) return;

		if (e.key === "ArrowRight") {
			activePager.next();
		}
		if (e.key === "ArrowLeft") {
			activePager.prev();
		}
	});
	</script>
	<jsp:include page="/common/flashMessage.jsp" />
</body>
</html>