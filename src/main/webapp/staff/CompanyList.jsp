<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>企業一覧</title>
</head>
<body>
<h2>企業一覧</h2>

<!-- ▼検索フォーム -->
<form action="company" method="GET">
	<input type="hidden" name="command" value="CompanyList">
    <input type="text" name="companyName" placeholder="企業名で検索" 
           value="${param.companyName}">
    <button type="submit">検索</button>
</form>

<table border="1">
    <thead>
        <tr>
            <th>企業名</th>
            <th>進行状況</th>
            <th>申請数</th>
            <th>情報編集</th>
            <th>開催ページ</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach var="companyDTO" items="${companies}">
        <tr>
            <td>${companyDTO.company.companyName}</td>
            <td>${companyDTO.eventProgress}</td>
            <td><a href="company?command=RequestList&companyId=${company.company.companyId}">申請者リスト</a>	${companyDTO.requestCount}人</td>
            <td>
                <!-- 仮リンク -->
                <a href="editCompany.jsp?companyId=${companyDTO.company.companyId}">編集</a>
            </td>
            <td>
                <!-- 仮リンク -->
                <a href="eventPage.jsp?companyId=${companyDTO.company.companyId}">開催ページ</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
