<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>イベント回答フォーム</title>
</head>
<body>

<%
    // URL: answer?command=AnswerForm&answerId=1
    String answerId = request.getParameter("answerId");
%>

<h2>参加可否・希望日時フォーム</h2>

<form action="answer" method="post">

    <!-- Hidden: answerId -->
    <input type="hidden" name="answerId" value="<%=answerId%>">
    <input type="hidden" name="command" value="registAnswer">

    <!-- 参加可否 -->
    <label>参加可否 (event_availability)</label><br>
    <input type="radio" name="eventAvailability" value="true"> 出席する<br>
    <input type="radio" name="eventAvailability" value="false"> 出席しない<br>
    <input type="radio" name="eventAvailability" value="null" checked> 未回答<br><br>

    <!-- 第一希望 -->
    <h3>第一希望</h3>
    <label>開始（first_choice_start_time）</label><br>
    <input type="datetime-local" name="firstChoiceStart"><br>

    <label>終了（first_choice_end_time）</label><br>
    <input type="datetime-local" name="firstChoiceEnd"><br><br>

    <!-- 第二希望 -->
    <h3>第二希望</h3>
    <label>開始（second_choice_start_time）</label><br>
    <input type="datetime-local" name="secondChoiceStart"><br>

    <label>終了（second_choice_end_time）</label><br>
    <input type="datetime-local" name="secondChoiceEnd"><br><br>

    <!-- 第三希望 -->
    <h3>第三希望</h3>
    <label>開始（third_choice_start_time）</label><br>
    <input type="datetime-local" name="thirdChoiceStart"><br>

    <label>終了（third_choice_end_time）</label><br>
    <input type="datetime-local" name="thirdChoiceEnd"><br><br>

    <input type="submit" value="回答する">
</form>

</body>
</html>
