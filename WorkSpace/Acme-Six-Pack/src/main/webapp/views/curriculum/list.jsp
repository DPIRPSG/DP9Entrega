<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('TRAINER')">

	<jstl:if test="${curriculum != null}">
		<strong><spring:message code="curriculum.updateMoment" />:</strong> <fmt:formatDate value="${curriculum.updateMoment}" pattern="yyyy-MM-dd" /><br>
		<jstl:choose>
		  <jstl:when test="${curriculum.picture == null || curriculum.picture == ''}">
		    <img src="${profilePicture}" style="width:204px;height:204px;"/><br>
		  </jstl:when>
		  <jstl:otherwise>
		    <img src="${curriculum.picture}" style="width:204px;height:204px;"/><br>
		  </jstl:otherwise>
		</jstl:choose>
		<strong><spring:message code="curriculum.statement" />:</strong> <jstl:out value="${curriculum.statement}"/><br>
		<strong><spring:message code="curriculum.skills" />:</strong><br>
	 	<jstl:forEach var="skill" items="${skills}" >
	        -<jstl:out value="${skill}"/><br>
	    </jstl:forEach>
	    <strong><spring:message code="curriculum.likes" />:</strong><br>
	 	<jstl:forEach var="like" items="${likes}" >
	        -<jstl:out value="${like}"/><br>
	    </jstl:forEach>
	    <strong><spring:message code="curriculum.dislikes" />:</strong><br>
	 	<jstl:forEach var="dislike" items="${dislikes}" >
	        -<jstl:out value="${dislike}"/><br>
	    </jstl:forEach>
	    <br>
	    <acme:link code="curriculum.edit" href="curriculum/trainer/edit.do"/>
	</jstl:if>
	<jstl:if test="${curriculum == null}">
		<spring:message code="curriculum.null" />
		<br>
		<acme:link code="curriculum.create" href="curriculum/trainer/create.do"/>
	</jstl:if>
	
	
</security:authorize>