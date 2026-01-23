<%@ page contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>日程調整メール 確認</title>
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/companylist.css">
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

		<div class="page-title">日程調整メール確認</div>

		<c:set var="mail" value="${email}" />


		<form id="emailForm" action="event" method="post"
			onsubmit="return showLoadingAndDisableSubmit(this);">
			<!-- デフォルト command -->
			<input type="hidden" id="commandField" name="command"
				value="SendScheduleArrangeEmail">

			<!-- 再送用 hidden -->
			<input type="hidden" name="graduateStudentNumber"
				value="${mail.graduate.graduateStudentNumber}"> <input
				type="hidden" name="staffId" value="${mail.staff.staffId}">
			<input type="hidden" name="companyId"
				value="${mail.company.companyId}"> <input type="hidden"
				name="mailTitle" value="${mail.email.subject}"> <input
				type="hidden" name="mailBody" value="${mail.inputBody}">

			<div class="schedulearrange-row">
				<div class="field-name">担当者</div>
				<div>${mail.staff.staffName}</div>
			</div>


			<div class="schedulearrange-row">
				<div class="field-name">宛先</div>
				<div class="toname-toemail">${mail.graduate.graduateName}（${mail.graduate.graduateStudentNumber}）</div>
			</div>

			<div class="schedulearrange-row">
				<div class="field-name">件名</div>
				<div>${mail.email.subject}</div>
			</div>

			<div class="schedulearrange-row">
				<div class="field-name">本文</div>
				<!--				ここは改行なく一行にしないとうまく表示されない！-->
				<pre class="confirm-mail-body">
					<c:out value="${mail.email.body}" />
				</pre>
			</div>
			</div>

			<div class="bottom-btn-split">
				<!-- 戻るボタンで command を切り替え -->
				<button type="submit"
					onclick="document.getElementById('commandField').value='ScheduleArrangeEmailBack'">
					戻る</button>
				<button type="submit">送信</button>
			</div>
		</form>
	</main>
	<jsp:include page="/common/loading.jsp" />
</body>
</html>