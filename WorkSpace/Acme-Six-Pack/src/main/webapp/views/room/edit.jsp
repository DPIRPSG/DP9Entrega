<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Form -->
<form:form action="room/administrator/edit.do" modelAttribute="room">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="activities" />
	<jstl:if test="${room.id != 0}">
		<form:hidden path="gym"/>
	</jstl:if>
	
	<!-- Editable Attributes -->
	

	<acme:textbox code="room.name" path="name" />

	<acme:textarea code="room.description" path="description" />

	<acme:textbox code="room.seats" path="numberOfSeats" />
	
	<acme:textarea code="room.pictures" path="pictures" />
	
	<jstl:if test="${room.id == 0}">
		<acme:select items="${gyms}" itemLabel="name" code="room.gym" path="gym"/>
	</jstl:if>
	
	<!-- Action buttons -->

	<acme:submit name="save" code="room.save"/>	

	<jstl:if test="${room.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="room.delete" />"
			onclick="return confirm('<spring:message code="room.confirm.delete" />')" />&nbsp;
	</jstl:if>
	
	<acme:cancel code="room.cancel" url="room/administrator/list.do?gymId=${room.gym.id}"/>

</form:form>