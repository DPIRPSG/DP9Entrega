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
<form:form action="activity/administrator/edit.do" modelAttribute="activity">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<form:hidden path="deleted"/>
	
	<form:hidden path="customers" />
	<form:hidden path="service"/>
	
	<!-- Editable Attributes -->
	
	<acme:textbox code="activity.title" path="title"/>
	<acme:textbox code="activity.numberOfSeatsAvailable" path="numberOfSeatsAvailable"/>
	<acme:textbox code="activity.startingMoment" path="startingMoment"/>
	<acme:textbox code="activity.duration" path="duration"/>
	<acme:textarea code="activity.description" path="description"/>
	<acme:textarea code="activity.pictures" path="pictures"/>
			
	<acme:select items="${rooms}" itemLabel="name" code="activity.room" path="room"/>
	<acme:select items="${trainers}" itemLabel="name" code="activity.trainer" path="trainer"/>
	
	<!-- Action buttons -->
	
	
	
	<acme:submit name="save" code="activity.save"/>
	
	<acme:cancel code="room.cancel" url="activity/administrator/list.do"/>

</form:form>