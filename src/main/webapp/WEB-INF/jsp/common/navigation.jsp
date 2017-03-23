<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="text"/>
<fmt:message var="my_page" key="menu.mypage"/>
<fmt:message var="messages" key="menu.messages"/>
<fmt:message var="friends" key="menu.friends"/>

<div class="col-xs-2 col-md-2 col-lg-2">
    <c:if test="${not empty currentUser}">
        <ul class="nav nav-list">
            <li class="nav-header">Навигация</li>
            <li><a href="${pageContext.request.contextPath}/profile">${my_page}</a></li>
            <li><a href="${pageContext.request.contextPath}/chat">${messages}</a></li>
            <li><a href="${pageContext.request.contextPath}/friends">${friends}</a></li>
            <%--<li><a href="/url"><strike>Отряды</strike></a></li>--%>
        </ul>
    </c:if>
</div>