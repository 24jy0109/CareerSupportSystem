<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*, model.*"%>

<html>
<head>
<title>企業一覧</title>
<style>
table {
	border-collapse: collapse;
	width: 100%;
}

th, td {
	border: 1px solid #ccc;
	padding: 8px;
	text-align: center;
}

th {
	background-color: #f2f2f2;
}

a {
	text-decoration: none;
	color: blue;
}
</style>
</head>
<body>
	<h2>企業一覧</h2>

	<%
	ArrayList<Company> companies = (ArrayList<Company>) request.getAttribute("companies");
	if (companies == null || companies.isEmpty()) {
	%>
	<p>企業情報はありません。</p>
	<%
	} else {
	%>
	<table>
		<thead>
			<tr>
				<th>企業名</th>
				<th>開催有無</th>
				<th>申請状況</th>
				<th>卒業生人数</th>
				<th>詳細</th>
			</tr>
		</thead>
		<tbody>
			<%
			for (Company c : companies) {
				// イベントが1つでもあれば「あり」、なければ「なし」
				String eventStatus = (c.getEvents() != null && !c.getEvents().isEmpty()) ? "あり" : "なし";

				// 申請状況（requestStudentsが1人でもいれば「申請済み」）
				String requestStatus = (c.getRequestStudents() != null && !c.getRequestStudents().isEmpty()) ? "申請済み" : "未申請";

				// 卒業生人数
				int graduateCount = (c.getGraduates() != null) ? c.getGraduates().size() : 0;
			%>
			<tr>
				<td><%=c.getCompanyName()%></td>
				<td><%=eventStatus%></td>
				<td><%=requestStatus%></td>
				<td><%=graduateCount%></td>
				<td><a
					href="companyDetail.jsp?companyId=<%=c.getCompanyId()%>">詳細</a></td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>
	<%
	}
	%>
</body>
</html>
