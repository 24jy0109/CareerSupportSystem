<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>企業詳細</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h2, h3 { color: #2c3e50; }
        table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        .section { margin-bottom: 30px; }
        .label { font-weight: bold; }
    </style>
</head>
<body>
<h2>企業詳細</h2>

<c:if test="${not empty companies}">
    <!-- List<CompanyDTO> の1件目だけを取得 -->
    <c:set var="companyDTO" value="${companies[0]}"/>

    <div class="section">
        <p><span class="label">企業名:</span> ${companyDTO.company.companyName}</p>
        <p><span class="label">リクエスト済み:</span>
            <c:choose>
                <c:when test="${companyDTO.isRequest == '申請済み'}">はい</c:when>
                <c:otherwise>いいえ</c:otherwise>
            </c:choose>
        </p>
    </div>

    <div class="section">
        <h3>開催予定イベント</h3>
        <c:if test="${not empty companyDTO.company.events}">
            <table>
                <thead>
                    <tr>
                        <th>場所</th>
                        <th>開始時間</th>
                        <th>終了時間</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="event" items="${companyDTO.company.events}">
                    <tr>
                        <td>${event.eventPlace}</td>
                        <td>${event.eventStartTime}</td>
                        <td>${event.eventEndTime}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty companyDTO.company.events}">
            <p>開催予定のイベントはありません</p>
        </c:if>
    </div>

    <div class="section">
        <h3>卒業生情報</h3>
        <c:if test="${not empty companyDTO.company.graduates}">
            <table>
                <thead>
                    <tr>
                        <th>卒業生番号</th>
                        <th>名前</th>
                        <th>メール</th>
                        <th>職種</th>
                        <th>学科</th>
                        <th>卒業年次</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="grad" items="${companyDTO.company.graduates}">
                    <tr>
                        <td>${grad.graduateStudentNumber}</td>
                        <td>${grad.graduateName}</td>
                        <td>${grad.graduateEmail}</td>
                        <td>${grad.graduateJobCategory}</td>
                        <td>${grad.course.courseName}</td>
                        <td>${grad.course.courseTerm}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty companyDTO.company.graduates}">
            <p>卒業生情報はありません</p>
        </c:if>
    </div>

</c:if>

<c:if test="${empty companies}">
    <p>企業情報が見つかりません。</p>
</c:if>

</body>
</html>
