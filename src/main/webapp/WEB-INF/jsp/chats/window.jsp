<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="text"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="${sessionScope.language}">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Template</title>

    <jsp:include page="${contextPath}/WEB-INF/jsp/common/css.jsp"/>

</head>
<body>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/navbar.jsp"/>
<div class="container" role="main">
    <div class="row">
        <jsp:include page="${contextPath}/WEB-INF/jsp/common/navigation.jsp"/>
        <div class="col-xs-10 col-md-10 col-lg-10">

            <jsp:useBean id="messageList" type="java.util.List" scope="request"/>
            <c:set var="limit" value="${requestScope.limit}"/>
            <c:set var="offset" value="${requestScope.offset}"/>
            <c:set var="chat_id" value="${param.chat_id}"/>
            <c:if test="${offset>0}">
                <a href="${contextPath}/chat/window?chat_id=${chat_id}&offset=${offset-limit}&limit=${limit}">
                    <fmt:message key="previousPage"/></a> </c:if>
            <c:if test="${requestScope.hasNextPage}"><a
                    href="${contextPath}/chat/window?chat_id=${chat_id}&offset=${offset}&limit=${limit+10}">
                <fmt:message key="messages.showPrevious"/></a> </c:if>
            <div class="row">
                <div class="col-xs-12" id="chat">
                    <table class="table">
                        <thead>
                        <tr>
                            <th><fmt:message key="messages.sender"/></th>
                            <th><fmt:message key="messages.message"/></th>
                            <th><fmt:message key="messages.date"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="message" items="${messageList}">
                            <tr>
                                <td>
                                    <a href="${contextPath}/profile?id=${message.id}">${message.senderName}</a>
                                </td>
                                <td>${message.text}</td>
                                <td>${message.sendingTime}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${empty messageList}">
                        Сообщений в этом чате нет (c)КЭП
                    </c:if>
                </div>
                <%--Форма для отправки сообщения--%>
                <form action="${contextPath}/chat/message/new" method="post">
                    <div class="form-group">
                        <label for="message"><fmt:message key="messages.message"/>:</label>
                        <input type="hidden" class="form-control" name="chat_id" value="${param.chat_id}">
                        <input autofocus type="text" class="form-control" id="message" name="text">
                    </div>
                    <button type="submit" class="btn btn-success"><fmt:message key="messages.send"/></button>
                </form>
            </div>

        </div>
    </div>
</div>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/scripts.jsp"/>
</body>
</html>