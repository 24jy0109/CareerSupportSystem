<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>企業詳細（卒業生一覧 + イベント登録）</title>
<style>
table {
	border-collapse: collapse;
	width: 90%;
	margin-top: 20px;
}

th, td {
	border: 1px solid #333;
	padding: 8px;
}

th {
	background: #eee;
}

h3 {
	margin-top: 40px;
}

.btn {
	padding: 5px 10px;
	background: #448aff;
	color: #fff;
	border-radius: 4px;
	text-decoration: none;
}

.btn-danger {
	background: #ff5252;
}

.input {
	padding: 5px;
}

.form-block {
	margin: 10px 0;
}
</style>
</head>
<body>

	<c:forEach var="dto" items="${companies}">
		<c:set var="company" value="${dto.company}" />

		<h2>${company.companyName}</h2>
		<p>企業ID：${company.companyId}</p>

		<hr />

		<!-- ============================
         卒業生一覧
         ============================ -->
		<h3>卒業生一覧</h3>

		<c:if test="${empty company.graduates}">
			<p>卒業生情報はありません。</p>
		</c:if>

		<c:if test="${not empty company.graduates}">
			<table>
				<tr>
					<th>卒業生番号</th>
					<th>氏名</th>
					<th>職種</th>
					<th>コース名</th>
					<th>学年</th>
					<th>スタッフ割当</th>
				</tr>

				<c:forEach var="g" items="${company.graduates}">
					<tr>
						<td>${g.graduateStudentNumber}</td>
						<td>${g.graduateName}</td>
						<td>${g.graduateJobCategory}</td>
						<td>${g.course.courseName}</td>
						<td>${g.course.courseTerm}</td>

						<!-- スタッフを割り当てる -->
						<td>
							<form action="graduate?command=AssignStaff" method="post">
								<input type="hidden" name="graduateStudentNumber"
									value="${g.graduateStudentNumber}" /> <select name="staffId"
									class="input">
									<c:forEach var="st" items="${dto.staffs}">
										<option value="${st.staffId}">${st.staffName}</option>
									</c:forEach>
								</select>

								<button type="submit" class="btn">割り当て</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>

		<hr />

		<!-- ============================
         Event 登録フォーム
         ============================ -->
		<h3>イベント登録</h3>

		<form action="event?command=RegistEvent" method="post">
			<input type="hidden" name="companyId" value="${company.companyId}" />

			<div class="form-block">
				<label>開催場所：</label> <input type="text" name="eventPlace"
					class="input" required />
			</div>

			<div class="form-block">
				<label>開始日時：</label> <input type="datetime-local"
					name="eventStartTime" class="input" required />
			</div>

			<div class="form-block">
				<label>終了日時：</label> <input type="datetime-local"
					name="eventEndTime" class="input" required />
			</div>

			<div class="form-block">
				<label>定員：</label> <input type="number" name="eventCapacity"
					class="input" required min="1" />
			</div>

			<div class="form-block">
				<label>備考：</label>
				<textarea name="eventOtherInfo" class="input" rows="3"></textarea>
			</div>

			<div class="form-block">
				<label>担当スタッフ：</label> <select name="staffId" class="input" required>
					<c:forEach var="st" items="${dto.staffs}">
						<option value="${st.staffId}">${st.staffName}</option>
					</c:forEach>
				</select>
			</div>


			<!-- ▼▼▼ ここから参加卒業生選択 ▼▼▼ -->
			<h4>参加させる卒業生を選択：</h4>

			<c:if test="${empty company.graduates}">
				<p>卒業生情報なし</p>
			</c:if>

			<c:if test="${not empty company.graduates}">
				<table>
					<tr>
						<th>選択</th>
						<th>卒業生番号</th>
						<th>氏名</th>
						<th>コース</th>
						<th>学年</th>
					</tr>

					<c:forEach var="g" items="${company.graduates}">
						<tr>
							<td><input type="checkbox" name="graduateStudents"
								value="${g.graduateStudentNumber}"></td>
							<td>${g.graduateStudentNumber}</td>
							<td>${g.graduateName}</td>
							<td>${g.course.courseName}</td>
							<td>${g.course.courseTerm}</td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
			<!-- ▲▲▲ ここまで参加卒業生選択 ▲▲▲ -->

			<button type="submit" class="btn">イベント登録</button>
		</form>

	</c:forEach>

</body>
</html>
