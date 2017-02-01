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

            <jsp:useBean id="messageMap" type="java.util.Map" scope="request"/>
            <jsp:useBean id="chatsList" type="java.util.List" scope="request"/>
            <div class="row">
                <div class="col-xs-12" id="chat">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Тип</th>
                            <th><fmt:message key="messages.message"/></th>
                            <th><fmt:message key="messages.date"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="chat" items="${chatsList}">
                            <c:set var="status" value="warning"/>
                            <c:if test="${messageMap[chat.id].sendingTime.isAfter(chat.lastRead)}">
                                <c:set var="status" value="success"/>
                            </c:if>
                            <tr class="${status}">
                                <td>
                                    <c:if test="${not empty chat.name}">${chat.name}</c:if>
                                    <c:if test="${empty chat.name}">ЛС</c:if>
                                </td>
                                <td>
                                    <c:set var="shortMessage"
                                           value="${messageMap[chat.id].senderName}: ${messageMap[chat.id].text}"/>
                                    <a href="${contextPath}/chat/window?chat_id=${chat.id}">${shortMessage}</a>
                                </td>
                                <td>${chat.lastUpdate}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <c:if test="${requestScope.offset>0}"><a
                        href="${contextPath}/chat?offset=${requestScope.offset-requestScope.limit}&limit=${requestScope.limit}">
                    <fmt:message key="previousPage"/></a> </c:if>
                <c:if test="${requestScope.hasNextPage}"><a
                        href="${contextPath}/chat?offset=${requestScope.offset+requestScope.limit}&limit=${requestScope.limit}">
                    <fmt:message key="nextPage"/></a> </c:if>
                <c:if test="${empty chatsList}">
                    Вы ещё не общались ни с кем
                </c:if>
            </div>

        </div>
    </div>
</div>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/scripts.jsp"/>
</body>
</html>