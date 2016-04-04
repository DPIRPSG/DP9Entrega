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
	name="services" requestURI="${requestURI}" id="row_Service">
	<!-- Action links -->
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="service/administrator/edit.do?serviceId=${row_Service.id}"> <spring:message
					code="service.edit" />
			</a>
		</display:column>
	</security:authorize>


	<!-- Attributes -->
	<spring:message code="service.name" var="nameHeader" />
	<display:column title="${nameHeader}"
		sortable="true">
		<jstl:out value="${row_Service.name}"/>
	</display:column>

	<spring:message code="service.description" var="descriptionHeader" />
	<display:column title="${descriptionHeader}"
		sortable="false">
		<jstl:out value="${row_Service.description}"/>
	</display:column>
	
	<spring:message code="service.customers" var="customersHeader" />	
	<display:column title="${customersHeader}"
		sortable="true" >
		<jstl:forEach var="services" items="${customers }">
			<jstl:if test="${services[0] == row_Service.id}">
				<jstl:out value="${services[1]}" />
			</jstl:if>
		</jstl:forEach>
	</display:column>

	<spring:message code="service.pictures" var="pictureHeader" />
	<display:column title="${pictureHeader}"
		sortable="false" >
		<jstl:forEach items="${row_Service.pictures}" var="picture">
			<img src="${picture}" style="width:204px;height:128px;"/>
		</jstl:forEach>
	</display:column>

	<display:column>
		<a href="gym/list.do?serviceId=${row_Service.id}"> <spring:message
				code="service.gyms" />
		</a>
	</display:column>

	<display:column>
		<a href="comment/list.do?commentedEntityId=${row_Service.id}"> <spring:message
				code="service.comments" />
		</a>
	</display:column>

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a
				href="activity/administrator/create.do?serviceId=${row_Service.id}">
				<spring:message code="activity.create" />
			</a>

		</display:column>
	</security:authorize>



	<security:authorize access="hasRole('TRAINER')">
		<jstl:if test="${addService == 'true'}">
		
			<display:column>
				<a href="service/trainer/add.do?serviceId=${row_Service.id}"> <spring:message
					code="service.add" />
				</a>
			</display:column>
		</jstl:if>

		<jstl:if test="${addService == 'false'}">
			<display:column>
				<jstl:if test="${false}" var="seBorra"/>
				<jstl:forEach items="${servicesId}" var="serviceId">
					<jstl:if test="${serviceId != row_Service.id}">
						<jstl:if test="${true}" var="seBorra"/>
					</jstl:if>
				</jstl:forEach>
				<jstl:if test="${seBorra || sinActivity}">
					<a href="service/trainer/delete.do?serviceId=${row_Service.id}"> <spring:message
					code="service.delete" />
				</a>
				</jstl:if>
			</display:column>
		</jstl:if>

	</security:authorize>

</display:table>

<br/>
<br/>


<!-- Action links -->
<security:authorize access="hasRole('TRAINER')">
	<jstl:if test="${addService == 'false'}">
		<div>
			<a href="service/trainer/list.do"> <spring:message
				code="service.addMore" />
			</a>
		</div>
	</jstl:if>
</security:authorize>
<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="service/administrator/create.do"> <spring:message
				code="service.create" />
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
