<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>連絡先登録</title>


</head>
<body>
	<h2>登録画面</h2>
	<form action="graduate" method="post">
		<input type="hidden" name="command" value="RegistEmailNext">
		<!--	企業選択-->
		<h1>企業選択</h1>
		<div>
			<!--			企業名選択-->
			<select name="companyId">

				<c:choose>
					<%-- 新規登録の時だけ「企業選択」を出す --%>
					<c:when test="${empty companyId}">
						<option value="">企業選択</option>
					</c:when>

					<%-- 戻ってきたとき（Back 時）は companyName を見出しに表示 --%>
					<c:otherwise>
						<option value="${companyId}">${companyName}</option>
					</c:otherwise>
				</c:choose>

				<!-- 企業一覧 -->
				<c:forEach var="companyDTO" items="${companies}">
					<option value="${companyDTO.company.companyId}"
						<c:if test="${companyId == companyDTO.company.companyId}">selected</c:if>>
						${companyDTO.company.companyName}</option>
				</c:forEach>
			</select>

			<!--職種選択-->
			<select name="jobType" id="jobType">
				<option value="">職種を選択</option>
				<option value="エンジニア">エンジニア</option>
				<option value="デザイナー">デザイナー</option>
				<option value="営業">営業</option>
				<option value="マーケティング">マーケティング</option>
				<option value=other>その他</option>
			</select>
			<div id="otherJobDiv" style="display: none; margin-top: 10px;">
				<textarea name="otherJob" rows="3" cols="40"
					placeholder="職種を自由に入力してください"></textarea>
			</div>
		</div>
		<!--		学生情報-->
		<h1>学生情報</h1>
		<div>
			氏名：<input type="text" name="graduateName" value="${graduateName}">
			<br> 学科： <select name="courseCode">

				<c:choose>
					<%-- 新規登録の時だけ「学科を選択」を出す --%>
					<c:when test="${empty courseCode}">
						<option value="">企業選択</option>
					</c:when>

					<%-- 戻ってきたとき（Back 時）は courseName を見出しに表示 --%>
					<c:otherwise>
						<option value="${courseCode}">${courseName}</option>
					</c:otherwise>
				</c:choose>

				<!-- 企業一覧 --> <
				<c:forEach var="course" items="${courses}">
					<option value="${course.courseCode}"
						<c:if test="${courseCode == course.courseCode}">selected</c:if>>
						${course.courseName}</option>
				</c:forEach>

			</select> <br> 学籍番号：<input type="text" name="graduateStudentNumber"
				value="${graduateStudentNumber}"> <br> メールアドレス：<input
				type="text" name="graduateEmail" value="${graduateEmail}"> <br>
			その他： <input type="text" name="otherInfo" value="${otherInfo}">
		</div>



		<button type="submit">確認</button>
	</form>

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