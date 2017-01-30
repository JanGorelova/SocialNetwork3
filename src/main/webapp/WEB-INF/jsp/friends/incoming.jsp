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

    <title>Friends</title>

    <jsp:include page="${contextPath}/WEB-INF/jsp/common/css.jsp"/>

</head>
<body>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/navbar.jsp"/>
<div class="container" role="main">
    <div class="row">
        <jsp:include page="${contextPath}/WEB-INF/jsp/common/navigation.jsp"/>
        <div class="col-xs-10 col-md-10 col-lg-10">
            <div class="row">
                <h2><a href="${contextPath}/friends/search">Поиск людей</a></h2>
            </div>
            <div class="row">
                <div class="col-xs-12" id="friends">
                    <c:forEach var="user" items="${requestScope.userList}">
                        <div class="row">
                            <div class="col-xs-3">
                                <a href="${contextPath}/profile?id=${user.id}">Пикча</a>
                            </div>
                            <div class="col-xs-6">
                                <p style="font-size: 2em"><a
                                        href="${contextPath}/profile?id=${user.id}">${user.firstName} ${user.lastName}</a>
                                </p>
                            </div>
                            <div class="col-xs-3">
                                <a href="${contextPath}/messages?recipient=${user.id}"
                                   class="btn btn-success btn-block"><fmt:message key="friends.writeMessage"/></a><br>
                                <a href="/id${user.id}" class="btn btn-info btn-block"><fmt:message
                                        key="friends.goToPage"/></a><br>
                                <form action="${contextPath}/friends/incoming/accept" method="POST" role="form">
                                    <div class="btn-group">
                                        <input type="hidden" name="user_id" value="${user.id}">
                                        <button type="submit" class="btn btn-default">
                                            <fmt:message key="friends.acceptIncoming"/>
                                        </button>
                                    </div>
                                </form>
                                <form action="${contextPath}/friends/cancel" method="POST" role="form">
                                    <div class="btn-group">
                                        <input type="hidden" name="user_id" value="${user.id}">
                                        <button type="submit" class="btn btn-default">
                                            <fmt:message key="friends.cancelIncoming"/>
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${requestScope.offset>0}"><a
                            href="${contextPath}/friends?offset=${requestScope.offset-requestScope.limit}&limit=${requestScope.limit}">
                        <fmt:message key="previousPage"/></a> </c:if>
                    <c:if test="${requestScope.hasNextPage}"><a
                            href="${contextPath}/friends?offset=${requestScope.offset+requestScope.limit}&limit=${requestScope.limit}">
                        <fmt:message key="nextPage"/></a> </c:if>
                    <c:if test="${empty requestScope.userList}">
                        Входящие заявки отсутствуют!
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/scripts.jsp"/>
</body>
</html>