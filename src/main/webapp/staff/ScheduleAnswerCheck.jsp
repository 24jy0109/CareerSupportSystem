<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>回答一覧</title>
</head>
<body>

<h2>回答一覧</h2>

<c:forEach var="a" items="${answers}">
    <hr>

    <form action="event" method="post">

        <p>Answer ID：${a.answerId}</p>
        <p>企業名：${a.event.company.companyName}</p>
        <p>卒業生名：${a.graduate.graduateName}</p>

        <p>
            <label>
                <input type="radio" name="choice" value="1" required>
                第1希望：
                ${a.firstChoiceStartTime} ～ ${a.firstChoiceEndTime}
            </label>
        </p>

        <p>
            <label>
                <input type="radio" name="choice" value="2">
                第2希望：
                ${a.secondChoiceStartTime} ～ ${a.secondChoiceEndTime}
            </label>
        </p>

        <p>
            <label>
                <input type="radio" name="choice" value="3">
                第3希望：
                ${a.thirdChoiceStartTime} ～ ${a.thirdChoiceEndTime}
            </label>
        </p>

        <input type="hidden" name="command" value="yesAnswer">
        <input type="hidden" name="answerId" value="${a.answerId}">

        <input type="submit" value="決定">
    </form>

    <!-- 拒否 -->
    <form action="AnswerController" method="post">
        <input type="hidden" name="command" value="noAnswer">
        <input type="hidden" name="answerId" value="${a.answerId}">
        <input type="submit" value="拒否">
    </form>

</c:forEach>

</body>
</html>
