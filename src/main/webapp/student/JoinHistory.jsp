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
<link rel="stylesheet" href="./css/pagination.css">
<title>参加一覧/履歴</title>
</head>

<body>
	<header class="head-part">
		<div id="h-left">
			<img src="./img/rogo.png" alt="アイコン">
		</div>

		<div class="header-title"
			onclick="location.href='mypage?command=AppointmentMenu'">
			<div class="title-jp">就活サポート</div>
			<div class="title-en">Career Support</div>
		</div>

		<div class="header-user">ようこそ${name}さん</div>
	</header>

	<main>
		<!-- ================= 参加一覧 ================= -->
		<div class="eventlist">
			<div class="eventlist-title">参加一覧</div>
		</div>

		<c:set var="hasCurrent" value="false" />

		<table>
			<c:forEach var="dto" items="${events}">
				<c:if test="${dto.event.eventProgress == 'ONGOING'}">
					<c:set var="hasCurrent" value="true" />
					<tr class="join-current-row">
						<td>${dto.event.company.companyName}</td>
						<td><a
							href="event?command=EventDetail&eventId=${dto.event.eventId}">
								開催詳細 </a></td>
						<td><c:choose>
								<c:when test="${dto.joinAvailability}">参加</c:when>
								<c:otherwise>不参加</c:otherwise>
							</c:choose></td>
					</tr>
				</c:if>
			</c:forEach>
		</table>

		<c:if test="${!hasCurrent}">
			<p class="errormsg">現在参加予定のイベントはありません。</p>
		</c:if>

		<div id="pagination-current" class="pagination"></div>

		<!-- ================= 参加履歴 ================= -->
		<div class="eventlist">
			<div class="eventlist-title">参加履歴</div>
		</div>

		<c:set var="hasHistory" value="false" />

		<table>
			<c:forEach var="dto" items="${events}">
				<c:if
					test="${dto.event.eventProgress == 'FINISHED'
						|| dto.event.eventProgress == 'CANCELED'}">
					<c:set var="hasHistory" value="true" />
					<tr class="join-history-row">
						<td>${dto.event.company.companyName}</td>
						<td><a
							href="event?command=EventDetail&eventId=${dto.event.eventId}">
								開催詳細 </a></td>
						<td><c:choose>
								<c:when test="${dto.event.eventProgress == 'CANCELED'}">中止</c:when>
								<c:when test="${dto.joinAvailability}">参加済</c:when>
								<c:otherwise>不参加</c:otherwise>
							</c:choose></td>
					</tr>
				</c:if>
			</c:forEach>
		</table>

		<c:if test="${!hasHistory}">
			<p class="errormsg">参加履歴はありません。</p>
		</c:if>

		<div id="pagination-history" class="pagination"></div>
	</main>

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

	const currentPager = setupPagination(".join-current-row", "pagination-current", 5);
	const historyPager = setupPagination(".join-history-row", "pagination-history", 5);

	if (currentPager) {
		activePager = currentPager;
	}

	document.addEventListener("keydown", function(e) {

		const tag = document.activeElement.tagName;
		if (tag === "INPUT" || tag === "TEXTAREA") return;
		if (!activePager) return;

		if (e.key === "ArrowRight") activePager.next();
		if (e.key === "ArrowLeft") activePager.prev();
	});
	</script>

	<jsp:include page="/common/flashMessage.jsp" />
</body>
</html>
