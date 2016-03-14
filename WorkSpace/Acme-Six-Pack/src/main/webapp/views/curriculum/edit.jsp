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
<form:form action="curriculum/trainer/edit.do" modelAttribute="curriculum">
	<!-- Hidden Attributes -->
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="updateMoment" />
	
	<!-- Editable Attributes -->
	

	<acme:textbox code="curriculum.picture" path="picture" />

	<acme:textbox code="curriculum.statement" path="statement" />

	<acme:textbox code="curriculum.skills" path="skills" />
	
	<acme:textbox code="curriculum.likes" path="likes" />

	<acme:textbox code="curriculum.dislikes" path="dislikes" />
	
	
	<!-- Action buttons -->

	<acme:submit name="save" code="curriculum.save"/>

	<jstl:if test="${curriculum.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="curriculum.delete" />"
			onclick="return confirm('<spring:message code="curriculum.confirm.delete" />')" />&nbsp;
	</jstl:if>
	
	<acme:cancel code="curriculum.cancel" url="curriculum/trainer/list.do"/>

</form:form>