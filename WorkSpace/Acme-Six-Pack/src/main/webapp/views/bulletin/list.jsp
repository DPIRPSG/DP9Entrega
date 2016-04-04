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

<h3><spring:message code="bulletin.gym"/>: <jstl:out value="${gym.name}" /></h3>

<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="bulletins" requestURI="${requestURI}" id="row_Bulletin">
	<!-- Action links -->

	<!-- Attributes -->
	<spring:message code="bulletin.title" var="titleHeader" />
	<display:column title="${titleHeader}"
		sortable="true">
		<jstl:out value="${row_Bulletin.title}"/>
	</display:column>

	<spring:message code="bulletin.description" var="descriptionHeader" />
	<display:column title="${descriptionHeader}"
		sortable="false">
		<jstl:out value="${row_Bulletin.description}"/>
	</display:column>

	<spring:message code="bulletin.publishMoment" var="publishMomentHeader" />
	<display:column title="${publishMomentHeader}"
		sortable="true">
		<jstl:out value="${row_Bulletin.publishMoment}"/>
	</display:column>

</display:table>

<br/>
<br/>

<form action="${requestURI}">
	<input type="hidden" name="gymId" value="${gym.id}">
	<input type="text" name="keyword"> <input type="submit"
		value="<spring:message code="bulletin.search" />" />&nbsp;
</form>

<!-- Action links -->
<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="bulletin/administrator/create.do?gymId=${gym.id}"> <spring:message
				code="bulletin.create" />
		</a>
	</div>
</security:authorize>

<!-- Alert -->
<jstl:if test="${messageStatus != Null && messageStatus != ''}">
	<spring:message code="${messageStatus}" var="showAlert" />
			<script>$(document).ready(function(){
		    alert("${showAlert}");
		  });
		</script>
</jstl:if>	
