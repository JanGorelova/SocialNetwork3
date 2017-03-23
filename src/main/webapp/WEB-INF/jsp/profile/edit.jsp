<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="text"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="profile" type="com.exam.models.Profile" scope="request"/>
<jsp:useBean id="currentUser" type="com.exam.models.User" scope="session"/>
<jsp:useBean id="errorMsg" class="java.lang.String" scope="request"/>
<jsp:useBean id="successMsg" class="java.lang.String" scope="request"/>

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
                <div class="col-xs-6">
                    <div class="row">
                        <h2>${currentUser.firstName} ${currentUser.lastName}</h2>
                    </div>
                    <div>
                        <c:if test="${not empty errorMsg}">
                            <div class="alert alert-danger">
                                <strong><fmt:message key="error.danger"/></strong> <fmt:message key="${errorMsg}"/>
                            </div>
                        </c:if>
                        <c:if test="${not empty successMsg}">
                            <div class="alert alert-success">
                                <strong><fmt:message key="success"/></strong> <fmt:message key="success.changes"/>
                            </div>
                        </c:if>
                        <form class="form-horizontal" action="${pageContext.request.contextPath}/profile/edit"
                              method="POST">
                            <fieldset>
                                <div class="row">
                                    <div class="control-group col-xs-12">
                                        <label class="control-label" for="telephone"><fmt:message
                                                key="profile.telephone"/></label>
                                        <div class="controls">
                                            <input type="tel" id="telephone" name="telephone" placeholder=""
                                                   class="input-xlarge"
                                                   value="${profile.telephone}">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="control-group col-xs-12">
                                        <label class="control-label" for="birthday"><fmt:message
                                                key="profile.birthday"/></label>
                                        <div class="controls">
                                            <input type="date" id="birthday" name="birthday" placeholder=""
                                                   class="input-xlarge"
                                                   value="${profile.birthday}">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="control-group col-xs-12">
                                        <label class="control-label" for="country"><fmt:message
                                                key="profile.country"/></label>
                                        <div class="controls">
                                            <input type="text" id="country" name="country" placeholder=""
                                                   class="input-xlarge"
                                                   value="${profile.country}">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="control-group col-xs-12">
                                        <label class="control-label" for="city"><fmt:message
                                                key="profile.city"/></label>
                                        <div class="controls">
                                            <input type="text" id="city" name="city" placeholder="" class="input-xlarge"
                                                   value="${profile.city}">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="control-group col-xs-12">
                                        <label class="control-label" for="university"><fmt:message
                                                key="profile.university"/></label>
                                        <div class="controls">
                                            <input type="text" id="university" name="university" placeholder=""
                                                   class="input-xlarge"
                                                   value="${profile.university}">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="control-group col-xs-12">
                                        <label class="control-label" for="team"><fmt:message
                                                key="profile.team"/></label>
                                        <select id="team" name="team">
                                            <c:set var="userTeam" value="${profile.team}"/>
                                            <c:forEach items="${requestScope.team_list}" var="item">
                                                <option value="${item.name}" <c:if test="${userTeam==item.name}">selected</c:if>
                                                ><c:out value="${item.name}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="control-group col-xs-12">
                                        <label class="control-label" for="position"><fmt:message
                                                key="profile.position"/></label>
                                        <input type="text" id="position" name="position" placeholder=""
                                               class="input-xlarge"
                                               value="${profile.position}">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="control-group col-xs-12">
                                        <label class="control-label" for="about"><fmt:message
                                                key="profile.about"/></label>
                                        <div class="controls">
                                <textarea id="about" name="about" rows="6" cols="51"
                                          placeholder="<fmt:message key="profile.writeAbout"/>">${profile.about}</textarea>
                                        </div>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <!-- Button -->
                                    <div class="controls">
                                        <button class="btn btn-success"><fmt:message key="profile.save"/></button>
                                    </div>
                                </div>
                            </fieldset>
                        </form>
                        <h3><fmt:message key="profile.avaUpload"/></h3>
                        <fmt:message key="profile.selectPhoto"/>: <br/>
                        <form action="${pageContext.request.contextPath}/upload/avatar" method="post"
                              enctype="multipart/form-data">
                            <input type="file" name="file" size="50" accept="image/jpeg"/>
                            <br/>
                            <input type="submit" value="<fmt:message key="profile.upload"/>"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${contextPath}/WEB-INF/jsp/common/scripts.jsp"/>
</body>
</html>