<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<!--<link rel="stylesheet" href="./css/appointment.css">-->
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>企業登録画面（職員）</title>
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
		<form action="company" method="post">
			<input type="hidden" name="companyId" value="${companyId}"> <input
				type="hidden" name="command" value="CompanyRegisterNext">
			<div class="left-screen">
				<span class="leftscreen-title">企業登録</span>

				<c:if test="${not empty error}">
					<p style="color: red">${error}</p>
				</c:if>

				<div class="company-register-flame">
					<table class="registscreen-table">
						<tr>
							<td>
								<div>企業名</div>
							</td>
							<td><input type="text" id="companyName" name="companyName"
								class="company-input" value="${companyName}"
								placeholder="${empty companyName ? '企業名を入力' : ''}"></td>
						</tr>
					</table>
					<p>(株)は使えません</p>

					<!-- 類似企業表示 -->
					<div id="similarCompanies">
						<c:if test="${not empty similarCompanies}">
							<ul>
								<c:forEach var="dto" items="${similarCompanies}">
									<li>${dto.company.companyName}</li>
								</c:forEach>
							</ul>
						</c:if>
					</div>

				</div>
			</div>

			<div class="bottom-btn-right buttom-btn-narrow">
				<button type="submit" name="command" value="CompanyRegisterNext">確認</button>
			</div>
		</form>

	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>


</body>
<script>
const input = document.getElementById('companyName'); // id を付けた
const similarDiv = document.getElementById('similarCompanies');

input.addEventListener('input', function() {
    const name = input.value.trim();
    if (!name) {
        similarDiv.innerHTML = "";
        return;
    }

    // JS 側で encodeURIComponent を呼ぶ
    fetch("company?command=AjaxSearchCompany&companyName=" + encodeURIComponent(name))
    .then(res => {
        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
        return res.json();
    })
    .then(data => {
        similarDiv.innerHTML = ""; 
        if (data.length === 0) {
            similarDiv.innerHTML = "<p>会社が見つかりません</p>";
            return;
        }
        const ul = document.createElement("ul");
        data.forEach(item => {
            const li = document.createElement("li");
            li.textContent = item.companyName;
            ul.appendChild(li);
        });
        similarDiv.appendChild(ul);
    })
    .catch(err => {
        console.error("AjaxSearchCompany エラー:", err);
    });
});
</script>

</html>