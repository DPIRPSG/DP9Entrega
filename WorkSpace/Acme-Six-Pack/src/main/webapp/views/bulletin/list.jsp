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
<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="bulletins" requestURI="${requestURI}" id="row_Bulletin">
	<!-- Action links -->

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="bulletin/administrator/edit.do?roomId=${row_Bulletin.id}"> <spring:message
					code="bulletin.edit" />
			</a>
		</display:column>
	</security:authorize>

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

<!-- Action links -->
<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="bulletin/administrator/create.do"> <spring:message
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
