<%@ page contentType="text/html; charset=UTF-8"%>

<style>
#loading-overlay {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(255, 255, 255, 0.85);
	display: none;
	align-items: center;
	justify-content: center;
	z-index: 9999;
}

.loading-box {
	text-align: center;
	font-size: 16px;
	color: #333;
}

.loading-spinner {
	width: 40px;
	height: 40px;
	border: 4px solid #ccc;
	border-top: 4px solid #333;
	border-radius: 50%;
	animation: spin 1s linear infinite;
	margin: 0 auto 10px;
}

@keyframes spin {
	0% { transform: rotate(0deg); }
	100% { transform: rotate(360deg); }
}
</style>

<div id="loading-overlay">
	<div class="loading-box">
		<div class="loading-spinner"></div>
		<p>処理中です。しばらくお待ちください。</p>
	</div>
</div>

<script>
function showLoadingAndDisableSubmit(form) {
	// ローディング表示
	const overlay = document.getElementById("loading-overlay");
	if (overlay) overlay.style.display = "flex";

	// ★ 送信確定後に disabled にする（重要）
	setTimeout(() => {
		const buttons = form.querySelectorAll("button, input[type='submit']");
		buttons.forEach(btn => btn.disabled = true);
	}, 0);

	return true; // 送信は必ず通す
}
</script>
