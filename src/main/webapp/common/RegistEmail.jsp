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
<title>連絡先登録画面（在校生/職員）<</title>


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
		<c:if test="${not empty error}">
			<div style="color: red; font-weight: bold; margin-bottom: 10px;">
				${error}</div>
		</c:if>

		<form action="graduate" method="post">
			<input type="hidden" name="fromConfirm" value="${fromConfirm}">

			<input type="hidden" name="command" value="RegistEmailNext">
			<div class="split-screen">
				<div class="left-screen">
					<span class="leftscreen-title">企業選択</span>
					<div class="flame">
						<table class="registscreen-table">
							<tr>
								<td>
									<div>企業</div>
								</td>
								<td><select name="companyId" class="company-input">
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
											<option value="${companyDTO.company.companyId}">
												<c:if test="${companyId == companyDTO.company.companyId}">selected
                                                </c:if>
												${companyDTO.company.companyName}
											</option>
										</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td>
									<div>職種</div>
								</td>
								<td>
									<!--職種選択--> <select name="jobType" id="jobType"
									class="job-input">
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
								<td>
									<div>氏名</div>
								</td>
								<td><input type="text" name="graduateName"
									value="${graduateName}" class="name-input"></td>
							</tr>

							<tr>
								<td>
									<div>学科</div>
								</td>
								<td><select name="courseCode" class="department-input">
										<c:choose>
											<%-- 新規登録の時だけ「学科を選択」を出す --%>
											<c:when test="${empty courseCode}">
												<option value="">学科選択</option>
											</c:when>

											<%-- 戻ってきたとき（Back 時）は courseName を見出しに表示 --%>
											<c:otherwise>
												<option value="${courseCode}">${courseName}</option>
											</c:otherwise>
										</c:choose>

										<!-- 企業一覧 -->
										<c:forEach var="course" items="${courses}">
											<option value="${course.courseCode}">
												<c:if test="${courseCode == course.courseCode}">selected</c:if>
												${course.courseName}
											</option>
										</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td>
									<div>学籍番号</div>
								</td>
								<td><input type="text" name="graduateStudentNumber"
									value="${graduateStudentNumber}"
									pattern="^[0-9]{2}[a-z]{2}[0-9]{4}$"
									title="例: 24jy0101 の形式で入力してください" class="student-number-input">
								</td>
							</tr>
							<tr>
								<td>
									<div>メールアドレス</div>
								</td>
								<td><input type="email" name="graduateEmail"
									value="${graduateEmail}" pattern="^(?!.*@jec\.ac\.jp$).+"
									title="jec.ac.jpのメールアドレスは使用できません" class="email-input">

									<div class="input-note">学校のメールアドレス(@jec.ac.jp)は登録できません</div></td>
							</tr>
							<tr>
								<td>
									<div>その他</div>
								</td>
								<td><input type="text" name="otherInfo"
									value="${otherInfo}" class="other-info-input"></td>
							</tr>
						</table>
					</div>
				</div>
			</div>


			<div class="bottom-btn-right">
				<button type="submit">確認</button>
			</div>
		</form>


	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
	<!--	処理-->
	<script>
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