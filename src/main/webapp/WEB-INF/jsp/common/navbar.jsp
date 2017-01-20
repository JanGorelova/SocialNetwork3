<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--Localisation--%>
<fmt:message key="menu.logout" var="logout"/>

<c:set var="currentUser" value="${sessionScope.currentUser}"/>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <ul class="nav navbar-nav">

        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
               aria-expanded="false"><fmt:message key="menu.chooseLocale"/><span class="caret"></span></a>

            <%--<form id="profileForm" method="post">

                <input type="hidden" name="hiddenPhone" />
            </form>--%>

            <ul class="dropdown-menu">
                <li>
                    <a href="${pageContext.request.contextPath}/locale?language=ru_RU">Русский</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/locale?language=en_US">English</a>
                </li>
            </ul>
        </li>
        <c:if test="${not empty currentUser}">
            <li><a href="${pageContext.request.contextPath}/logout">${logout}</a></li>
        </c:if>
    </ul>
</nav>