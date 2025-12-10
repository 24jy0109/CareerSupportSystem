<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>イベント詳細（卒業生一覧 + イベント登録）</title>
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

<c:forEach var="dto" items="${events}">
    <!-- EventDTO の event -->
    <c:set var="event" value="${dto.event}" />

    <h2>イベントID：${event.eventId}</h2>
    <p>企業ID：${event.company.companyId}</p>

    <p>
        開始：${event.eventStartTime} <br/>
        終了：${event.eventEndTime} <br/>
        場所：${event.eventPlace} <br/>
        定員：${event.eventCapacity}
    </p>

    <hr />

    <!-- ============================
         卒業生一覧
         ============================ -->
    <h3>卒業生一覧</h3>

    <c:if test="${empty dto.graduates}">
        <p>卒業生情報はありません。</p>
    </c:if>

    <c:if test="${not empty dto.graduates}">
        <table>
            <tr>
                <th>卒業生番号</th>
                <th>氏名</th>
                <th>職種</th>
                <th>コース名</th>
                <th>学年</th>
                <th>スタッフ割当</th>
            </tr>

            <c:forEach var="g" items="${dto.graduates}">
                <tr>
                    <td>${g.graduateStudentNumber}</td>
                    <td>${g.graduateName}</td>
                    <td>${g.graduateJobCategory}</td>
                    <td>${g.course.courseName}</td>
                    <td>${g.course.courseTerm}</td>

                    <td>
                        <form action="graduate?command=AssignStaff" method="post">
                            <input type="hidden" name="graduateStudentNumber"
                                value="${g.graduateStudentNumber}" />
                            <input type="hidden" name="eventId" value="${event.eventId}" />

                            <select name="staffId" class="input">
                                <option value=""
                                    <c:if test="${empty g.staff}">selected</c:if>>
                                    （未割当）
                                </option>

                                <c:forEach var="st" items="${dto.staffs}">
                                    <option value="${st.staffId}"
                                        <c:if test="${g.staff.staffId == st.staffId}">selected</c:if>>
                                        ${st.staffName}
                                    </option>
                                </c:forEach>
                            </select>

                            <button type="submit" class="btn">割り当て</button>

                            <a href="event?command=ScheduleArrangeSendForm&graduateStudentNumber=${g.graduateStudentNumber}">
                                開催相談
                            </a>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <hr />

    <!-- ============================
         イベント登録フォーム
         ============================ -->
    <h3>イベント登録</h3>

    <form action="event?command=RegistEvent" method="post">

        <input type="hidden" name="companyId" value="${event.company.companyId}" />

        <div class="form-block">
            <label>開催場所：</label>
            <input type="text" name="eventPlace" class="input" required />
        </div>

        <div class="form-block">
            <label>開始日時：</label>
            <input type="datetime-local" name="eventStartTime" class="input" required />
        </div>

        <div class="form-block">
            <label>終了日時：</label>
            <input type="datetime-local" name="eventEndTime" class="input" required />
        </div>

        <div class="form-block">
            <label>定員：</label>
            <input type="number" name="eventCapacity" class="input" required min="1" />
        </div>

        <div class="form-block">
            <label>備考：</label>
            <textarea name="eventOtherInfo" class="input" rows="3"></textarea>
        </div>

        <div class="form-block">
            <label>担当スタッフ：</label>
            <select name="staffId" class="input" required>
                <c:forEach var="st" items="${dto.staffs}">
                    <option value="${st.staffId}">${st.staffName}</option>
                </c:forEach>
            </select>
        </div>

        <!-- 卒業生選択 -->
        <h4>参加させる卒業生を選択：</h4>

        <c:if test="${empty dto.graduates}">
            <p>卒業生情報なし</p>
        </c:if>

        <c:if test="${not empty dto.graduates}">
            <table>
                <tr>
                    <th>選択</th>
                    <th>卒業生番号</th>
                    <th>氏名</th>
                    <th>コース</th>
                    <th>学年</th>
                </tr>

                <c:forEach var="g" items="${dto.graduates}">
                    <tr>
                        <td><input type="checkbox" name="graduateStudents"
                            value="${g.graduateStudentNumber}" /></td>
                        <td>${g.graduateStudentNumber}</td>
                        <td>${g.graduateName}</td>
                        <td>${g.course.courseName}</td>
                        <td>${g.course.courseTerm}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>

        <button type="submit" class="btn">イベント登録</button>
    </form>

</c:forEach>

</body>
</html>
