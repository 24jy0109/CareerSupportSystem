<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/pagination.css">
<title>企業一覧</title>
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
		<!-- ▼検索フォーム -->
		<form action="company" method="GET">
			<input type="hidden" name="command" value="CompanyList">
			<div class="search">
				<input type="text" name="companyName" placeholder="企業名で検索"
					value="${param.companyName}" class="search-box">
				<button type="submit" class="search-button">検索</button>
			</div>
		</form>

		<table class="company-table">
			<tr class="company-r">
				<th class="company-h">企業名</th>
				<th class="company-h">開催有無</th>
				<th class="company-h">申請状況</th>
				<th class="company-h">卒業生人数</th>
				<th class="company-h"></th>
			</tr>

			<c:choose>
				<c:when test="${empty companies}">
					<tr>
						<td colspan="6" class="center">該当する企業はありません</td>
					</tr>
				</c:when>

				<c:otherwise>
					<c:forEach var="company" items="${companies}">
						<tr class="company-r data-row">
							<td ><a 
								href="company?command=CompanyDetail&companyId=${company.company.companyId}" class="company-link">${company.company.companyName}<a></a></td>

							<c:choose>
								<c:when test="${company.eventProgress == '開催'}">
									<td class="held">${company.eventProgress}</td>
								</c:when>
								<c:otherwise>
									<td>${company.eventProgress}</td>
								</c:otherwise>
							</c:choose>

							<c:choose>
								<c:when test="${company.isRequest == '申請済み'}">
									<td class="red-msg">申請済</td>
								</c:when>
								<c:otherwise>
									<td>${company.isRequest}</td>
								</c:otherwise>
							</c:choose>

							<td>${company.graduateCount}</td>
							<td><a
								href="company?command=CompanyDetail&companyId=${company.company.companyId}">
									申請/詳細 </a></td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</table>

		<!-- ▼ ページネーション -->
		<div id="pagination" class="pagination"></div>
	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

	<script>
	const rowsPerPage = 10;
	const rows = document.querySelectorAll(".data-row");
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

		// ボタンの active 制御
		buttons.forEach(btn => btn.classList.remove("active"));
		if (buttons[page - 1]) {
			buttons[page - 1].classList.add("active");
		}
	}

	// ページボタン生成
	for (let i = 1; i <= totalPages; i++) {
		const btn = document.createElement("button");
		btn.textContent = i;
		btn.onclick = () => showPage(i);
		buttons.push(btn);
		pagination.appendChild(btn);
	}

	// 矢印キー操作
	document.addEventListener("keydown", function(e) {

		// 入力中は無効
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

<jsp:include page="/common/flashMessage.jsp" />
</body>
</html>
