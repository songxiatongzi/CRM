<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<%--用来展示用户编码修改字符集
    如果换成jsp文件需要
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%--模板--%>
    <base href="<%=basePath%>">
    <title>模板</title>
</head>
<body>

</body>
</html>
