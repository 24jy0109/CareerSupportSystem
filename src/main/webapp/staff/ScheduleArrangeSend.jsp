<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<title>メール作成（開催相談）</title>
<style>
body { font-family: sans-serif; padding: 20px; }
.form-block { margin: 12px 0; }
.input, textarea, select { padding: 6px; width: 300px; }
textarea { width: 520px; height: 200px; }
.btn { padding: 8px 15px; background: #448aff; color: #fff; border-radius: 4px; border: none; }
</style>
</head>
<body>

<!-- ▼ List<EventDTO> の 1 件目を取り出す -->
<c:set var="dto" value="${events[0]}" />
<c:set var="g" value="${dto.graduates[0]}" />

<h2>日程調整メール作成</h2>

<p>卒業生番号：<strong>${g.graduateStudentNumber}</strong></p>
<p>氏名：<strong>${g.graduateName}</strong></p>

<hr>

<form action="event?command=SendScheduleArrangeEmail" method="post">

    <!-- ▼ Hidden：卒業生番号 -->
    <input type="hidden" name="graduateStudentNumber" value="${g.graduateStudentNumber}" />

    <!-- ▼ 宛先 -->
    <div class="form-block">
        <label>宛先メールアドレス：</label><br>
        <input type="email" name="toEmail" class="input"
               value="${g.graduateEmail}" required />
    </div>

    <!-- ▼ 件名 -->
    <div class="form-block">
        <label>件名：</label><br>
        <input type="text" name="subject" class="input"
               value="【日程調整】企業訪問の候補日について" required />
    </div>

    <!-- ▼ 本文 -->
    <div class="form-block">
        <label>本文：</label><br>
        <textarea name="body" required>
${g.graduateName} さん

お世話になっております。
以下の日程候補の中から、ご都合の良い日時をお知らせください。

・第1候補：
・第2候補：
・第3候補：

どうぞよろしくお願いいたします。
        </textarea>
    </div>

    <!-- ▼ 担当スタッフ -->
    <div class="form-block">
        <label>担当スタッフ：</label><br>
        <select name="staffId" class="input" required>
            <c:forEach var="st" items="${dto.staffs}">
                <option value="${st.staffId}">
                    ${st.staffName}
                </option>
            </c:forEach>
        </select>
    </div>

    <button type="submit" class="btn">確認画面へ</button>
</form>

</body>
</html>
