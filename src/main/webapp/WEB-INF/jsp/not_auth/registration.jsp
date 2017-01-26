<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="text"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<fmt:message key="error.danger" var="danger"/>
<fmt:message key="${errorMsg}" var="errorMsg"/>
<fmt:message key="title.registration" var="legend"/>
<fmt:message key="regform.fname" var="firstNameLabel"/>
<fmt:message key="regform.fname.hint" var="firstNameHint"/>
<fmt:message key="regform.lname" var="lastNameLabel"/>
<fmt:message key="regform.lname" var="lastNameHint"/>
<fmt:message key="regform.email.hint" var="emailHint"/>
<fmt:message key="regform.gender" var="genderLabel"/>
<fmt:message key="regform.male" var="maleOption"/>
<fmt:message key="regform.female" var="femaleOption"/>

<!DOCTYPE html>
<html lang="${sessionScope.language}">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Регистрация</title>

    <jsp:include page="${contextPath}/WEB-INF/jsp/common/css.jsp"/>

</head>
<body>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/navbar.jsp"/>
<div class="container" role="main">
    <div class="row">
        <jsp:include page="${contextPath}/WEB-INF/jsp/common/navigation.jsp"/>
        <div class="col-xs-10 col-md-10 col-lg-10">

            <div class="row">
                <div class="col-xs-12">
                    <form class="form-horizontal"
                          action="${pageContext.request.contextPath}/not_auth/registration"
                          method="POST">
                        <fieldset>
                            <div id="legend">
                                <legend class="area-legend-symbol">${legend}</legend>
                            </div>
                            <c:if test="${not empty errorMsg}">
                                <div class="alert alert-danger">
                                    <strong>${danger}</strong> ${errorMsg}
                                </div>
                            </c:if>
                            <div class="control-group">
                                <!-- FirstName -->
                                <label class="control-label" for="first_name">${firstNameLabel}</label>
                                <div class="controls">
                                    <input type="text" id="first_name" name="first_name" placeholder=""
                                           class="input-xlarge"
                                           value="${param.first_name}">
                                    <p class="help-block">${firstNameHint}</p>
                                </div>
                            </div>

                            <div class="control-group">
                                <!-- LastName -->
                                <label class="control-label" for="last_name">${lastNameLabel}</label>
                                <div class="controls">
                                    <input type="text" id="last_name" name="last_name" placeholder=""
                                           class="input-xlarge"
                                           value="${param.last_name}">
                                    <p class="help-block">${lastNameHint}</p>
                                </div>
                            </div>

                            <div class="control-group">
                                <!-- E-mail -->
                                <label class="control-label" for="email">E-mail</label>
                                <div class="controls">
                                    <input type="text" id="email" name="email" placeholder="" class="input-xlarge"
                                           value="${param.email}">
                                    <p class=" help-block">${emailHint}</p>
                                </div>
                            </div>

                            <div class="control-group">
                                <label class="control-label" for="gender">${genderLabel}</label>
                                <select id="gender" name="gender" class="form-control input-xlarge">
                                    <option
                                            <c:if test="${param.gender=='male'}">selected</c:if> value="male">
                                        ${maleOption}
                                    </option>
                                    <option
                                            <c:if test="${param.gender=='female'}">selected</c:if> value="female">
                                        ${femaleOption}
                                    </option>
                                </select>
                            </div>

                            <div class="control-group">
                                <!-- Password-->
                                <label class="control-label" for="password"><fmt:message
                                        key="regform.password"/></label>
                                <div class="controls">
                                    <input type="password" id="password" name="password" placeholder=""
                                           class="input-xlarge">
                                    <p class="help-block"><fmt:message key="regform.password.hint"/></p>
                                </div>
                            </div>

                            <div class="control-group">
                                <!-- Password -->
                                <label class="control-label" for="password_confirm"><fmt:message
                                        key="regform.password.confirm"/></label>
                                <div class="controls">
                                    <input type="password" id="password_confirm" name="password_confirm" placeholder=""
                                           class="input-xlarge">
                                    <p class="help-block"><fmt:message key="regform.password.confirm.hint"/></p>
                                </div>
                            </div>

                            <div class="control-group">
                                <!-- Button -->
                                <div class="controls">
                                    <button class="btn btn-success"><fmt:message key="regform.register"/></button>
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/scripts.jsp"/>
</body>
</html>