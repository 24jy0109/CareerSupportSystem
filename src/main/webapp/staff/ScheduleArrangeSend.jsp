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
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
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

			<div class="header-user">ようこそ 24jy0119 さん</div>
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

			<input type="hidden" name="command" value="SendScheduleArrangeEmail">
			<div class="schedulearrange-row">
				<div class="field-name">宛先</div>
				<div>
                <div class="toname-toemail">
                    <div>川原田こうき</div>
                    <div>24jy0000@gmail.com</div>
                </div>
            </div>
				<p>
					<%=grad.getGraduateName()%>
					（学籍番号：<%=grad.getGraduateStudentNumber()%>）
				</p>
				<input type="hidden" name="graduateStudentNumber"
					value="<%=grad.getGraduateStudentNumber()%>"> <input
					type="hidden" name="companyId"
					value="<%=grad.getCompany().getCompanyId()%>"> <br>

				<!-- 担当スタッフ -->
				<label>担当スタッフ</label><br> <select name="staffId">
					<%
					for (Staff s : staffs) {
					%>
					<option value="<%=s.getStaffId()%>">
						<%=s.getStaffName()%>
					</option>
					<%
					}
					%>
				</select> <br> <br>

				<!-- 件名（デフォルト値入り） -->
				<label>件名</label><br> <input type="text" name="mailTitle"
					size="60" value="<%=defaultTitle%>" required> <br> <br>

				<!-- 本文（デフォルト値入り） -->
				<label>本文</label><br>
				<textarea name="mailBody" rows="14" cols="60" required><%=defaultBody%></textarea>

				<br> <br> <input type="submit" value="メールを送信する">
		</form>

	</main>
	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

</body>
</html>
