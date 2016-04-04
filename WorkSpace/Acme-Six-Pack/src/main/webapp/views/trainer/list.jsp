<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="false"
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
		sortable="true">
		<jstl:out value="${row_Trainer.surname}"/>
	</display:column>
	
	<spring:message code="trainer.picture" var="pictureHeader" />	
	<display:column title="${pictureHeader}"
		sortable="false" >
		<img src="${row_Trainer.picture}" style="height:128px;"/>
	</display:column>
	
	<spring:message code="trainer.statement" var="statementHeader" />
	<display:column title="${statementHeader}"
		sortable="false">
		<jstl:out value="${row_Trainer.curriculum.statement}"/>
	</display:column>
	
	<spring:message code="trainer.skills" var="skillsHeader" />
	<display:column title="${skillsHeader}"
		sortable="false">
		<jstl:forEach var="skill" items="${row_Trainer.curriculum.skills}" >
	        -<jstl:out value="${skill}"/><br>
	    </jstl:forEach>
	</display:column>
	
	<spring:message code="trainer.likes" var="likesHeader" />
	<display:column title="${likesHeader}"
		sortable="false">
		<jstl:forEach var="like" items="${row_Trainer.curriculum.likes}" >
	        -<jstl:out value="${like}"/><br>
	    </jstl:forEach>
	</display:column>
	
	<spring:message code="trainer.dislikes" var="dislikesHeader" />
	<display:column title="${dislikesHeader}"
		sortable="false">
		<jstl:forEach var="dislike" items="${row_Trainer.curriculum.dislikes}" >
	        -<jstl:out value="${dislike}"/><br>
	    </jstl:forEach>
	</display:column>

	<display:column>
		<a href="trainer/specialities.do?trainerId=${row_Trainer.id}"> <spring:message
				code="trainer.specialities" />
		</a>
	</display:column>
	
	<display:column>
		<a href="comment/list.do?commentedEntityId=${row_Trainer.id}"> <spring:message
				code="trainer.comments" />
		</a>
	</display:column>

</display:table>

<br/>
<br/>

<form action="${requestURI}">
	<input type="text" name="keyword"> <input type="submit"
		value="<spring:message code="trainer.search" />" />&nbsp;
</form>


<!-- Action links -->


<!-- Alert -->
<jstl:if test="${messageStatus != Null && messageStatus != ''}">
	<spring:message code="${messageStatus}" var="showAlert" />
			<script>$(document).ready(function(){
		    alert("${showAlert}");
		  });
		</script>
</jstl:if>	
