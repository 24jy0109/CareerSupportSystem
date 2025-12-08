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
	<form action="company" method="post">
		<input type="hidden" name="command" value="CompanyRegisterConfirm">
		<!--	企業選択-->
		<h1>企業選択</h1>
		<div>
			<!--			企業名選択-->
			<select name="companyName">
				<!--			<option value="">企業名を選択</option>-->
				<c:forEach var="companyDTO" items="${companies}">
					<option value="${companyDTO.company.companyName}">
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
			氏名：<input type="text" name="studentName"> 
			<br>
			学科： <select
				name="courseName">
				<option value="">学科を選択</option>
				<c:forEach var="course" items="${courses}">
					<option value="${course.courseName}">${course.courseName}
					</option>
				</c:forEach>
			</select>
			<br>
			学籍番号：<input type="text" name="studentNumber"> 
			<br>
			メールアドレス：<input type="text" name="mailAddress"> 
			<br>
			その他：<input type="text" name="other"> 
			
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