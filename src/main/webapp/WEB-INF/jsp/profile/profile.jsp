<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="text"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="user" type="com.exam.models.User" scope="request"/>
<jsp:useBean id="profile" type="com.exam.models.Profile" scope="request"/>
<jsp:useBean id="currentUser" type="com.exam.models.User" scope="session"/>

<!DOCTYPE html>
<html lang="${sessionScope.language}">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Solar BIM</title>

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
                        Пикча будет здесь!
                    </div>
                    <c:if test="${user.id==currentUser.id}">
                        <div class="row">
                            <a href="<c:url value="/profile/edit"/>">
                                <button class="btn btn-success btn-block"><fmt:message key="profile.edit"/></button>
                            </a>
                        </div>
                    </c:if>
                    <c:if test="${user.id!=currentUser.id}">
                        <a href=/messages?recipient=${user.id}" class="btn btn-success btn-block">
                            <fmt:message key="friends.writeMessage"/></a><br>
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
                                <c:if test="${user.gender==0}">
                                    <fmt:message key="profile.gender.male"/>
                                </c:if>
                                <c:if test="${user.gender==1}">
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
        </div>
    </div>
</div>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/scripts.jsp"/>
</body>