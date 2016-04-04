<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">

	
	<!-- Dashboard 1 -->
	<h3><spring:message code="administrator.mostPopularGyms"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="mostPopularGyms" requestURI="${requestURI}" id="row1">
		<!-- Attributes -->
		<spring:message code="gym.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row1.name}"/>
 		</display:column>
 	</display:table>
 	
 	<!-- Dashboard 2 -->
	<h3><spring:message code="administrator.leastPopularGyms"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="leastPopularGyms" requestURI="${requestURI}" id="row2">
		<!-- Attributes -->
		<spring:message code="gym.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row2.name}"/>
 		</display:column>
 	</display:table>
 	
 	<!-- Dashboard 3 -->
	<h3><spring:message code="administrator.mostPopularService"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="mostPopularService" requestURI="${requestURI}" id="row3">
		<!-- Attributes -->
		<spring:message code="service.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row3.name}"/>
 		</display:column>
 	</display:table>
 	
  	<!-- Dashboard 4 -->
	<h3><spring:message code="administrator.leastPopularService"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="leastPopularService" requestURI="${requestURI}" id="row4">
		<!-- Attributes -->
		<spring:message code="service.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row4.name}"/>
 		</display:column>
 	</display:table>
 	
  	<!-- Dashboard 5 -->
	<h3><spring:message code="administrator.paidMoreFees"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="paidMoreFees" requestURI="${requestURI}" id="row5">
		<!-- Attributes -->
		<spring:message code="customer.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row5.name}"/>
 		</display:column>
 	</display:table>
	 
  	<!-- Dashboard 6 -->
	<h3><spring:message code="administrator.paidLessFees"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="paidLessFees" requestURI="${requestURI}" id="row6">
		<!-- Attributes -->
		<spring:message code="customer.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row6.name}"/>
 		</display:column>
 	</display:table>
 	
 	
 	<h3><spring:message code="administrator.averageRoomsPerGym"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${averageRoomsPerGym == 0}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${averageRoomsPerGym}" />
		</jstl:otherwise>
	</jstl:choose>
	
	 
	<h3><spring:message code="administrator.standardDeviationRoomsPerGym"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${standardDeviationRoomsPerGym == 0}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${standardDeviationRoomsPerGym}" />
		</jstl:otherwise>
	</jstl:choose>
 	
 	
 	<h3><spring:message code="administrator.gymsWithMoreRoomsThanAverage"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="gymsWithMoreRoomsThanAverage" requestURI="${requestURI}" id="row7">
		<!-- Attributes -->
		<spring:message code="gym.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row7.name}"/>
 		</display:column>
 	</display:table>
 	
 	
 	<h3><spring:message code="administrator.gymsWithLessRoomsThanAverage"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="gymsWithLessRoomsThanAverage" requestURI="${requestURI}" id="row8">
		<!-- Attributes -->
		<spring:message code="gym.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row8.name}"/>
 		</display:column>
 	</display:table>
 	 	

 	<h3><spring:message code="administrator.moreInvoicesIssuedCustomer"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="moreInvoicesIssuedCustomer" requestURI="${requestURI}" id="row9">
		<!-- Attributes -->
		<spring:message code="gym.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row9.name}"/>
 		</display:column>
 	</display:table>
 	
 	
 	<h3><spring:message code="administrator.noRequestedInvoicesCustomer"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="noRequestedInvoicesCustomer" requestURI="${requestURI}" id="row10">
		<!-- Attributes -->
		<spring:message code="gym.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row10.name}"/>
 		</display:column>
 	</display:table>
 	
  	<!-- Dashboard 7 -->
	<h3><spring:message code="administrator.sendMoreSpam"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="sendMoreSpam" requestURI="${requestURI}" id="row11">
		<!-- Attributes -->
		<spring:message code="customer.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row11.name}"/>
 		</display:column>
 	</display:table>
 	
  	<!-- Dashboard 8 -->
	<h3><spring:message code="administrator.averageNumberOfMessages"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${averageNumberOfMessages == 0}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${averageNumberOfMessages}" />
		</jstl:otherwise>
	</jstl:choose>
	
	
	
	<h3><spring:message code="administrator.activitiesByPopularity"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="activitiesByPopularity" requestURI="${requestURI}" id="row12">
		<!-- Attributes -->
		<spring:message code="activity.title" var="titleHeader" />
		<display:column title="${titleHeader}" 
 			sortable="false" >
 			<jstl:out value="${row12.title}"/>
 		</display:column>
 	</display:table>
	
	<h3><spring:message code="administrator.averageNumberOfActivitiesPerGymByService"/></h3>
	<!-- Result -->
	<jstl:forEach var="x" items="${averageNumberOfActivitiesPerGymByService}">
	<jstl:choose>
  		<jstl:when test="${x == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${x.key}" />
			<jstl:out value="${x.value}" />
			</br>
		</jstl:otherwise>
	</jstl:choose>
	</jstl:forEach>
	
	<h3><spring:message code="administrator.averageNumberOfServiceWithSpecialisedTrainer"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${averageNumberOfServiceWithSpecialisedTrainer == 0}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${averageNumberOfServiceWithSpecialisedTrainer}" />
		</jstl:otherwise>
	</jstl:choose>
	
	<h3><spring:message code="administrator.mostPopularServiceByNumberOfTrainer"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="mostPopularServiceByNumberOfTrainer" requestURI="${requestURI}" id="row13">
		<!-- Attributes -->
		<spring:message code="service.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row13.name}"/>
 		</display:column>
 	</display:table>
	
	
	
  	<!-- Dashboard 9 -->
	<h3><spring:message code="administrator.moreCommentedGyms"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="moreCommentedGyms" requestURI="${requestURI}" id="row14">
		<!-- Attributes -->
		<spring:message code="gym.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row14.name}"/>
 		</display:column>
 	</display:table>
 	
  	<!-- Dashboard 10 -->
	<h3><spring:message code="administrator.moreCommentedServices"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="moreCommentedServices" requestURI="${requestURI}" id="row15">
		<!-- Attributes -->
		<spring:message code="service.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row15.name}"/>
 		</display:column>
 	</display:table>
 	
  	<!-- Dashboard 11 -->
	<h3><spring:message code="administrator.averageNumberOfComments"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${averageNumberOfComments == 0}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${averageNumberOfComments}" />
		</jstl:otherwise>
	</jstl:choose>

  	<!-- Dashboard 11 -->
	<h3><spring:message code="administrator.standardDeviationNumberOfComments"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${standardDeviationNumberOfComments == 0}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${standardDeviationNumberOfComments}" />
		</jstl:otherwise>
	</jstl:choose>
	
  	<!-- Dashboard 12 -->
	<h3><spring:message code="administrator.averageNumberOfCommentsPerGym"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${averageNumberOfCommentsPerGym == 0}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${averageNumberOfCommentsPerGym}" />
		</jstl:otherwise>
	</jstl:choose>
	
  	<!-- Dashboard 13 -->
	<h3><spring:message code="administrator.averageNumberOfCommentsPerService"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${averageNumberOfCommentsPerService == 0}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${averageNumberOfCommentsPerService}" />
		</jstl:otherwise>
	</jstl:choose>
	
  	<!-- Dashboard 14 -->
	<h3><spring:message code="administrator.removedMoreComments"/></h3>
	<!-- Listing grid -->
	<display:table pagesize="5" class="displaytag" keepStatus="true"
 		name="removedMoreComments" requestURI="${requestURI}" id="row16">
		<!-- Attributes -->
		<spring:message code="customer.name" var="nameHeader" />
		<display:column title="${nameHeader}" 
 			sortable="false" >
 			<jstl:out value="${row16.name}"/>
 		</display:column>
 	</display:table>
 	
 	<h3><spring:message code="administrator.servicesWithTrainesSpecialized"/></h3>
	<!-- Result -->
	<jstl:forEach var="x" items="${servicesWithTrainesSpecialized}">
	<jstl:choose>
  		<jstl:when test="${x == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${x.key.name}" />
			<jstl:out value="${x.value}" />
			</br>
		</jstl:otherwise>
	</jstl:choose>
	</jstl:forEach>
	
	<h3><spring:message code="administrator.ratioOfTrainerWithCurriculumUpToDate"/></h3>
	<!-- Result -->
	<jstl:choose>
  		<jstl:when test="${ratioOfTrainerWithCurriculumUpToDate == null}">
 			<spring:message code="administrator.ratio.null"/>
		</jstl:when>
  		<jstl:otherwise>
			<jstl:out value="${ratioOfTrainerWithCurriculumUpToDate}" />
			</br>
		</jstl:otherwise>
	</jstl:choose>

</security:authorize>