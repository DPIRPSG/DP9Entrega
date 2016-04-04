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
<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="activities" requestURI="${requestURI}" id="row_Activity">
	<!-- Action links -->

	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="activity.edit" var="editHeader"/>
		<display:column title="${editHeader}" sortable="true">
		<jstl:if test="${row_Activity.deleted == false}">
			<jstl:if test="${row_Activity.customers.size() != 0}">
				<a href="activity/administrator/edit.do?activityId=${row_Activity.id}"> <spring:message
					code="activity.edit" />
				</a>
			</jstl:if>
			<jstl:if test="${row_Activity.customers.size() == 0}">
				<a href="activity/administrator/create.do?activityId=${row_Activity.id}"> <spring:message
					code="activity.edit" />
				</a>
			</jstl:if>		
		</jstl:if>
		</display:column>
		<spring:message code="activity.delete" var="deleteHeader"/>
		<display:column title="${deleteHeader}" sortable="true">
		<jstl:if test="${row_Activity.deleted == false && row_Activity.customers.size() == 0}">
			<a href="activity/administrator/delete.do?activityId=${row_Activity.id}"> <spring:message
					code="activity.delete" />
			</a>
		</jstl:if>
		</display:column>
	</security:authorize>

	<!-- Attributes -->
	
	<security:authorize access="hasRole('CUSTOMER')">
	<jstl:if test="${!hayGymId}">
	<spring:message code="activity.cancel" var="cancelHeader"/>
		<display:column title="${cancelHeader}"
		sortable = "true">
		<jstl:if test="${row_Activity.deleted == false && row_Activity.startingMoment > moment}">
		<a href="activity/customer/cancel.do?activityId=${row_Activity.id}"> <spring:message
				code="activity.cancel"/>
		</a>
		</jstl:if>
		</display:column>
	</jstl:if>
	</security:authorize>

	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="activity.deleted" var="deletedHeader" />
		<display:column title="${deletedHeader}" sortable="true">
			<jstl:out value="${row_Activity.deleted}" />
		</display:column>
	</security:authorize>

	<spring:message code="activity.title" var="titleHeader" />
	<display:column title="${titleHeader}"
		sortable="true">
		<jstl:out value="${row_Activity.title}"/>
	</display:column>
	
	<spring:message code="activity.numberOfSeatsAvailable" var="numberOfSeatsAvailableHeader" />
	<display:column title="${numberOfSeatsAvailableHeader}"
		sortable="true">
		<jstl:out value="${row_Activity.numberOfSeatsAvailable}"/>
	</display:column>
	
	<spring:message code="activity.startingMoment" var="startingMomentHeader" />
	<display:column title="${startingMomentHeader}"
		sortable="true">
		<jstl:out value="${row_Activity.startingMoment}"/>
	</display:column>
	
	<spring:message code="activity.duration" var="durationHeader" />
	<display:column title="${durationHeader}"
		sortable="true">
		<jstl:out value="${row_Activity.duration}"/>
	</display:column>

	<spring:message code="activity.description" var="descriptionHeader" />
	<display:column title="${descriptionHeader}"
		sortable="false">
		<jstl:out value="${row_Activity.description}"/>
	</display:column>

	<spring:message code="activity.pictures" var="picturesHeader" />
	<display:column title="${picturesHeader}"
		sortable="false" >
		<jstl:forEach items="${row_Activity.pictures}" var="picture">
			<img src="${picture}" style="width:204px;height:128px;"/>
		</jstl:forEach>
	</display:column>
	
	<spring:message code="activity.service" var="serviceHeader" />
	<display:column title="${serviceHeader}"
		sortable="false">
		<jstl:out value="${row_Activity.service.name}"/>
	</display:column>
	
	<spring:message code="activity.room.gym" var="gymHeader" />
	<display:column title="${gymHeader}"
		sortable="false">
		<jstl:out value="${row_Activity.room.gym.name}"/>
	</display:column>
	
	<spring:message code="activity.room" var="roomHeader" />
	<display:column title="${roomHeader}"
		sortable="false">
		<jstl:out value="${row_Activity.room.name}"/>
	</display:column>
	
	<spring:message code="activity.trainer" var="trainerHeader" />
	<display:column title="${trainerHeader}"
		sortable="false">
		<jstl:out value="${row_Activity.trainer.name}"/>
	</display:column>
	
	<security:authorize access="hasRole('CUSTOMER')">
	<jstl:if test="${gymPagado}">
	<spring:message code="activity.book" var="activityHeader"/>
		<display:column title="${activityHeader}"
		sortable = "true">
		<a href="activity/customer/book.do?activityId=${row_Activity.id}"> <spring:message
				code="activity.book"/>
		</a>
		</display:column>
	</jstl:if>
	</security:authorize>

</display:table>

<br/>
<br/>

<!-- Action links -->
<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="activity/administrator/create.do"> <spring:message
				code="activity.create" />
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
