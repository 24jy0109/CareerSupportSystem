<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<!--<link rel="stylesheet" href="./css/appointment.css">-->
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
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
			<tr class="company-r">
				<th class="company-h">企業名</th>
				<th class="company-h"></th>
				<th class="company-h">状況</th>
				<th class="company-h">申請数</th>
				<th class="company-h">開催詳細/登録</th>
			</tr>

			<c:forEach var="companyDTO" items="${companies}">
				<tr class="company-r">
					<td><a 
						href="event?command=RegistEventForm&companyId=${companyDTO.company.companyId}" class="company-link">
							${companyDTO.company.companyName}</a></td>

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
								<p style="padding: 1.25rem 0;">${companyDTO.requestCount}人</p>
							</c:otherwise>
						</c:choose></td>

					<td><a
						href="event?command=RegistEventForm&companyId=${companyDTO.company.companyId}">
							開催情報登録 </a></td>
				</tr>
			</c:forEach>
		</table>
	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>
