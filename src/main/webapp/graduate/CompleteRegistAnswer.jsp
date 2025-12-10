<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Answer" %>
<%@ page import="java.util.List" %>

<%
    List<Answer> answers = (List<Answer>) request.getAttribute("answers");
    Answer answer = null;
    if (answers != null && !answers.isEmpty()) {
        answer = answers.get(0);   // 保存された Answer を取得
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Submission Complete</title>
<style>
    body { font-family: Arial; margin: 40px; }
    .box {
        border: 1px solid #ccc; padding: 20px;
        width: 500px; border-radius: 8px;
        background: #f8f8f8;
    }
    .label { font-weight: bold; }
</style>
</head>
<body>

<h1>Submission Complete</h1>

<div class="box">
    <% if (answer != null) { %>

        <p><span class="label">Answer ID:</span> <%= answer.getAnswerId() %></p>

        <p><span class="label">Graduate Student Number:</span>
            <%= answer.getGraduateStudentNumber() %></p>

        <p><span class="label">Event Availability:</span>
            <%= answer.getEventAvailability() %></p>

        <hr>

        <p><span class="label">First Choice:</span><br>
            <%= answer.getFirstChoiceStartTime() %> 〜 <%= answer.getFirstChoiceEndTime() %>
        </p>

        <p><span class="label">Second Choice:</span><br>
            <%= answer.getSecondChoiceStartTime() %> 〜 <%= answer.getSecondChoiceEndTime() %>
        </p>

        <p><span class="label">Third Choice:</span><br>
            <%= answer.getThirdChoiceStartTime() %> 〜 <%= answer.getThirdChoiceEndTime() %>
        </p>

    <% } else { %>

        <p>No answer information was found.</p>

    <% } %>
</div>

<br>

<a href="index.jsp">Back to Home</a>

</body>
</html>
