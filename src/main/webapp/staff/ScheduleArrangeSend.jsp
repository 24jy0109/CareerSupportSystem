<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="dto.EventDTO"%>
<%@ page import="model.Graduate"%>
<%@ page import="model.Staff"%>
<%@ page import="model.Event"%>
<%@ page import="dto.EmailDTO"%>

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

		EmailDTO emailDTO = (EmailDTO) request.getAttribute("email");

		String mailTitle;
		String mailBody;
		Staff defaultStaff;

		if (emailDTO != null) {
			mailTitle = emailDTO.getEmail().getSubject();
			mailBody = emailDTO.getInputBody();
			defaultStaff = emailDTO.getStaff();
		} else {
			mailTitle = "【イベント開催】参加可否・希望日程の回答のお願い";
			mailBody = grad.getGraduateName() + " 様\n\n"
			+ "お世話になっております。\n"
			+ "キャリアサポートセンターの担当スタッフよりご連絡です。\n\n"
			+ "この度、卒業生アポイントメントを開催するにあたり、\n"
			+ "【○月○日 〜 ○月○日】の期間内にてご参加可能な日時を\n"
			+ "記載のリンクより、ご回答くださいますようお願いいたします。\n\n"
			+ "また、ご不明点などありましたら、\n"
			+ "担当スタッフまでお気軽にご連絡ください。\n\n"
			+ "よろしくお願いいたします。\n\n";
			defaultStaff = staffs.get(0);
		}
		%>

		<form action="event" method="post">
			<input type="hidden" name="command"
				value="ScheduleArrangeEmailConfirm">

			<div class="schedulearrange-row">
				<div class="field-name">担当者</div>
				<select name="staffId" required>
					<%
					for (Staff s : staffs) {
						String selected = (defaultStaff != null && s.getStaffId() == defaultStaff.getStaffId()) ? "selected" : "";
					%>
					<option value="<%=s.getStaffId()%>" <%=selected%>><%=s.getStaffName()%></option>
					<%
					}
					%>
				</select>
			</div>

			<div class="schedulearrange-row">
				<div class="field-name">宛先</div>
				<div>
					<div class="toname-toemail"><%=grad.getGraduateName()%>（<%=grad.getGraduateStudentNumber()%>）
					</div>
					<input type="hidden" name="graduateStudentNumber"
						value="<%=grad.getGraduateStudentNumber()%>"> <input
						type="hidden" name="companyId"
						value="<%=grad.getCompany().getCompanyId()%>">
				</div>
			</div>

			<div class="schedulearrange-row">
				<div class="field-name">件名</div>
				<input type="text" name="mailTitle" size="60" value="<%=mailTitle%>"
					class="email-title" required>
			</div>


			<div class="schedulearrange-row">
				<div class="field-name"></div>
				<p style="margin: 0; font-size: 0.9em; color: #555;">
					※本メール送信時には、本文下部に回答用URLおよび担当スタッフ名・メールアドレスが自動的に記載されます。</p>
			</div>
			<div class="schedulearrange-row">
				<div class="field-name">本文</div>
				<textarea name="mailBody" class="email-text" rows="30" cols="100"
					required><%=mailBody%></textarea>
			</div>

			<div class="bottom-btn-split schedulearrange-button">
				<div>
					<button type="button" onclick="location.href='event?command=RegistEventForm&companyId=<%=grad.getCompany().getCompanyId()%>'" >開催情報登録に戻る</button>
				</div>
				<input type="submit" value="確認へ" class="schedulearrange-button">
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
