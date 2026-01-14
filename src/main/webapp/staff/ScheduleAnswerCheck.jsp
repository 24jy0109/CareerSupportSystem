<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/pagination.css">
<title>日程回答確認画面（職員）</title>

<script type="text/javascript">
	function confirmReject(answerId) {
		if (confirm("この回答を見送ります。\n卒業生へ開催見送りメールが送信され、回答は削除されますがよろしいですか？")) {
			location.href = "answer?command=noAnswer&answerId=" + answerId;
		}
		return false;
	}

	function confirmDelete(answerId) {
		if (confirm("この回答を削除します。よろしいですか？")) {
			location.href = "answer?command=deleteAnswer&answerId=" + answerId;
			return true;
		}
		return false;
	}

	function injectChoice(form, answerId) {

		// 該当 answerId の radio を全取得
		const radios = document.querySelectorAll(
			'input[type="radio"][name="choice_' + answerId + '"]'
		);

		let selected = null;

		radios.forEach(r => {
			if (r.checked) selected = r.value;
		});

		// 未選択防止
		if (selected === null) {
			alert("希望日時を選択してください。");
			return false;
		}

		// hidden が無ければ作る
		let hidden = form.querySelector('input[name="choice"]');
		if (!hidden) {
			hidden = document.createElement("input");
			hidden.type = "hidden";
			hidden.name = "choice";
			form.appendChild(hidden);
		}

		hidden.value = selected;
		return true; // submit 継続
	}
</script>
</head>

<body>
	<header class="head-part">
		<div id="h-left">
			<img src="./img/rogo.png" alt="アイコン">
		</div>

		<div class="header-title"
			onclick="location.href='mypage?command=AppointmentMenu'">
			<div class="title-jp">就活サポート</div>
			<div class="title-en">Career Support</div>
		</div>

		<div class="header-user">ようこそ${name}さん</div>
	</header>

	<main>
		<h2 class="page-title">日程回答確認</h2>

		<c:if test="${empty answers}">
			<p class="errormsg answer-errormsg">回答がありません。</p>
		</c:if>

		<table class="schedule-check-table">
			<tr class="schedule-check-r">
				<th class="schedule-check-h">企業名</th>
				<th class="schedule-check-h">名前</th>
				<th class="schedule-check-h">回答</th>
				<th class="schedule-check-h"></th>
			</tr>

			<c:forEach var="a" items="${answers}">
				<tr class="schedule-check-r2 answer-row">
					<td></td>
					<td></td>

					<td><c:if test="${a.eventAvailability}">
							<label> <input type="radio" name="choice_${a.answerId}"
								value="1" required> 第1希望
								${a.firstChoiceStartTime.year}/${a.firstChoiceStartTime.monthValue}/${a.firstChoiceStartTime.dayOfMonth}
								${a.firstChoiceStartTime.hour < 10 ? '0' : ''}${a.firstChoiceStartTime.hour}:
								${a.firstChoiceStartTime.minute < 10 ? '0' : ''}${a.firstChoiceStartTime.minute}～
								${a.firstChoiceEndTime.hour < 10 ? '0' : ''}${a.firstChoiceEndTime.hour}:
								${a.firstChoiceEndTime.minute < 10 ? '0' : ''}${a.firstChoiceEndTime.minute}
							</label>
						</c:if></td>
					<td></td>
				</tr>

				<tr class="schedule-check-r3">
					<td>${a.event.company.companyName}</td>
					<td>${a.graduate.graduateName}</td>

					<td><c:choose>
							<c:when test="${!a.eventAvailability}">
								不参加
							</c:when>

							<c:when test="${a.secondChoiceStartTime != null}">
								<label> <input type="radio" name="choice_${a.answerId}"
									value="2"> 第2希望
									${a.secondChoiceStartTime.year}/${a.secondChoiceStartTime.monthValue}/${a.secondChoiceStartTime.dayOfMonth}
									${a.secondChoiceStartTime.hour < 10 ? '0' : ''}${a.secondChoiceStartTime.hour}:
									${a.secondChoiceStartTime.minute < 10 ? '0' : ''}${a.secondChoiceStartTime.minute}～
									${a.secondChoiceEndTime.hour < 10 ? '0' : ''}${a.secondChoiceEndTime.hour}:
									${a.secondChoiceEndTime.minute < 10 ? '0' : ''}${a.secondChoiceEndTime.minute}
								</label>
							</c:when>

							<c:otherwise>
								第2希望 なし
							</c:otherwise>
						</c:choose></td>

					<td>
						<form action="event" method="get"
							onsubmit="return injectChoice(this, ${a.answerId});">
							<c:choose>
								<c:when test="${a.eventAvailability}">
									<input type="hidden" name="command" value="yesAnswer">
									<input type="hidden" name="answerId" value="${a.answerId}">
									<input type="submit" value="企画" class="decision-button">
									<button type="button" class="cancel-button"
										onclick="return confirmReject(${a.answerId});">見送</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="cancel-button"
										onclick="return confirmDelete(${a.answerId});">削除</button>
								</c:otherwise>
							</c:choose>
						</form>
					</td>
				</tr>

				<tr class="schedule-check-r">
					<td></td>
					<td></td>
					<td><c:if test="${a.eventAvailability}">
							<c:choose>
								<c:when test="${a.thirdChoiceStartTime != null}">
									<label> <input type="radio" name="choice_${a.answerId}"
										value="3"> 第3希望
										${a.thirdChoiceStartTime.year}/${a.thirdChoiceStartTime.monthValue}/${a.thirdChoiceStartTime.dayOfMonth}
										${a.thirdChoiceStartTime.hour < 10 ? '0' : ''}${a.thirdChoiceStartTime.hour}:
										${a.thirdChoiceStartTime.minute < 10 ? '0' : ''}${a.thirdChoiceStartTime.minute}～
										${a.thirdChoiceEndTime.hour < 10 ? '0' : ''}${a.thirdChoiceEndTime.hour}:
										${a.thirdChoiceEndTime.minute < 10 ? '0' : ''}${a.thirdChoiceEndTime.minute}
									</label>
								</c:when>
								<c:otherwise>
									第3希望 なし
								</c:otherwise>
							</c:choose>
						</c:if></td>
					<td></td>
				</tr>
			</c:forEach>
		</table>

		<div id="pagination-answer" class="pagination"></div>
	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

	<script>
