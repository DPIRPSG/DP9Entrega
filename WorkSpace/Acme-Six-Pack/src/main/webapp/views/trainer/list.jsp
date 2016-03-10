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

<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="trainers" requestURI="${requestURI}" id="row_Trainer">
	<!-- Action links -->

	<!-- Attributes -->
	<spring:message code="trainer.name" var="nameHeader" />
	<display:column title="${nameHeader}"
		sortable="true">
		<jstl:out value="${row_Trainer.name}"/>
	</display:column>

	<spring:message code="trainer.surname" var="surnameHeader" />
	<display:column title="${surnameHeader}"
		sortable="false">
		<jstl:out value="${row_Trainer.surname}"/>
	</display:column>
	
	<spring:message code="trainer.picture" var="pictureHeader" />	
	<display:column title="${pictureHeader}"
		sortable="true" >
		<img src="${row_Trainer.picture}" style="height:128px;"/>
	</display:column>

	<display:column>
		<a href="trainer/specialities.do?trainerId=${row_Trainer.id}"> <spring:message
				code="trainer.specialities" />
		</a>
	</display:column>

</display:table>

<br/>
<br/>


<!-- Action links -->


<!-- Alert -->
<jstl:if test="${messageStatus != Null && messageStatus != ''}">
	<spring:message code="${messageStatus}" var="showAlert" />
			<script>$(document).ready(function(){
		    alert("${showAlert}");
		  });
		</script>
</jstl:if>	
