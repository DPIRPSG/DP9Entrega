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
<!-- Listing grid -->
<display:table pagesize="5" class="displaytag" keepStatus="false"
	name="exchangesRates" requestURI="${requestURI}" id="row_ExchangeRate">
		<!-- Action links -->
		<display:column>
			<a href="exchangeRate/administrator/edit.do?exchangeRateId=${row_ExchangeRate.id}">
				<spring:message	code="exchangeRate.edit" />
			</a>
		</display:column>
		<!-- Attributes -->
		<spring:message code="exchangeRate.name" var="nameHeader" />
		<display:column title="${nameHeader}"
			sortable="true">
			<jstl:out value="${row_ExchangeRate.name}"/>
		</display:column>
		
		<spring:message code="exchangeRate.currency" var="currencyHeader" />
		<display:column title="${currencyHeader}"
			sortable="true" >
			<jstl:out value="${row_ExchangeRate.currency}"/>
		</display:column>
		
		<spring:message code="exchangeRate.rate" var="rateHeader" />
		<display:column title="${rateHeader}" sortable="false" >
			<jstl:out value="${row_ExchangeRate.rate}"/>
		</display:column>
		
</display:table>
<!-- Action links -->
<div>
	<a href="exchangeRate/administrator/create.do"> <spring:message
			code="exchangeRate.create" />
	</a>
</div>
	
</security:authorize>	
	