let activePager = null;

function setupPagination(rowSelector, paginationId, rowsPerPage = 10) {
	const rows = document.querySelectorAll(rowSelector);
	const pagination = document.getElementById(paginationId);
	if (!rows.length) return null;

	const totalPages = Math.ceil(rows.length / rowsPerPage);
	let currentPage = 1;
	const buttons = [];

	function showPage(page) {
		if (page < 1 || page > totalPages) return;
		currentPage = page;

		const start = (page - 1) * rowsPerPage;
		const end = start + rowsPerPage;

		rows.forEach((row, index) => {
			row.style.display = (index >= start && index < end) ? "" : "none";
		});

		buttons.forEach(btn => btn.classList.remove("active"));
		if (buttons[page - 1]) buttons[page - 1].classList.add("active");
	}

	for (let i = 1; i <= totalPages; i++) {
		const btn = document.createElement("button");
		btn.textContent = i;
		btn.onclick = () => {
			activePager = pager;
			showPage(i);
		};
		buttons.push(btn);
		pagination.appendChild(btn);
	}

	showPage(1);

	const pager = {
		next() { showPage(currentPage + 1); },
		prev() { showPage(currentPage - 1); }
	};

	return pager;
}

const answerPager = setupPagination(".answer-row", "pagination-answer", 10);
activePager = answerPager;

document.addEventListener("keydown", function(e) {
	const tag = document.activeElement.tagName;
	if (tag === "INPUT" || tag === "TEXTAREA") return;
	if (!activePager) return;
	if (e.key === "ArrowRight") activePager.next();
	if (e.key === "ArrowLeft") activePager.prev();
});
</script>

	<jsp:include page="/common/flashMessage.jsp" />
</body>
</html>