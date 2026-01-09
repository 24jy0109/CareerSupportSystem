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
<title>開催一覧</title>

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

/* 現在ページ */
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
		<div>開催詳細から参加登録をしてください。</div>

		<c:choose>
			<c:when test="${empty events}">
				<p style="text-align: center;">現在参加可能なイベントはありません。</p>
			</c:when>

			<c:otherwise>
				<table class="company-table">
					<tr class="company-r">
						<th class="company-h">日程</th>
						<th class="company-h">企業名</th>
						<th class="company-h">残り定員</th>
						<th class="company-h">詳細</th>
						<th class="company-h">参加状況</th>
					</tr>

					<c:forEach var="dto" items="${events}">
						<tr class="company-r event-row">
							<td>${dto.event.eventStartTime.year}
								/${dto.event.eventStartTime.monthValue}
								/${dto.event.eventStartTime.dayOfMonth}</td>

							<td>${dto.event.company.companyName}</td>

							<td>${dto.event.eventCapacity - dto.joinStudentCount}</td>

							<td><a
								href="event?command=EventDetail&eventId=${dto.event.eventId}">
									開催詳細 </a></td>

							<td><c:choose>
									<c:when test="${dto.joinAvailability == true}">
										参加
									</c:when>
									<c:otherwise>
										不参加
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>

		<!-- ページネーション -->
		<div id="pagination" class="pagination"></div>
	</main>

	<script>
	const rowsPerPage = 10;
	const rows = document.querySelectorAll(".event-row");
	const pagination = document.getElementById("pagination");
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
		btn.onclick = () => showPage(i);
		buttons.push(btn);
		pagination.appendChild(btn);
	}

	document.addEventListener("keydown", function(e) {
		const tag = document.activeElement.tagName;
		if (tag === "INPUT" || tag === "TEXTAREA") return;

		if (e.key === "ArrowRight") {
			showPage(currentPage + 1);
		}
		if (e.key === "ArrowLeft") {
			showPage(currentPage - 1);
		}
	});

	if (rows.length > 0) {
		showPage(1);
	}
</script>

</body>
</html>
