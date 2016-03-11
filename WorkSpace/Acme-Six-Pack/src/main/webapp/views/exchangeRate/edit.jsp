<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access = "hasRole('ADMIN')">
	<!-- Form -->
	<form:form action="exchangeRate/administrator/edit.do" modelAttribute="exchangeRate">
		<!-- Hidden Attributes -->
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<!-- Editable Attributes -->
	<acme:textbox code="exchangeRate.name" path="name" />

	<acme:textbox code="exchangeRate.currency" path="currency" />

	<acme:textbox code="exchangeRate.rate" path="rate" />
	<br />
	
		<!-- Action buttons -->
		<jstl:if test="${exchangeRate.id == 0 }">
			<input type="submit" name="save" value="<spring:message code="exchangeRate.create" />" />&nbsp;	
		</jstl:if>
		<jstl:if test="${exchangeRate.id != 0 }">
			<input type="submit" name="save" value="<spring:message code="exchangeRate.save" />" />
			<input type="submit" name="delete" 
				value="<spring:message code="exchangeRate.delete" />" 
				onclick="return confirm('<spring:message code="exchangeRate.confirm.delete" />')" />&nbsp;
		</jstl:if>
	
		<input type="button" name="cancel"
			value="<spring:message code="exchangeRate.cancel" />"
			onclick="javascript: relativeRedir('exchangeRate/administrator/list.do');" />
		
	</form:form>

</security:authorize>
