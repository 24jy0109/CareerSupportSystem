<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>企業リスト</title>
<style>
body {
	font-family: Arial, sans-serif;
}

table {
	border-collapse: collapse;
	width: 80%;
	margin: 20px auto;
}

th, td {
	border: 1px solid #333;
	padding: 8px;
	text-align: left;
}

th {
	background-color: #f0f0f0;
}

.center {
	text-align: center;
}
</style>
</head>
<body>
	<h2 class="center">企業リスト</h2>

	<table>
		<thead>
			<tr>
				<th>企業ID</th>
				<th>企業名</th>
				<th>イベント状況</th>
				<th>リクエスト状況</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="company" items="${companies}">
				<tr>
					<td>${company.company.companyId}</td>
					<td>${company.company.companyName}</td>
					<td>${company.eventProgress}</td>
					<td>${company.isRequest}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
