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

<h3><spring:message code="room.gym"/>: <jstl:out value="${gym.name}" /></h3>

<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="rooms" requestURI="${requestURI}" id="row_Room">
	<!-- Action links -->

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="room/administrator/edit.do?roomId=${row_Room.id}"> <spring:message
					code="room.edit" />
			</a>
		</display:column>
	</security:authorize>

	<!-- Attributes -->
	<spring:message code="room.name" var="nameHeader" />
	<display:column title="${nameHeader}"
		sortable="true">
		<jstl:out value="${row_Room.name}"/>
	</display:column>

	<spring:message code="room.description" var="descriptionHeader" />
	<display:column title="${descriptionHeader}"
		sortable="false">
		<jstl:out value="${row_Room.description}"/>
	</display:column>

	<spring:message code="room.seats" var="seatsHeader" />
	<display:column title="${seatsHeader}"
		sortable="true">
		<jstl:out value="${row_Room.numberOfSeats}"/>
	</display:column>

	<spring:message code="room.pictures" var="picturesHeader" />
	<display:column title="${picturesHeader}"
		sortable="false" >
		<jstl:forEach items="${row_Room.pictures}" var="picture">
			<img src="${picture}" style="width:204px;height:128px;"/>
		</jstl:forEach>
	</display:column>

</display:table>

<br/>
<br/>

<!-- Action links -->
<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="room/administrator/create.do?gymId=${gym.id}"> <spring:message
				code="room.create" />
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
