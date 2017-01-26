<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="text"/>

<div class="col-xs-2 col-md-2 col-lg-2">
    <c:if test="${not empty currentUser}">
        <ul class="nav nav-list">
            <li class="nav-header">Навигация</li>
            <li><a href="${pageContext.request.contextPath}/profile">Моя страница</a></li>
            <li><a href="/url">Сообщения</a></li>
            <li><a href="/url">Друзья</a></li>
            <li><a href="/url">Отряды</a></li>
            <li><a href="/url">Мероприятия</a></li>
            <li><a href="/url">Фотографии</a></li>
        </ul>
    </c:if>
</div>