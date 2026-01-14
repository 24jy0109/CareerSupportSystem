<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${not empty sessionScope.flashMessage}">

<style>
/* =========================
   Toast Base
   ========================= */
.flash-toast {
    position: fixed;
    top: 28px;
    right: 28px;

    min-width: 320px;
    max-width: 480px;

    padding: 18px 22px 18px 18px;
    border-radius: 8px;

    font-size: 15px;
    line-height: 1.5;

    background: #ffffff;
    color: #1f2937;

    box-shadow: 0 12px 28px rgba(0,0,0,0.22);

    opacity: 0;
    transform: translateX(48px) scale(0.94);
    transition:
        opacity 0.35s ease,
        transform 0.35s cubic-bezier(.22,1,.36,1);

    z-index: 9999;
    overflow: hidden;

    display: flex;
    align-items: flex-start;
    gap: 12px;
}

.flash-toast.show {
    opacity: 1;
    transform: translateX(0) scale(1);
}

/* =========================
   Icon
   ========================= */
.flash-icon {
    font-size: 20px;
    line-height: 1;
    margin-top: 2px;
}

/* =========================
   Accent
   ========================= */
.flash-success::before,
.flash-error::before {
    content: "";
    position: absolute;
    left: 0;
    top: 0;
    width: 6px;
    height: 100%;
}

.flash-success::before {
    background-color: #10b981;
}

.flash-error::before {
    background-color: #ef4444;
}

/* =========================
   Progress (width animation)
   ========================= */
.flash-progress {
    position: absolute;
    left: 0;
    bottom: 0;
    height: 4px;
    width: 100%;
    animation: flash-progress 3s linear forwards;
}

.flash-success .flash-progress {
    background: linear-gradient(
        to right,
        #10b981,
        rgba(16,185,129,0.3)
    );
}

.flash-error .flash-progress {
    background: linear-gradient(
        to right,
        #ef4444,
        rgba(239,68,68,0.3)
    );
}

@keyframes flash-progress {
    from { width: 100%; }
    to   { width: 0%; }
}

/* =========================
   Error Shake
   ========================= */
.flash-error.shake {
    animation: flash-shake 0.2s ease-in-out;
}

@keyframes flash-shake {
    0%   { transform: translateX(0); }
    25%  { transform: translateX(-6px); }
    50%  { transform: translateX(6px); }
    75%  { transform: translateX(-4px); }
    100% { transform: translateX(0); }
}
</style>

<script>
(function () {

    function flash_showToast(message, type) {

        const toast = document.createElement("div");
        toast.className =
            "flash-toast " +
            (type === "error" ? "flash-error" : "flash-success");

        const icon = document.createElement("div");
        icon.className = "flash-icon";
        icon.textContent = (type === "error") ? "⚠" : "✔";

        const text = document.createElement("div");
        text.textContent = message;

        const progress = document.createElement("div");
        progress.className = "flash-progress";

        toast.appendChild(icon);
        toast.appendChild(text);
        toast.appendChild(progress);

        document.body.appendChild(toast);

        setTimeout(function () {
            toast.classList.add("show");
            if (type === "error") {
                toast.classList.add("shake");
            }
        }, 10);

        setTimeout(function () {
            toast.classList.remove("show");
            setTimeout(function () {
                toast.remove();
            }, 350);
        }, 4000);
    }

    flash_showToast(
        "${fn:escapeXml(sessionScope.flashMessage)}",
        "${sessionScope.flashMessageType}"
    );

})();
</script>

<%-- FlashMessage は一度だけ表示 --%>
<c:remove var="flashMessage" scope="session" />
<c:remove var="flashMessageType" scope="session" />

</c:if>
