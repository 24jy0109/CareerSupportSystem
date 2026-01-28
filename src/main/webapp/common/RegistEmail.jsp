<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>連絡先情報入力画面</title>

<style>
	.warning-stock {
		color: #555; /* 強すぎないグレー */
		font-size: 0.9em; /* 少し小さめ */
		margin-top: 5px; /* 上とのスペース */
		margin-left: 10px; /* 左少しスペース */
		padding-left: 10px; /* 入力欄との微調整 */
		font-style: italic; /* 柔らかい印象 */
	}
</style>
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
	<div class="page-title">連絡先情報入力</div>
		<c:if test="${not empty error}">
			<div style="color: red; font-weight: bold; margin-bottom: 10px;">
				${error}</div>
		</c:if>

		<form action="graduate" method="post">
			<c:set var="isStudent" value="${sessionScope.role == 'student'}" />
			<input type="hidden" name="fromConfirm" value="${fromConfirm}">
			<input type="hidden" name="updateMode" value="${updateMode}">
			<input type="hidden" name="check" value="${check}"> <input
				type="hidden" name="command" value="RegistEmailNext">
			<div class="split-screen">
				<div class="left-screen">
					<span class="leftscreen-title">企業選択</span>
					<div class="flame">
						<table class="registscreen-table">
							<tr>
								<td>企業検索</td>
								<td><input type="text" id="companySearch"
									placeholder="企業名で検索">
								</td>
							</tr>
							<tr>
								<td colspan="2" class="warning-stock">
									検索結果は企業選択ボックスを開いてご確認ください。
								</td>
							</tr>
							<tr>
								<td>
									<div>企業</div>
								</td>
								<td><select name="companyId" class="company-input" required>
										<c:choose>
											<%-- 新規登録の時だけ「企業選択」を出す --%>
											<c:when test="${empty companyId}">
												<option value="">企業選択
											</c:when>

											<%-- 戻ってきたとき（Back 時）は companyName を見出しに表示 --%>
											<c:otherwise>
												<option value="${companyId}">${companyName}</option>
											</c:otherwise>
										</c:choose>
										<!-- 企業一覧 -->
										<c:forEach var="companyDTO" items="${companies}">
											<option value="${companyDTO.company.companyId}"
												<c:if test="${companyId eq companyDTO.company.companyId}">selected</c:if>>
												${companyDTO.company.companyName}</option>
										</c:forEach>

								</select>
									<div id="noCompanyMessage"
										style="display: none; color: red; margin-top: 6px; font-size: 0.9em;">
										該当する企業はございません</div></td>
							</tr>

							<tr>
								<td>
									<div>職種</div>
								</td>
								<td>
									<!--職種選択--> <select name="jobType" id="jobType"
									class="job-input" required>
										<c:choose>
											<%-- 新規登録の時だけ「職種を選択」を出す --%>
											<c:when test="${empty jobType}">
												<option value="">職種を選択</option>
											</c:when>

											<%-- 戻ってきたとき（Back 時）は jobType を見出しに表示 --%>
											<c:otherwise>
												<option value="${jobType}">${jobType}</option>
											</c:otherwise>
										</c:choose>

										<option value="エンジニア">エンジニア</option>
										<option value="デザイナー">デザイナー</option>
										<option value="営業">営業</option>
										<option value="マーケティング">マーケティング</option>
										<option value=other>その他</option>
								</select>
								</td>
							</tr>
							<tr>
								<td></td>
								<td>
									<div id="otherJobDiv" style="display: none;">
										<textarea name="otherJob" rows="3" cols="35"
											placeholder="職種を自由に入力してください"></textarea>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>

				<div class="right-screen">
					<span class="rightscreen-title">学生情報</span>
					<div class="flame">
						<table class="registscreen-table">
							<tr>
								<td><div>氏名</div></td>
								<td><c:choose>
										<c:when test="${isStudent}">
										        ${name}
										        <input type="hidden" name="graduateName"
												value="${graduateName}" required>
										</c:when>
										<c:otherwise>
											<input type="text" name="graduateName"
												value="${graduateName}" class="name-input" required>
										</c:otherwise>
									</c:choose></td>
							</tr>


							<tr>
								<td>
									<div>学科</div>
								</td>
								<td><c:choose>

										<c:when test="${isStudent}">
											<span class="department-fixed">${courseName}</span>
											<input type="hidden" name="courseCode" value="${courseCode}"
												required>
										</c:when>

										<c:otherwise>
											<!-- 学科検索 -->
											<input type="text" id="courseSearch" placeholder="学科名で検索"
												style="margin-bottom: 6px;">
											<select name="courseCode" class="department-input"
												id="courseSelect" required>
												<c:if test="${empty courseCode}">
													<option value="">学科選択</option>
												</c:if>

												<c:forEach var="course" items="${courses}">
													<option value="${course.courseCode}"
														<c:if test="${courseCode eq course.courseCode}">selected</c:if>>
														${course.courseName}</option>
												</c:forEach>
											</select>
										</c:otherwise>

									</c:choose></td>
							</tr>
							<tr>
								<td><div>学籍番号</div></td>
								<td><c:choose>
										<c:when test="${isStudent}">
								        ${studentNumber}
								        <input type="hidden" name="graduateStudentNumber"
												value="${studentNumber}" required>
										</c:when>
										<c:otherwise>
											<input type="text" name="graduateStudentNumber"
												value="${graduateStudentNumber}"
												pattern="^[0-9]{2}[a-z]{2}[0-9]{4}$"
												title="例: 24jy0101 の形式で入力してください"
												class="student-number-input" required>
										</c:otherwise>
									</c:choose></td>
							</tr>

							<tr>
								<td>
									<div>メールアドレス</div>
								</td>
								<td><input type="email" name="graduateEmail"
									value="${graduateEmail}" pattern="^(?!.*@jec\.ac\.jp$).+"
									title="jec.ac.jpのメールアドレスは使用できません" class="email-input" required>

									<div class="input-note">学校のメールアドレス(@jec.ac.jp)は登録できません</div></td>
							</tr>
							<tr>
								<td>
									<div>その他</div>
								</td>
								<td><input type="text" name="otherInfo"
									value="${otherInfo}" class="other-info-input" maxlength="50"
									placeholder="50文字以内で入力してください"></td>
							</tr>
						</table>
					</div>
				</div>
			</div>


			<!-- 戻るボタン -->
			<!--まず生徒かスタッフかで分ける-->
			<!--生徒はメニューに戻るのみ,スタッフは下記４パターンあり-->
			<!--fromComfirm == null && updateMode == true 情報編集に戻る -->
			<!--fromComfirm == true && updateMode == true 情報編集に戻る-->
			<!--fromComfirm == true && updateMode == "" メニューに戻る-->
			<!--fromComfirm == null && updateMode == null メニューに戻る-->
			<c:choose>
				<c:when test="${isStudent}">
					<c:set var="backUrl" value="mypage?command=AppointmentMenu" />
					<c:set var="backLabel" value="メニューに戻る" />
				</c:when>

				<c:otherwise>
					<c:choose>
						<c:when test="${updateMode == 'true'}">
							<c:set var="backUrl"
								value="graduate?companyId=${companyId}&command=editInfo" />
							<c:set var="backLabel" value="情報編集に戻る" />
						</c:when>
						<c:otherwise>
							<c:set var="backUrl" value="mypage?command=AppointmentMenu" />
							<c:set var="backLabel" value="メニューに戻る" />
						</c:otherwise>

					</c:choose>
				</c:otherwise>
			</c:choose>

			<div class="bottom-btn-split">
				<div>
					<button type="button" onclick="location.href='${backUrl}'">${backLabel}</button>
				</div>

				<div class="btn-gap">
					<button type="submit">確認</button>
				</div>
			</div>
		</form>


	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
	<!--処理-->
	<script>
	/* ===== 企業検索 ===== */
		const searchInput = document.getElementById("companySearch");
		const companySelect = document.querySelector("select[name='companyId']");
		const allOptions = Array.from(companySelect.options);
		const noCompanyMessage = document.getElementById("noCompanyMessage");
		
		searchInput.addEventListener("input", function () {
		  const query = this.value.toLowerCase();
		  let visibleCount = 0;
		
		  allOptions.forEach(option => {
		    if (option.value === "") return; // 「企業選択」は無視
		
		    const isMatch = option.text.toLowerCase().includes(query);
		    option.style.display = isMatch ? "" : "none";
		
		    if (isMatch) visibleCount++;
		  });
		
		  // 表示できる企業がなければメッセージ表示
		  if (visibleCount === 0 && query !== "") {
		    noCompanyMessage.style.display = "block";
		  } else {
		    noCompanyMessage.style.display = "none";
		  }
		});


	/* ===== 学科検索（職員のみ） ===== */
	const courseSearch = document.getElementById("courseSearch");
	const courseSelect = document.getElementById("courseSelect");

	if (courseSearch && courseSelect) {
	  const allCourseOptions = Array.from(courseSelect.options);

	  courseSearch.addEventListener("input", function () {
	    const query = this.value.toLowerCase();

	    allCourseOptions.forEach(option => {
	      if (option.value === "") return;
	      option.style.display =
	        option.text.toLowerCase().includes(query) ? "" : "none";
	    });
	  });
	} 
	
	
		document.getElementById("jobType").addEventListener("change",
				function() {
					const selected = this.value;
					const otherDiv = document.getElementById("otherJobDiv");

					if (selected === "other") {
						otherDiv.style.display = "block";
					} else {
						otherDiv.style.display = "none";
					}
				});
	</script>
</body>
</html>