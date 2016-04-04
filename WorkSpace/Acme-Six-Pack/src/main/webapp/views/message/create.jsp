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
<form:form action="message/actor/create.do" modelAttribute="messa">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="sentMoment" />
	<form:hidden path="sender" />
	<form:hidden path="folders" />
	
	<spring:message code="message.notBlank" var="notBlank"/>
	<spring:message code="message.notEmpty" var="notEmpty"/>

	<form:label path="recipients">
		<spring:message code="message.recipients" />:
	</form:label>
	<form:select id="recipients" path="recipients" items="${actors}"
		itemLabel="userAccount.username" itemValue="id" multiple="multiple" />
	<form:errors cssClass="error" path="recipients" />
	<jstl:if test="${!hayRecipients }">
		<div class="error"><jstl:out value="${notEmpty }"></jstl:out></div>
	</jstl:if>
	<br />
	
	<form:label path="subject">
		<spring:message code="message.subject" />:
	</form:label>
	<form:input path="subject"/>
	<form:errors cssClass="error" path="subject" />
	<jstl:if test="${!haySubject }">
		<div class="error"><jstl:out value="${notBlank }"></jstl:out></div>
	</jstl:if>
	<br />
	
	<form:label path="body">
		<spring:message code="message.body" />:
	</form:label>
	<form:textarea path="body"/>
	<form:errors cssClass="error" path="body" />
	<jstl:if test="${!hayBody }">
		<div class="error"><jstl:out value="${notBlank }"></jstl:out></div>
	</jstl:if>
	<br />
	
	<br />

	<!-- Action buttons -->
	<acme:submit name="send" code="message.save"/>
	&nbsp; 
	<acme:cancel url="folder/actor/list.do" code="message.cancel"/>
	<br />

</form:form>