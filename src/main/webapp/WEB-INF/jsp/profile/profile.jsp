<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="custom" uri="/WEB_INF/taglib.tld" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="text"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="user" type="com.exam.models.User" scope="request"/>
<jsp:useBean id="profile" type="com.exam.models.Profile" scope="request"/>
<jsp:useBean id="currentUser" type="com.exam.models.User" scope="session"/>
<jsp:useBean id="postList" type="java.util.List" scope="request"/>
<jsp:useBean id="userMap" type="java.util.Map" scope="request"/>
<jsp:useBean id="minAvatars" type="java.util.Map" scope="request"/>

<!DOCTYPE html>
<html lang="${sessionScope.language}">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title><fmt:message key="profile"/></title>

    <jsp:include page="${contextPath}/WEB-INF/jsp/common/css.jsp"/>

</head>
<body>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/navbar.jsp"/>
<div class="container" role="main">
    <div class="row">
        <jsp:include page="${contextPath}/WEB-INF/jsp/common/navigation.jsp"/>
        <div class="col-xs-10 col-md-10 col-lg-10">
            <div class="row">
                <div class="col-xs-3" id="avatar">
                    <div class="row">
                        <c:if test="${not empty requestScope.avatar}">
                            <img width="200" src="/files/${requestScope.avatar.link}"/>
                        </c:if>
                        <c:if test="${empty requestScope.avatar}">
                            <img src="${pageContext.request.contextPath}/static/img/default_ava.png">
                        </c:if>
                    </div>
                    <c:if test="${user.id==currentUser.id}">
                        <div class="row">
                            <a href="${contextPath}/profile/edit">
                                <button class="btn btn-success btn-block"><fmt:message key="profile.edit"/></button>
                            </a>
                        </div>
                    </c:if>
                    <c:if test="${user.id!=currentUser.id}">
                        <div class="row">
                            <a href="${contextPath}/chat/private?recipient_id=${user.id}"
                               class="btn btn-success btn-block">
                                <fmt:message key="friends.writeMessage"/></a>
                        </div>

                        <div class="row">
                            <c:set var="relationType" value="${requestScope.relationType}"/>
                            <c:choose>
                                <c:when test="${relationType==0}">
                                    <form action="${contextPath}/friends/request/send" method="POST" role="form">
                                        <div class="btn-group">
                                            <input type="hidden" name="user_id" value="${user.id}">
                                            <button type="submit" class="btn btn-default">
                                                <fmt:message key="friends.add"/>
                                            </button>
                                        </div>
                                    </form>
                                </c:when>
                                <c:when test="${relationType==1}">
                                    <form action="${contextPath}/friends/cancel" method="POST" role="form">
                                        <div class="btn-group">
                                            <input type="hidden" name="user_id" value="${user.id}">
                                            <button type="submit" class="btn btn-default">
                                                <fmt:message key="friends.cancelRequest"/>
                                            </button>
                                        </div>
                                    </form>
                                </c:when>
                                <c:when test="${relationType==2}">
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
                                </c:when>
                                <c:when test="${relationType==3}">
                                    <form action="${contextPath}/friends/cancel" method="POST" role="form">
                                        <div class="btn-group">
                                            <input type="hidden" name="user_id" value="${user.id}">
                                            <button type="submit" class="btn btn-default">
                                                <fmt:message key="friends.delete"/>
                                            </button>
                                        </div>
                                    </form>
                                </c:when>
                            </c:choose>
                        </div>
                    </c:if>
                </div>
                <div class="col-xs-9">
                    <div class="row">
                        <h2>${user.firstName} ${user.lastName}</h2>
                    </div>
                    <div>
                        <p><fmt:message key="profile.information"/>:</p>
                        <div class="row">
                            <div class="col-xs-6"><fmt:message key="profile.gender"/>:</div>
                            <div class="col-xs-6">
                                <c:if test="${user.gender==1}">
                                    <fmt:message key="profile.gender.male"/>
                                </c:if>
                                <c:if test="${user.gender==0}">
                                    <fmt:message key="profile.gender.female"/>
                                </c:if>
                            </div>
                        </div>
                        <c:if test="${not empty profile}">
                            <c:if test="${not empty profile.birthday}">
                                <div class="row">
                                    <div class="col-xs-6"><fmt:message key="profile.birthday"/>:</div>
                                    <div class="col-xs-6">${profile.birthday}</div>
                                </div>
                            </c:if>
                            <c:if test="${not empty profile.telephone}">
                                <div class="row">
                                    <div class="col-xs-6"><fmt:message key="profile.telephone"/>:</div>
                                    <div class="col-xs-6">${profile.telephone}</div>
                                </div>
                            </c:if>
                            <c:if test="${not empty profile.country}">
                                <div class="row">
                                    <div class="col-xs-6"><fmt:message key="profile.country"/>:</div>
                                    <div class="col-xs-6">${profile.country}</div>
                                </div>
                            </c:if>
                            <c:if test="${not empty profile.city}">
                                <div class="row">
                                    <div class="col-xs-6"><fmt:message key="profile.city"/>:</div>
                                    <div class="col-xs-6">${profile.city}</div>
                                </div>
                            </c:if>
                            <c:if test="${not empty profile.university}">
                                <div class="row">
                                    <div class="col-xs-6"><fmt:message key="profile.university"/>:</div>
                                    <div class="col-xs-6">${profile.university}</div>
                                </div>
                            </c:if>
                            <c:if test="${not empty profile.team}">
                                <div class="row">
                                    <div class="col-xs-6">Отряд:</div>
                                    <div class="col-xs-6">${profile.team}</div>
                                </div>
                            </c:if>

                            <c:if test="${not empty profile.position}">
                                <div class="row">
                                    <div class="col-xs-6"><fmt:message key="profile.position"/>:</div>
                                    <div class="col-xs-6">
                                            ${profile.position}
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${not empty profile.about}">
                                <div class="row">
                                    <div class="col-xs-6"><fmt:message key="profile.about"/>:</div>
                                    <div class="col-xs-6">
                                            ${profile.about}
                                    </div>
                                </div>
                            </c:if>
                        </c:if>
                    </div>
                </div>
            </div>
            <%--Посты на стене--%>
            <div class="row">
                <div class="col-xs-12" id="wall">
                    <h3><fmt:message key="profile.wall"/></h3>
                    <%--Форма для нового поста--%>
                    <div class="row">
                        <div class="col-xs-12">
                            <form action="${contextPath}/post/new" method="post">
                                <div class="form-group">
                                    <label for="message"><fmt:message key="messages.message"/>:</label>
                                    <input type="hidden" class="form-control" name="recipient" value="${user.id}">
                                    <input autofocus type="text" class="form-control" id="message" name="text">
                                </div>
                                <button type="submit" class="btn btn-success"><fmt:message
                                        key="messages.send"/></button>
                            </form>
                        </div>
                    </div>
                    <%--Проходимся по каждому посту--%>
                    <%--<c:forEach var="post" items="${postList}">
                        <c:set var="senderId" value="${post.sender}"/>
                        <div class="row">
                            <div class="col-xs-3">
                                <c:set var="avaLink" value="${minAvatars[senderId]}"/>
                                <c:if test="${not empty avaLink}"><img src="${contextPath}/files/${avaLink}"/></c:if>
                                <c:if test="${empty avaLink}"><img
                                        src="${contextPath}/static/img/default_ava_miсro.png"/></c:if>
                                <br>
                                    &lt;%&ndash;Инициалы и ссылка на профиль автора поста&ndash;%&gt;
                                <a href="${contextPath}/profile?id=${userMap[senderId].id}"
                                >${userMap[senderId].firstName} ${userMap[senderId].lastName}</a>
                            </div>
                            <div class="col-xs-9">
                                <p>${post.message}</p><br>
                                Время отправки: <custom:format time="${post.time}"/>
                            </div>
                        </div>
                    </c:forEach>--%>
                    <table class="table table-condensed">
                        <thead>
                        <tr>
                            <th><fmt:message key="post.author"/></th>
                            <th><fmt:message key="post.text"/></th>
                            <th><fmt:message key="post.time"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="post" items="${postList}">
                            <c:set var="senderId" value="${post.sender}"/>
                            <tr>
                                <td>
                                    <c:set var="avaLink" value="${minAvatars[senderId]}"/>
                                    <c:if test="${not empty avaLink}"><img
                                            src="${contextPath}/files/${avaLink}"/></c:if>
                                    <c:if test="${empty avaLink}"><img
                                            src="${contextPath}/static/img/default_ava_miсro.png"/></c:if>
                                    <br>
                                        <%--Инициалы и ссылка на профиль автора поста--%>
                                    <a href="${contextPath}/profile?id=${userMap[senderId].id}"
                                    >${userMap[senderId].firstName} ${userMap[senderId].lastName}</a>
                                </td>
                                <td>
                                    <p>${post.message}</p><br>
                                    <c:if test="${currentUser.id==post.sender||currentUser.id==post.recipient}">
                                        <form action="${contextPath}/post/delete" method="post">
                                            <input type="hidden" name="post_id" value="${post.id}">
                                            <input type="submit" value="<fmt:message key="post.delete"/>">
                                        </form>
                                    </c:if>
                                </td>
                                <td><custom:format time="${post.time}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <%--Сссылки для перехода на предыдущую и следующую страницу--%>
                    <c:if test="${requestScope.offset>0}"><a
                            href="${contextPath}/profile?id=${user.id}&offset=${requestScope.offset-requestScope.limit}&limit=${requestScope.limit}">
                        <fmt:message key="previousPage"/></a> </c:if>
                    <c:if test="${requestScope.hasNextPage}"><a
                            href="${contextPath}/profile?id=${user.id}&offset=${requestScope.offset+requestScope.limit}&limit=${requestScope.limit}">
                        <fmt:message key="nextPage"/></a></c:if>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/scripts.jsp"/>
</body>