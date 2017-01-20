<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="text"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<%--Localisation--%>
<fmt:message key="login.signin" var="signin"/>
<fmt:message key="error.danger" var="danger"/>
<fmt:message key="password" var="passwordLabel"/>
<fmt:message key="login.signin.btn" var="signinBtn"/>

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
                <div class="col-xs-12 col-md-8 col-lg-6 center-block">
                    <h1 class="text-center login-title">${signin}</h1>
                    <c:if test="${not empty errorMsg}">
                        <div class="alert alert-danger">
                            <strong>${danger}</strong> <fmt:message key="${errorMsg}"/>
                        </div>
                    </c:if>
                    <div class="account-wall">
                        <img class="profile-img center-block" src="${contextPath}/static/img/login/photo.png" alt="">

                        <form class="form-signin" action="${contextPath}/j_security_check"
                              method="post">
                            <input type="text" class="form-control" placeholder="Email" name="j_username"
                                   value="${param.j_username}" required autofocus>
                            <input type="password" class="form-control " placeholder="${passwordLabel}"
                                   name="j_password" required>
                            <button class="btn btn-lg btn-primary btn-block" type="submit">${signinBtn}</button>
                        </form>

                    </div>
                    <p class="text-center"><a href="${contextPath}/registration"
                                              class="new-account btn btn-success btn-block">
                        <fmt:message key="login.create"/></a></p>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/scripts.jsp"/>
</body>
</html>