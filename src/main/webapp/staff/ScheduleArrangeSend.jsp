<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="dto.EventDTO"%>
<%@ page import="model.Graduate"%>
<%@ page import="model.Staff"%>
<%@ page import="model.Event"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/companylist.css">
<title>日程調整メール作成</title>
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

		<%
		List<EventDTO> events = (List<EventDTO>) request.getAttribute("events");
		EventDTO dto = events.get(0);

		Event event = dto.getEvent();
		List<Staff> staffs = dto.getStaffs();
		List<Graduate> graduates = dto.getGraduates();

		Graduate grad = graduates.get(0);

		// デフォルト件名
		String defaultTitle = "【イベント開催】参加可否・希望日程の回答のお願い";

		// デフォルト本文
		String defaultBody = grad.getGraduateName() + " 様\n\n"
				+ "お世話になっております。\n"
				+ "キャリアサポートセンターの担当スタッフよりご連絡です。\n\n"
				+ "この度、卒業生アポイントメントを開催するにあたり、\n"
				+ "ご都合の良い日時をお伺いしたく存じます。\n\n"
				+ "記載のリンクより、参加可能な日時をご回答ください。\n"
				+ "また、ご不明点などありましたら、\n"
				+ "担当スタッフまでお気軽にご連絡ください。\n\n"
				+ "よろしくお願いいたします。\n\n";
		%>

		<!--		<h2>日程調整メール作成</h2>-->

		<form action="event" method="post">
			<input type="hidden" name="command" value="ScheduleArrangeEmailConfirm">

		<div class="schedulearrange-row">
			<div class="field-name">担当者</div>
			<select name="staffId">
				<%
				for (Staff s : staffs) {
				%>
				<option value="<%=s.getStaffId()%>">
					<%=s.getStaffName()%>
				</option>
				<%
				}
				%>
			</select>
		</div>
			<div class="schedulearrange-row">
				<div class="field-name">宛先</div>
				<div>
					<div class="toname-toemail">
						<%=grad.getGraduateName()%>
						<%=grad.getGraduateStudentNumber()%>
					</div>
					<input type="hidden" name="graduateStudentNumber"
						value="<%=grad.getGraduateStudentNumber()%>"> <input
						type="hidden" name="companyId"
						value="<%=grad.getCompany().getCompanyId()%>">
				</div>
			</div>


			<!-- 担当スタッフ -->



			<div class="schedulearrange-row">
				<div class="field-name">件名</div>
				<input type="text" name="mailTitle" size="60"
					value="<%=defaultTitle%>" class="email-title" required>
			</div>



			<div class="schedulearrange-row">
				<div class="field-name">本文</div>
				<textarea name="mailBody" class="email-text" rows="30" cols="100"
					required><%=defaultBody%></textarea>
			</div>



			<div class="bottom-btn-right schedulearrange-button">
				<input type="submit" value="日程確認送信" class="schedulearrange-button">

			</div>
		</form>

	</main>
	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

</body>
</html>
