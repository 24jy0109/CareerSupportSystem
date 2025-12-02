<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>マイページ</title>
    <style>
        table {
            border-collapse: collapse;
            width: 50%;
        }
        th, td {
            border: 1px solid #666;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #ddd;
        }
    </style>
</head>
<body>
	<h1>マイページ（職員）</h1>
	<a href="mypage?command=AppointmentMenu">アポイントメント画面</a>
    <h2>セッションの中身一覧</h2>

<%
    if (session != null) {
        java.util.Enumeration<String> attributeNames = session.getAttributeNames();

        if (attributeNames.hasMoreElements()) {
%>
            <table>
                <tr>
                    <th>属性名</th>
                    <th>値</th>
                </tr>
<%
            while (attributeNames.hasMoreElements()) {
                String name = attributeNames.nextElement();
                Object value = session.getAttribute(name);
%>
                <tr>
                    <td><%= name %></td>
                    <td><%= value %></td>
                </tr>
<%
            } // while
%>
            </table>
<%
        } else {
%>
            <p>セッションには属性が設定されていません。</p>
<%
        }
    } else {
%>
    <p>セッションが存在しません。</p>
<%
    }
%>

</body>
</html>
