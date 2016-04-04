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
<form:form action="activity/administrator/create.do" modelAttribute="activity">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<form:hidden path="title"/>
	<form:hidden path="numberOfSeatsAvailable"/>
	<form:hidden path="startingMoment"/>
	<form:hidden path="duration"/>
	<form:hidden path="description"/>
	<form:hidden path="pictures"/>
	<form:hidden path="deleted"/>
	
	<form:hidden path="customers" />
	<form:hidden path="room"/>
	<form:hidden path="trainer"/>
	
	<!-- Editable Attributes -->
			
	<acme:select items="${services}" itemLabel="name" code="activity.service" path="service"/>
	
	<!-- Action buttons -->
	
	
	
	<acme:submit name="save" code="activity.save"/>
	
	<acme:cancel code="activity.cancel" url="activity/administrator/list.do"/>

</form:form>