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
<title>参加一覧/履歴</title>

<style>
.pagination {
	text-align: center;
	margin-top: 20px;
}

.pagination button {
	margin: 0 4px;
	padding: 6px 10px;
	border-radius: 4px;
	border: 1px solid #ccc;
	background-color: #fff;
	color: #333;
	cursor: pointer;
}

.pagination button:hover {
	background-color: #f2f2f2;
}

.pagination button.active {
	background-color: #e6f4ea;
	border-color: #009A37;
	color: #006b2e;
	font-weight: 600;
	cursor: default;
}
</style>
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

		<table>
			<c:forEach var="dto" items="${events}">
				<c:if test="${dto.event.eventProgress == 'ONGOING'}">
					<tr class="join-current-row">
						<td>${dto.event.company.companyName}</td>
						<td>
							<a href="event?command=EventDetail&eventId=${dto.event.eventId}">
								開催詳細
							</a>
						</td>
						<td>
							<c:choose>
								<c:when test="${dto.joinAvailability}">参加</c:when>
								<c:otherwise>不参加</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
			</c:forEach>
		</table>

		<div id="pagination-current" class="pagination"></div>

		<!-- ================= 参加履歴 ================= -->
		<div class="eventlist">
			<div class="eventlist-title">参加履歴</div>
		</div>

		<table>
			<c:forEach var="dto" items="${events}">
				<c:if test="${dto.event.eventProgress == 'FINISHED' || dto.event.eventProgress == 'CANCELED'}">
					<tr class="join-history-row">
						<td>${dto.event.company.companyName}</td>
						<td>
							<a href="event?command=EventDetail&eventId=${dto.event.eventId}">
								開催詳細
							</a>
						</td>
						<td>
							<c:choose>
								<c:when test="${dto.event.eventProgress == 'CANCELED'}">中止</c:when>
								<c:when test="${dto.joinAvailability}">参加済</c:when>
								<c:otherwise>不参加</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
			</c:forEach>
		</table>

		<div id="pagination-history" class="pagination"></div>
	</main>

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

	// ★ 初期表示時は「参加一覧」をアクティブにする
	if (currentPager) {
		activePager = currentPager;
	}

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

</body>
</html>