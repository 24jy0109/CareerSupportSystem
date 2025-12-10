<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="dto.EventDTO" %>
<%@ page import="model.Graduate" %>
<%@ page import="model.Staff" %>
<%@ page import="model.Event" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>日程調整メール作成</title>
</head>
<body>

<%
    // Controller で渡された events（List<EventDTO>）
    List<EventDTO> events = (List<EventDTO>) request.getAttribute("events");

    // 今回は EventDTO が1件だけ入っている前提
    EventDTO dto = events.get(0);

    Event event = dto.getEvent();
    List<Staff> staffs = dto.getStaffs();
    List<Graduate> graduates = dto.getGraduates();

    // 卒業生は1人だけ入っている前提
    Graduate grad = graduates.get(0);
%>

<h2>日程調整メール作成</h2>

<form action="event" method="post">

    <input type="hidden" name="command" value="SendScheduleArrangeEmail">

    <!-- 宛先（卒業生） -->
    <label>宛先</label><br>
    <p>
        <%= grad.getGraduateName() %>
        （学籍番号：<%= grad.getGraduateStudentNumber() %>）
    </p>
    <input type="hidden" name="graduateStudentNumber"
           value="<%= grad.getGraduateStudentNumber() %>">

    <br>
    
        <!-- 企業ID -->
    <input type="hidden" name="companyId"
           value="<%= grad.getCompany().getCompanyId() %>">

    <br>

    <!-- 担当スタッフ -->
    <label>担当スタッフ</label><br>
    <select name="staffId">
        <% for (Staff s : staffs) { %>
            <option value="<%= s.getStaffId() %>">
                <%= s.getStaffName() %>
            </option>
        <% } %>
    </select>

    <br><br>

    <!-- 件名 -->
    <label>件名</label><br>
    <input type="text" name="mailTitle" size="60" required>

    <br><br>

    <!-- 本文 -->
    <label>本文</label><br>
    <textarea name="mailBody" rows="10" cols="60" required></textarea>

    <br><br>

    <input type="submit" value="メールを送信する">

</form>

</body>
</html>
