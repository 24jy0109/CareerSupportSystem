<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/pagination.css">
<title>企業一覧申請状況付き（職員）</title>

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
		<form action="company" method="GET">
			<input type="hidden" name="command" value="CompanyList">
			<div class="search">
				<input type="text" name="companyName" placeholder="企業名で検索"
					value="${param.companyName}" class="search-box">
				<button type="submit" class="search-button">検索</button>
			</div>
		</form>

		<table class="company-table">
			<tr class="company-r company-header">
				<th class="company-h">企業名</th>
				<th class="company-h"></th>
				<th class="company-h">状況</th>
				<th class="company-h">申請数</th>
				<th class="company-h">開催詳細/登録</th>
			</tr>

			<c:choose>
				<c:when test="${empty companies}">
					<tr>
						<td colspan="5" style="text-align: center; padding: 2rem;">
							該当する企業がありません</td>
					</tr>
				</c:when>

				<c:otherwise>
					<c:forEach var="companyDTO" items="${companies}">
						<tr class="company-r company-row">
							<td><a
								href="event?command=RegistEventForm&companyId=${companyDTO.company.companyId}"
								class="company-link"> ${companyDTO.company.companyName} </a></td>

							<td><a
								href="./graduate?companyId=${companyDTO.company.companyId}&command=editInfo">
									情報編集 </a></td>

							<c:choose>
								<c:when test="${companyDTO.eventProgress == '開催'}">
									<td class="held">${companyDTO.eventProgress}</td>
								</c:when>
								<c:otherwise>
									<td>${companyDTO.eventProgress}</td>
								</c:otherwise>
							</c:choose>

							<td><c:choose>
									<c:when test="${companyDTO.requestCount > 0}">
										<a
											href="appointment_request?command=RequestList&companyId=${companyDTO.company.companyId}">
											申請者一覧 </a>
										<p>${companyDTO.requestCount}人</p>
									</c:when>
									<c:otherwise>
										<p style="padding: 1.25rem 0;">
											${companyDTO.requestCount}人</p>
									</c:otherwise>
								</c:choose></td>

							<td><a
								href="event?command=RegistEventForm&companyId=${companyDTO.company.companyId}">
									開催情報登録 </a></td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>

		</table>

		<div id="pagination-company" class="pagination"></div>
	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

	<script>
	let activePager = null;

	function setupPagination(rowSelector, paginationId, rowsPerPage = 10) {

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

	const companyPager = setupPagination(".company-row", "pagination-company", 10);
	activePager = companyPager;

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